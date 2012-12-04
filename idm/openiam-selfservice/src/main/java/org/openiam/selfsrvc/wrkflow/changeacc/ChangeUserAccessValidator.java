package org.openiam.selfsrvc.wrkflow.changeacc;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ChangeUserAccessValidator implements Validator {

	


	public boolean supports(Class cls) {
		 return ChangeUserAccessCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

		ChangeUserAccessCommand identityCommand =  (ChangeUserAccessCommand) cmd;

        boolean selectedItem = false;

        // validate that the user picked something
        if (identityCommand.getGroupId() != null && !identityCommand.getGroupId().isEmpty()) {
            selectedItem = true;
        }

        if (identityCommand.getResourceId() != null && !identityCommand.getResourceId().isEmpty()) {
            selectedItem = true;
        }

        if ( identityCommand.getRoleId() != null && !identityCommand.getRoleId().isEmpty()) {
            selectedItem = true;
        }

        if (!selectedItem) {
            err.rejectValue("roleId", "required");
        }


		
	}


	
}
