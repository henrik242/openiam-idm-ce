package org.openiam.webadmin.user;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.pswd.dto.Password;
import org.openiam.idm.srvc.pswd.ws.PasswordWebService;
import org.openiam.webadmin.admin.AppConfiguration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ResetUserPasswordValidator implements Validator {

    protected PasswordWebService passwordService;
    protected AppConfiguration configuration;


	public void validate(Object cmd, Errors err) {

		boolean required = true;

		ResetUserPasswordCommand pswdChangeCmd =  (ResetUserPasswordCommand) cmd;

        if (!pswdChangeCmd.isAutoGeneratePassword()) {
            if (pswdChangeCmd.getPassword() == null || pswdChangeCmd.getPassword().length() == 0 ) {
                err.rejectValue("password","required");
                required = false;
            }
            if (pswdChangeCmd.getConfPassword() == null || pswdChangeCmd.getConfPassword().length() == 0 ) {
                err.rejectValue("confPassword","required");
                required = false;
            }

            if (pswdChangeCmd.getUserId() == null || pswdChangeCmd.getUserId().length() == 0 ) {
                err.rejectValue("userId","required");
                required = false;
            }
        }


		if (!required) {
			return;
		}
		if ( !pswdChangeCmd.getConfPassword().equals(pswdChangeCmd.getPassword() )) {
			err.rejectValue("confPassword","notequal");
			required = false;
			return;
		}
		// validate the password against the policy
        // validate the password against the policy
        if ("ENFORCE_POLICY".equalsIgnoreCase(configuration.getAdminPasswordReset())) {

            Password pswd = new Password();
            pswd.setDomainId(configuration.getDefaultSecurityDomain());
            pswd.setManagedSysId(configuration.getDefaultManagedSysId());
            pswd.setPrincipal(pswdChangeCmd.getPrincipal());
            pswd.setPassword(pswdChangeCmd.getPassword());


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



	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class cls) {
		return ResetUserPasswordCommand.class.equals(cls);
	}


    public void setConfiguration(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public PasswordWebService getPasswordService() {
        return passwordService;
    }

    public void setPasswordService(PasswordWebService passwordService) {
        this.passwordService = passwordService;
    }
}
	
