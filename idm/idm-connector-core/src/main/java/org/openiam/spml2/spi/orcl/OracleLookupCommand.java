package org.openiam.spml2.spi.orcl;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.common.LookupCommand;

import java.sql.*;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class OracleLookupCommand extends AbstractOracleCommand implements LookupCommand {

    private static final String SELECT_USER = "SELECT * FROM DBA_USERS WHERE USERNAME=?";

    @Override
    public LookupResponseType lookup(LookupRequestType reqType) {
        boolean found = false;

        if(log.isDebugEnabled()) {
            log.debug("AppTable lookup operation called.");
        }

        final LookupResponseType response = new LookupResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        final String principalName = reqType.getPsoID().getID();

        final PSOIdentifierType psoID = reqType.getPsoID();
        /* targetID -  */
        final String targetID = psoID.getTargetID();

        final ManagedSys managedSys = managedSysService.getManagedSys(targetID);
        if(managedSys == null) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, String.format("No Managed System with target id: %s", targetID));
            return response;
        }

        if (StringUtils.isBlank(managedSys.getResourceId())) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "ResourceID is not defined in the ManagedSys Object");
            return response;
        }

        final Resource res = resourceDataService.getResource(managedSys.getResourceId());
        if(res == null) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "No resource for managed resource found");
            return response;
        }

        Connection con = null;

        try {
            final ExtensibleObject resultObject = new ExtensibleObject();
            resultObject.setObjectId(principalName);

            con = connectionMgr.connect(managedSys);

            final PreparedStatement statement = con.prepareStatement(SELECT_USER);
            statement.setString(1, principalName);

            final ResultSet rs = statement.executeQuery();
            final ResultSetMetaData rsMetadata = rs.getMetaData();
            int columnCount = rsMetadata.getColumnCount();

            if(log.isDebugEnabled()) {
                log.debug(String.format("Query contains column count = %s",columnCount));
            }

            if (rs.next()) {
                found = true;
                for (int colIndx = 1; colIndx <= columnCount; colIndx++) {
                    final ExtensibleAttribute extAttr = new ExtensibleAttribute();
                    extAttr.setName(rsMetadata.getColumnName(colIndx));
                    setColumnValue(extAttr, colIndx, rsMetadata, rs);
                    resultObject.getAttributes().add(extAttr);
                }
                response.getAny().add(resultObject);
                response.setStatus(StatusCodeType.SUCCESS);
            } else {
                if(log.isDebugEnabled()) {
                    log.debug("Principal not found");
                }
                response.setStatus(StatusCodeType.FAILURE);
                return response;
            }
        } catch (SQLException se) {
            log.error(se);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR, se.toString());
        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, cnfe.toString());
        } catch(Throwable e) {
            log.error(e);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.OTHER_ERROR, e.toString());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException s) {
                    log.error(s);
                    populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR, s.toString());
                }
            }
        }
        return response;
    }

    private void setColumnValue(ExtensibleAttribute extAttr, int colIndx, ResultSetMetaData rsMetadata, ResultSet rs)
            throws SQLException {

        final int fieldType = rsMetadata.getColumnType(colIndx);

        if(log.isDebugEnabled()) {
            log.debug(String.format("column type = %s", fieldType));
        }

        if (fieldType == Types.INTEGER) {
            if(log.isDebugEnabled()) {
                log.debug("type = Integer");
            }
            extAttr.setDataType("INTEGER");
            extAttr.setValue(String.valueOf(rs.getInt(colIndx)));
        }

        if (fieldType == Types.FLOAT || fieldType == Types.NUMERIC) {
            if(log.isDebugEnabled()) {
                log.debug("type = Float");
            }
            extAttr.setDataType("FLOAT");
            extAttr.setValue(String.valueOf(rs.getFloat(colIndx)));

        }

        if (fieldType == Types.DATE) {
            if(log.isDebugEnabled()) {
                log.debug("type = Date");
            }
            extAttr.setDataType("DATE");
            if (rs.getDate(colIndx) != null) {
                extAttr.setValue(String.valueOf(rs.getDate(colIndx).getTime()));
            }

        }
        if (fieldType == Types.TIMESTAMP) {
            if(log.isDebugEnabled()) {
                log.debug("type = Timestamp");
            }
            extAttr.setDataType("TIMESTAMP");
            extAttr.setValue(String.valueOf(rs.getTimestamp(colIndx).getTime()));

        }
        if (fieldType == Types.VARCHAR || fieldType == Types.CHAR) {
            if(log.isDebugEnabled()) {
                log.debug("type = Varchar");
            }
            extAttr.setDataType("STRING");
            if (rs.getString(colIndx) != null) {
                extAttr.setValue(rs.getString(colIndx));
            }

        }
    }
}
