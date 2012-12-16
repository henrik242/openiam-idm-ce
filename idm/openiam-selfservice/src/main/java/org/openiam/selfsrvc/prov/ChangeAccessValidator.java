package org.openiam.selfsrvc.prov;

import java.util.List;

import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;


public class ChangeAccessValidator implements Validator {
	protected UserDataWebService userMgr;
	protected LoginDataWebService loginManager;
    protected String newHireProcessId;
	



	public UserDataWebService getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}

	public boolean supports(Class cls) {
		 return ChangeAccessCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

		ChangeAccessCommand changeCmd =  (ChangeAccessCommand) cmd;

		// validate that workflow is selected

        String workflowId = changeCmd.getWorkflowResourceId();

        if ( changeCmd.getWorkflowResourceId() == null || changeCmd.getWorkflowResourceId().isEmpty()    ) {
            err.rejectValue("workflowResourceId","required");

        }

        // except for newHire, enforce that a user has been selected for the workflow
        if (!newHireProcessId.equalsIgnoreCase(workflowId)) {

            if ( changeCmd.getUserId() == null || changeCmd.getUserId().isEmpty() ) {
                err.rejectValue("userId","required");
            }

        }


		
	}

	public LoginDataWebService getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}

    public String getNewHireProcessId() {
        return newHireProcessId;
    }

    public void setNewHireProcessId(String newHireProcessId) {
        this.newHireProcessId = newHireProcessId;
    }
}
