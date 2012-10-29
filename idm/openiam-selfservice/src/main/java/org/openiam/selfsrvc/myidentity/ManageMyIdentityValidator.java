package org.openiam.selfsrvc.myidentity;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ManageMyIdentityValidator implements Validator {

	


	public boolean supports(Class cls) {
		 return ManageMyIdentityCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {
		System.out.println("ManageMyIdentityValidator:validate");

		ManageMyIdentityCommand identityCommand =  (ManageMyIdentityCommand) cmd;

		
		
	}


	
}
