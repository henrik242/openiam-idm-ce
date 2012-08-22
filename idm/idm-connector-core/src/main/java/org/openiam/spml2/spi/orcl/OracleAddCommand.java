package org.openiam.spml2.spi.orcl;

import groovy.json.StringEscapeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.spi.common.AddCommand;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class OracleAddCommand extends  AbstractOracleCommand implements AddCommand {

    private static final String INSERT_SQL = "CREATE USER \"%s\" IDENTIFIED BY \"%s\"";

    @Override
    public AddResponseType add(AddRequestType reqType) {
        final AddResponseType response = new AddResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        final String targetID = reqType.getTargetID();
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

        final String principalName = reqType.getPsoID().getID();
        if(principalName == null) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "No principal sent");
            return response;
        }

        final List<ExtensibleObject> objectList = reqType.getData().getAny();

        if(log.isDebugEnabled()) {
            log.debug(String.format("ExtensibleObject in Add Request=%s", objectList));
        }

        final List<AttributeMap> attributeMap = attributeMaps(res);

        Connection con = null;
        try {
            con = connectionMgr.connect(managedSys);

            if (identityExists(con, principalName)) {
                if(log.isDebugEnabled()) {
                    log.debug(String.format("%s exists. Returning success to the connector", principalName));
                }
                return response;
            }

            String identifiedBy = null;
            for (final ExtensibleObject obj : objectList) {
                final List<ExtensibleAttribute> attrList = obj.getAttributes();

                if(log.isDebugEnabled()) {
                    log.debug(String.format("Number of attributes to persist in ADD = %s", attrList.size()));
                }

                if(CollectionUtils.isNotEmpty(attributeMap)) {
                    for (final ExtensibleAttribute att : attrList) {
                        for(final AttributeMap attribute : attributeMap) {
                            if(StringUtils.equalsIgnoreCase("password", attribute.getMapForObjectType())) {
                                if(StringUtils.equalsIgnoreCase(att.getName(),  attribute.getAttributeName())) {
                                    identifiedBy = att.getValue();
                                }
                            }
                        }
                    }
                }
            }

            if(StringUtils.isBlank(identifiedBy)) {
                populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_ATTRIBUTE, "No password specified");
                return response;
            }

            final String sql = String.format(INSERT_SQL, principalName, identifiedBy);
            if(log.isDebugEnabled()) {
                log.debug(String.format("SQL=%s", sql));
            }

            con.createStatement().execute(sql);
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
                    log.error(s.toString());
                    populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR, s.toString());
                }
            }
        }


        return response;
    }
}
