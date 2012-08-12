/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.selfsrvc.pswd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyAttribute;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;
import org.openiam.idm.srvc.pswd.dto.ValidatePasswordResetTokenResponse;
import org.openiam.idm.srvc.pswd.service.ChallengeResponseService;
import org.openiam.idm.srvc.pswd.ws.PasswordWebService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.service.ProvisionService;
import org.openiam.selfsrvc.hire.NewHireCommand;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openiam.idm.srvc.auth.dto.Login;

/**
 * @author suneet
 *
 */
public class SecureIdVerificationWizardController extends AbstractWizardFormController {

	protected LoginDataWebService loginManager;
	protected PasswordConfiguration configuration;
	protected PolicyDataService policyDataService;
	protected SecurityDomainDataService secDomainService;
	protected ChallengeResponseService challengeResponse;
    protected PasswordWebService passwordService;
	
	protected static final Log log = LogFactory.getLog(SecureIdVerificationWizardController.class);
	protected ProvisionService provisionService;



    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        SecureIdVerificationCommand cmd = new SecureIdVerificationCommand();

        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {

            ValidatePasswordResetTokenResponse tokenResponse = passwordService.validatePasswordResetToken(token);
            if ( tokenResponse.getStatus() == ResponseStatus.FAILURE ) {
                cmd.setMessage("Invalid password reset identifier. Unable to verify user identity.");
                return cmd;
            }

            Login principal =  tokenResponse.getPrincipal();

            System.out.println("Found principal =" + principal);

            cmd.setPrincipal(principal.getId().getLogin());


            loadChallengeQuestions(cmd, principal);



        }else {
            cmd.setMessage("Invalid parameters found. Unable to verify user identity.");
        }



        return cmd;
    }

    private void loadChallengeQuestions(SecureIdVerificationCommand cmd, Login login) {


        List<UserIdentityAnswer> answerList = challengeResponse.answersByUser(login.getUserId());
        for (UserIdentityAnswer ans : answerList) {
            // dont want to show the answer on the UI for the challenge response
            ans.setQuestionAnswer(null);
        }
        cmd.setAnswerList(answerList);

        cmd.setQuestionCount(answerList.size());
        Policy policy = passwordService.getPasswordPolicy(login.getId().getDomainId(),
                login.getId().getLogin(), login.getId().getManagedSysId()).getPolicy();

        PolicyAttribute atr = policy.getAttribute("QUEST_ANSWER_CORRECT");

        String strRequiredAnswer = atr.getValue1();
        if (strRequiredAnswer == null || strRequiredAnswer.length() == 0 ) {
            cmd.setRequiredCorrect(answerList.size());
        }else {
            cmd.setRequiredCorrect(Integer.parseInt(atr.getValue1()));
        }
    }
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException arg3)
			throws Exception {

        SecureIdVerificationCommand cmd  =(SecureIdVerificationCommand)command;
		
		// get objects from the command object
		String password = cmd.getPassword();
		
		// update the password in the openiam repository of the primary id
		String managedSysId = configuration.getDefaultManagedSysId();
		String secDomainId = configuration.getDefaultSecurityDomain();
		

		log.info("-Sync password start");
		PasswordSync passwordSync = new PasswordSync("RESET PASSWORD", managedSysId, password, 
				cmd.getPrincipal(), null, secDomainId, "SELFSERVICE", false );

        passwordSync.setRequestClientIP(request.getRemoteHost());
        passwordSync.setRequestorLogin(cmd.getPrincipal());
        passwordSync.setRequestorDomain(secDomainId);

		provisionService.setPassword(passwordSync);
			
		log.info("-Sync password complete");
		
		
		  Map model = new HashMap();   
	      model.put("message", "Job done!");   
	      return new ModelAndView("pub/confirm");   
	        
	}

	@Override
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		Map model = new HashMap();   
        model.put("message", "Request to reset the password has been canceled");   
        return new ModelAndView(new RedirectView("/login.selfserve",true));
        
	}

	@Override
	protected void validatePage(Object command, Errors errors, int page) {
		log.debug("Validate page:" + page);
        SecureIdVerificationValidator validator = (SecureIdVerificationValidator)getValidator();
		switch (page) {

		case 0:
			validator.validateUnlockVerifyIdentity(command, errors);
			break;
		case 1:
			validator.validateUnlockSetNewPassword(command, errors);
			break;
		}
		
	}



	public PasswordConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PasswordConfiguration configuration) {
		this.configuration = configuration;
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

	public ChallengeResponseService getChallengeResponse() {
		return challengeResponse;
	}

	public void setChallengeResponse(ChallengeResponseService challengeResponse) {
		this.challengeResponse = challengeResponse;
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
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
}
