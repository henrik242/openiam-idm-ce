package org.openiam.spml2.spi.jdbc;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.common.DeleteCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/17/12
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppTableDeleteCommand extends AbstractAppTableCommand implements DeleteCommand {

    public ResponseType delete(final DeleteRequestType reqType) {
        final ResponseType response = new ResponseType();
        response.setStatus(StatusCodeType.SUCCESS);


        final String principalName = reqType.getPsoID().getID();

        final PSOIdentifierType psoID = reqType.getPsoID();
        final String targetID = psoID.getTargetID();

        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        if(managedSys == null) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, String.format("No Managed System with target id: %s", targetID));
            return response;
        }

        if (StringUtils.isBlank(managedSys.getResourceId())) {
            populateResponse(response, StatusCodeType.FAILURE,  ErrorCode.INVALID_CONFIGURATION, "ResourceID is not defined in the ManagedSys Object");
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
        if (StringUtils.isBlank(tableName)) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION,  "TABLE NAME is not defined.");
            return response;
        }

        Connection con = null;
        try {
            con = connectionMgr.connect(managedSys);

            final PreparedStatement statement = createDeleteStatement(con, res, tableName, principalName);
            statement.executeUpdate();
        } catch (SQLException se) {
            log.error(se);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR,  se.toString());
        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION,  cnfe.toString());
        } catch (ParseException pe) {
            log.error(pe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION,  pe.toString());
        } catch(Throwable e) {
            log.error(e);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.OTHER_ERROR, e.toString());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException s) {
                    log.error(s);
                    populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR,  s.toString());
                }
            }
        }


        return response;


    }
}
