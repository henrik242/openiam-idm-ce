package org.openiam.spml2.spi.common.jdbc;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/17/12
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonJDBCModifyCommand extends AbstractJDBCCommand {

    private static final String UPDATE_SQL = "UPDATE %s SET %s WHERE %s=?";
    private static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES (%s)";

    public ModifyResponseType modify(final ModifyRequestType reqType) {
        Connection con = null;


        final ModifyResponseType response = new ModifyResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        //String requestID = reqType.getRequestID();
        /* PSO - Provisioning Service Object -
*     -  ID must uniquely specify an object on the target or in the target's namespace
*     -  Try to make the PSO ID immutable so that there is consistency across changes. */
        final PSOIdentifierType psoID = reqType.getPsoID();
        final String principalName = psoID.getID();

        /* targetID -  */
        final String targetID = psoID.getTargetID();


        /* A) Use the targetID to look up the connection information under managed systems */
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

        final ResourceProp prop = res.getResourceProperty("TABLE_NAME");
        if(prop == null) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "No TABLE_NAME property found");
            return response;
        }

        final String tableName = prop.getPropValue();
        if (tableName == null || tableName.length() == 0) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "TABLE NAME is not defined.");
            return response;
        }

        // modificationType contains a collection of objects for each type of operation
        final List<ModificationType> modificationList = reqType.getModification();

        if(log.isDebugEnabled()) {
            log.debug(String.format("ModificationList = %s", modificationList));
        }


        final List<ModificationType> modTypeList = reqType.getModification();

        try {
            con = connectionMgr.connect(managedSys);

            for (ModificationType mod : modTypeList) {
                final ExtensibleType extType = mod.getData();
                final List<ExtensibleObject> extobjectList = extType.getAny();

                int ctr = 0;
                for (final ExtensibleObject obj : extobjectList) {
                    if (identityExists(con, tableName, principalName, obj)) {
                        if(log.isDebugEnabled()) {
                            log.debug(String.format("Identity found. Modifying identity: %s", principalName));
                        }

                        final StringBuilder setBuffer = new StringBuilder();

                        final List<ExtensibleAttribute> attrList = obj.getAttributes();
                        final String principalFieldName = obj.getPrincipalFieldName();
                        final String principalFieldDataType = obj.getPrincipalFieldDataType();

                        for (ExtensibleAttribute att : attrList) {
                            if (att.getOperation() != 0 && att.getName() != null) {
                                if (att.getObjectType().equalsIgnoreCase("USER")) {
                                    if (ctr != 0) {
                                        setBuffer.append(",");
                                    }
                                    ctr++;

                                    setBuffer.append(String.format("%s = ?", att.getName()));
                                }
                            }
                        }

                        if (ctr > 0) {
                            final String sql = String.format(UPDATE_SQL, tableName, setBuffer, principalFieldName)    ;

                            if(log.isDebugEnabled()) {
                                log.debug(String.format("SQL=%s", sql));
                            }

                            final PreparedStatement statement = con.prepareStatement(sql);

                            ctr = 1;
                            for (final ExtensibleAttribute att : attrList) {
                                if (att.getOperation() != 0 && att.getName() != null) {
                                    if(StringUtils.equalsIgnoreCase(att.getObjectType(), "user")) {
                                        setStatement(statement, ctr, att);
                                        ctr++;
                                    }

                                }

                            }
                            if (principalFieldName != null) {
                                setStatement(statement, ctr, principalFieldDataType, principalName);
                            }

                            statement.executeUpdate();
                        }

                    } else {

                        // identity does not exist in the target system
                        // identity needs to be re-provisioned
                        addIdentity(con, tableName, principalName, obj);
                    }

                }
            }
        } catch (SQLException se) {
            log.error(se);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR, se.toString());

        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, cnfe.toString());
        } catch (ParseException pe) {
            log.error(pe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, pe.toString());
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

    private void addIdentity(final Connection con, final String tableName, final String principalName, final ExtensibleObject obj)
            throws SQLException, ParseException {
        // build sql

        final StringBuilder columnBuf = new StringBuilder("");
        final StringBuilder valueBuf = new StringBuilder("");

        final List<ExtensibleAttribute> attrList = obj.getAttributes();

        final String principalFieldName = obj.getPrincipalFieldName();
        //String principalFieldDataType = obj.getPrincipalFieldDataType();

        if(log.isDebugEnabled()) {
            log.debug(String.format("Adding identity: %s", principalName));
            log.debug(String.format("Number of attributes to persist in ADD = %s", attrList.size()));
        }

        int ctr = 0;
        for (final ExtensibleAttribute att : attrList) {
            if (ctr != 0) {
                columnBuf.append(",");
                valueBuf.append(",");
            }
            ctr++;
            columnBuf.append(att.getName());
            valueBuf.append("?");
        }
        // add the primary key
        log.debug("Principal column name=" + obj.getPrincipalFieldName());
        if (principalFieldName != null) {
            if (ctr != 0) {
                columnBuf.append(",");
                valueBuf.append(",");
            }
            columnBuf.append(obj.getPrincipalFieldName());
            valueBuf.append("?");
        }

        final String sql = String.format(INSERT_SQL, tableName, columnBuf, valueBuf);

        if(log.isDebugEnabled()) {
            log.debug(String.format("ADD SQL=%s", sql));
        }

        final PreparedStatement statement = con.prepareStatement(sql);

        ctr = 1;
        for (final ExtensibleAttribute att : attrList) {
            setStatement(statement, ctr, att);
            ctr++;
            if(log.isDebugEnabled()) {
                log.debug(String.format("Binding parameter: %s -> %s", att.getName(), att.getValue()));
            }

        }


        if (obj.getPrincipalFieldName() != null) {
            setStatement(statement, ctr, obj.getPrincipalFieldDataType(), principalName);
        }

        statement.executeUpdate();


    }
}
