package org.openiam.selfsrvc.pswd;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ExtendController;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.msg.dto.NotificationParam;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.pswd.dto.PasswordResetTokenRequest;
import org.openiam.idm.srvc.pswd.dto.PasswordResetTokenResponse;
import org.openiam.idm.srvc.pswd.ws.PasswordWebService;
import org.openiam.idm.srvc.user.dto.DelegationFilterSearch;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.service.ProvisionService;
import org.openiam.script.ScriptIntegration;
import org.openiam.selfsrvc.helper.ScriptEngineUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Controller for SecureUnlockUser
 * @author suneet
 *
 */
public class SecureUnlockUserController extends CancellableFormController {


	protected UserDataWebService userMgr;
	protected LoginDataWebService loginManager;
	protected PasswordConfiguration configuration;
    protected String extendController;
	String defaultDomainId;
	String menuGroup;
    protected String pwsdResetCancelView;
    protected MailService mailService;
    protected PasswordWebService passwordService;

	private static final Log log = LogFactory.getLog(SecureUnlockUserController.class);
    protected static  ResourceBundle res = ResourceBundle.getBundle("datasource");


	public SecureUnlockUserController() {
		super();



    }
	




	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		


        // update the password in the openiam repository of the primary id
        String managedSysId = configuration.getDefaultManagedSysId();
        String secDomainId = configuration.getDefaultSecurityDomain();



        SecureUnlockUserCommand unlockCmd =(SecureUnlockUserCommand)command;
		
		// get objects from the command object
		String principal = unlockCmd.getPrincipal();

        // get the user object
        Login lg = loginManager.getLoginByManagedSys(configuration.getDefaultSecurityDomain(),
                principal,
                /* Our primary identity */
                configuration.getDefaultManagedSysId()).getPrincipal();

        User u = userMgr.getUserWithDependent(lg.getUserId(),false).getUser();


        // generate a temporary token for this reset

        PasswordResetTokenRequest tokenRequest = new PasswordResetTokenRequest(secDomainId,principal,managedSysId);
        PasswordResetTokenResponse resp =  passwordService.generatePasswordResetToken(tokenRequest);
        String token =  resp.getPasswordResetToken();

        notify(u.getUserId(), token);


		ModelAndView mav = new ModelAndView(getSuccessView());

		
		return mav;
	}

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {

        return new ModelAndView(new RedirectView("/login.selfserve",true));

    }


    private void notify(String userId, String token) {

        String baseUrl = res.getString("APP_BASE_URL");

        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setNotificationType("REQUEST_PASSWORD_RESET");

        request.getParamList().add(new NotificationParam("BASE_URL", baseUrl ));
        request.getParamList().add(new NotificationParam("TOKEN", token));

        mailService.sendNotification(request);
    }



	public PasswordConfiguration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(PasswordConfiguration configuration) {
		this.configuration = configuration;
	}



	public UserDataWebService getUserMgr() {
		return userMgr;
	}


	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}


	public LoginDataWebService getLoginManager() {
		return loginManager;
	}


	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}

    public String getExtendController() {
        return extendController;
    }

    public void setExtendController(String extendController) {
        this.extendController = extendController;
    }

    public String getPwsdResetCancelView() {
        return pwsdResetCancelView;
    }

    public void setPwsdResetCancelView(String pwsdResetCancelView) {
        this.pwsdResetCancelView = pwsdResetCancelView;
    }



    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public PasswordWebService getPasswordService() {
        return passwordService;
    }

    public void setPasswordService(PasswordWebService passwordService) {
        this.passwordService = passwordService;
    }
}
