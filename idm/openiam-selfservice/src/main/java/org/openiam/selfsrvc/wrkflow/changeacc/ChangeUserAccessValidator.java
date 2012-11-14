package org.openiam.selfsrvc.wrkflow.changeacc;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ChangeUserAccessValidator implements Validator {

	


	public boolean supports(Class cls) {
		 return ChangeUserAccessCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {
		System.out.println("ManageMyIdentityValidator:validate");

		ChangeUserAccessCommand identityCommand =  (ChangeUserAccessCommand) cmd;

		
		
	}


	
}
