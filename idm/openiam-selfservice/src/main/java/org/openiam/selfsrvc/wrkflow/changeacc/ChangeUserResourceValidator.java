package org.openiam.selfsrvc.wrkflow.changeacc;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ChangeUserResourceValidator implements Validator {

	


	public boolean supports(Class cls) {
		 return ChangeUserResourceCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

		ChangeUserResourceCommand identityCommand =  (ChangeUserResourceCommand) cmd;

        boolean selectedItem = false;


        if ( identityCommand != null && !identityCommand.getResourceId().isEmpty()) {
            selectedItem = true;
        }

        if (!selectedItem) {
            err.rejectValue("resourceId", "required");
        }


		
	}


	
}
