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

public class SecureIdVerificationValidator implements Validator {
	
	protected LoginDataWebService loginManager;
	protected PasswordConfiguration configuration;
	protected ChallengeResponseService challengeResponse;
	protected PolicyDataService policyDataService;
	protected SecurityDomainDataService secDomainService;
	protected PasswordWebService passwordService;
	protected UserDataWebService userMgr;
	
	private static final Log log = LogFactory.getLog(SecureIdVerificationValidator.class);

	public void validate(Object cmd, Errors err) {
        SecureIdVerificationCommand idQuestCmd =  (SecureIdVerificationCommand) cmd;
	}



	public void validateUnlockVerifyIdentity(Object cmd, Errors err) {
		// Page 0
		
		boolean required = true;
		log.debug("unlockVerifyIdentity validator called.");
        SecureIdVerificationCommand idQuestCmd =  (SecureIdVerificationCommand) cmd;
		
		List<UserIdentityAnswer> ansList = idQuestCmd.getAnswerList();

		log.debug("AsnwerList=" + ansList);

		boolean valid = challengeResponse.isResponseValid(configuration.getDefaultSecurityDomain(),
				idQuestCmd.getPrincipal(),
				configuration.getDefaultManagedSysId(),
				configuration.getDefaultChallengeResponseGroup(),
				ansList);
		if (!valid) {
			err.rejectValue("domainId","notequal");
		}

		
	}
	public void validateUnlockSetNewPassword(Object cmd, Errors err) {
		// Page 1 (Finish)

		boolean required = true;
		log.debug("unlockSetNewPassword validator called.");
        SecureIdVerificationCommand idQuestCmd =  (SecureIdVerificationCommand) cmd;
		
		// validate the password against the policy
		if (idQuestCmd.getPassword() == null || idQuestCmd.getPassword().length() == 0 ) {
			err.rejectValue("password","required");
			required = false;
		}
		if (idQuestCmd.getConfPassword() == null || idQuestCmd.getConfPassword().length() == 0 ) {
			err.rejectValue("confPassword","required");
			required = false;
		}
		if (!required) {
			return;
		}
		if ( !idQuestCmd.getConfPassword().equals(idQuestCmd.getPassword() )) {
			err.rejectValue("confPassword","notequal");
			required = false;
			return;
		}
		
		// validate the password against the policy
		Password pswd = new Password();
		pswd.setDomainId(configuration.getDefaultSecurityDomain());
		pswd.setManagedSysId(configuration.getDefaultManagedSysId());
		pswd.setPrincipal(idQuestCmd.getPrincipal());
		pswd.setPassword(idQuestCmd.getPassword());
		
		
		try {
			Response resp = passwordService.isPasswordValid(pswd);
			if (resp.getStatus() == ResponseStatus.FAILURE) {
				
				
				err.rejectValue("password",resp.getErrorCode().toString());
				required = false;				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	 

	public void initQuestion(SecureIdVerificationCommand cmd, String userId) {
		log.debug("Answerlist in the command object=" + cmd.getAnswerList());
		if (cmd.getAnswerList() != null && cmd.getAnswerList().size() > 0) {
			// dont load the list if it has already been loaded.
			return;
		}
		
		List<UserIdentityAnswer> answerList = challengeResponse.answersByUser(userId);
		for (UserIdentityAnswer ans : answerList) {
			// dont want to show the answer on the UI for the challenge response
			ans.setQuestionAnswer(null);
		}		
		cmd.setAnswerList(answerList);
		
		cmd.setQuestionCount(answerList.size());
		Policy policy = passwordService.getPasswordPolicy(configuration.getDefaultSecurityDomain(), 
				cmd.getPrincipal(), configuration.getDefaultManagedSysId()).getPolicy();
		PolicyAttribute atr = policy.getAttribute("QUEST_ANSWER_CORRECT");
		
		String strRequiredAnswer = atr.getValue1();
		if (strRequiredAnswer == null || strRequiredAnswer.length() == 0 ) {
			cmd.setRequiredCorrect(answerList.size());
		}else {
			cmd.setRequiredCorrect(Integer.parseInt(atr.getValue1()));
		}
	}
		


	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class cls) {
		return SecureIdVerificationCommand.class.equals(cls);
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
	public PasswordWebService getPasswordService() {
		return passwordService;
	}
	public void setPasswordService(PasswordWebService passwordService) {
		this.passwordService = passwordService;
	}
	public UserDataWebService getUserMgr() {
		return userMgr;
	}
	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}
	
}
	
