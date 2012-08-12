package org.openiam.selfsrvc.pswd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyAttribute;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.idm.srvc.pswd.dto.Password;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;
import org.openiam.idm.srvc.pswd.service.ChallengeResponseService;
import org.openiam.idm.srvc.pswd.ws.PasswordWebService;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class SecureUnlockUserValidator implements Validator {
	
	protected LoginDataWebService loginManager;
	protected PasswordConfiguration configuration;
	protected ChallengeResponseService challengeResponse;
	protected PolicyDataService policyDataService;
	protected SecurityDomainDataService secDomainService;
	protected UserDataWebService userMgr;
	
	private static final Log log = LogFactory.getLog(SecureUnlockUserValidator.class);

    public void validate(Object cmd, Errors err) {
        boolean required = true;

        log.debug("unlockUser validator called.");

        SecureUnlockUserCommand idQuestCmd =  (SecureUnlockUserCommand) cmd;

        if (idQuestCmd.getPrincipal() == null || idQuestCmd.getPrincipal().length() == 0 ) {
            err.rejectValue("principal","required");
            return;
        }
        // check if the login exists
        Login lg = loginManager.getLoginByManagedSys(configuration.getDefaultSecurityDomain(),
                idQuestCmd.getPrincipal(),
                /* Our primary identity */
                configuration.getDefaultManagedSysId()).getPrincipal();
        if (lg == null) {
            err.rejectValue("principal","invalid");
            return;
        }
        // make sure that the account has not been locked by the admin
        User u = userMgr.getUserWithDependent(lg.getUserId(),false).getUser();
        if (u.getSecondaryStatus() == UserStatusEnum.LOCKED_ADMIN) {
            err.rejectValue("principal","adminlocked");
            return;
        }

        if (u.getEmail() == null || u.getEmail().isEmpty()) {
            err.rejectValue("principal","noemail");
            return;
        }



        // check if this user has answered the challenge response questions
        boolean answerStatus = challengeResponse.userAnserExists(lg.getUserId());
        if (!answerStatus) {
            err.rejectValue("principal", "noanswers");
            return;
        }

    }



	 


		


	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class cls) {
		return SecureUnlockUserCommand.class.equals(cls);
	}

	public PasswordConfiguration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(PasswordConfiguration configuration) {
		this.configuration = configuration;
	}
	public ChallengeResponseService getChallengeResponse() {
		return challengeResponse;
	}
	public void setChallengeResponse(ChallengeResponseService challengeResponse) {
		this.challengeResponse = challengeResponse;
	}
	
	public PolicyDataService getPolicyDataService() {
		return policyDataService;
	}
	public void setPolicyDataService(PolicyDataService policyDataService) {
		this.policyDataService = policyDataService;
	}
	public SecurityDomainDataService getSecDomainService() {
		return secDomainService;
	}
	public void setSecDomainService(SecurityDomainDataService secDomainService) {
		this.secDomainService = secDomainService;
	}
	public LoginDataWebService getLoginManager() {
		return loginManager;
	}
	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}

	public UserDataWebService getUserMgr() {
		return userMgr;
	}
	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}
	
}
	
