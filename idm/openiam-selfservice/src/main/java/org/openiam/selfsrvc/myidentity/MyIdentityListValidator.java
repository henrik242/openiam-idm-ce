package org.openiam.selfsrvc.myidentity;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class MyIdentityListValidator implements Validator {


	public boolean supports(Class cls) {
		 return MyIdentityListCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

		
		MyIdentityListCommand attrCmd =  (MyIdentityListCommand) cmd;

			
		
		
	}



	
}
