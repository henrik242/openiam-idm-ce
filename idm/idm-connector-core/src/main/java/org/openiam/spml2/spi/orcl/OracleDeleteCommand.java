package org.openiam.spml2.spi.orcl;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.common.DeleteCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class OracleDeleteCommand extends AbstractOracleCommand implements DeleteCommand {

    private static final String DROP_USER = "DROP USER \"%s\"";

    @Override
    public ResponseType delete(DeleteRequestType reqType) {
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

        Connection con = null;
        try {
            final String sql = String.format(DROP_USER, principalName);
            con = connectionMgr.connect(managedSys);
            con.createStatement().execute(sql);
        } catch (SQLException se) {
            log.error(se);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR,  se.toString());
        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION,  cnfe.toString());
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
