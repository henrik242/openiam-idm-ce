package org.openiam.spml2.spi.common.jdbc;

import org.apache.commons.lang.StringUtils;
import org.openiam.exception.EncryptionException;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
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
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonJDBCResumeCommand extends AbstractJDBCCommand {

    private LoginDataService loginManager;

    public ResponseType resume(ResumeRequestType request) {
        Connection con = null;

        final ResponseType response = new ResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        final String principalName = request.getPsoID().getID();

        final PSOIdentifierType psoID = request.getPsoID();
        /* targetID -  */
        final String targetID = psoID.getTargetID();

        List<Login> loginList = loginManager.getLoginByManagedSys(principalName, targetID);
        if (loginList == null || loginList.isEmpty()) {
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_IDENTIFIER, "Principal not found");
            return response;
        }

        try {

            final Login login = loginList.get(0);
            final String encPassword = login.getPassword();
            final String decPassword = loginManager.decryptPassword(encPassword);

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
            if (StringUtils.isBlank(tableName)) {
                populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, "TABLE NAME is not defined.");
                return response;
            }


            con = connectionMgr.connect(managedSys);

            final PreparedStatement statement = createSetPasswordStatement(con, res, tableName, principalName, decPassword);

            statement.executeUpdate();

        } catch (SQLException se) {
            log.error(se);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.SQL_ERROR, se.toString());
        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, cnfe.toString());
        } catch (ParseException pe) {
            log.error(pe);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.INVALID_CONFIGURATION, pe.toString());
        } catch (EncryptionException ee) {
            log.error(ee);
            populateResponse(response, StatusCodeType.FAILURE, ErrorCode.OTHER_ERROR, ee.toString());
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

    @Required
    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }
}
