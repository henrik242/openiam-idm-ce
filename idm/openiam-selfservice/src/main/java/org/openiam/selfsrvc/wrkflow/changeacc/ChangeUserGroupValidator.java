package org.openiam.selfsrvc.wrkflow.changeacc;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ChangeUserGroupValidator implements Validator {

	


	public boolean supports(Class cls) {
		 return ChangeUserGroupCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

        ChangeUserGroupCommand identityCommand =  (ChangeUserGroupCommand) cmd;

        boolean selectedItem = false;

        // validate that the user picked something
        if (identityCommand.getGroupId() != null && !identityCommand.getGroupId().isEmpty()) {
            selectedItem = true;
        }

		
	}


	
}
