package org.openiam.webadmin.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;



public class BulkProvsioningValidator implements Validator {

	private static final Log log = LogFactory.getLog(BulkProvsioningValidator.class);

	
	public boolean supports(Class cls) {
		 return BulkProvisioningCommand.class.equals(cls);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object cmd, Errors arg1) {
        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;
		
	}

	
	public void validateUserSelectionForm(Object cmd, Errors err) {

        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;

        // check if at least on search criteria has been selected.
        if (! provCmd.isSearchDefined()) {
            err.rejectValue("lastName", "required");
        }
	}


	
	public void validateOperation(Object cmd, Errors err) {
        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;

        if ( provCmd.getActionType() == null || provCmd.getActionType().isEmpty()) {

            err.rejectValue("actionType", "required");
            return;
        }

        if (    BulkMigrationConfig.ACTION_RESET_PASSWORD.equalsIgnoreCase(provCmd.getActionType()) &&
                (provCmd.getNewPassword() == null || provCmd.getNewPassword().isEmpty()  ) ) {

            err.rejectValue("newPassword", "required");
            return;
        }

        if (BulkMigrationConfig.ACTION_MODIFY_ACCESS.equalsIgnoreCase(provCmd.getActionType())) {

            if (    (provCmd.getTargetResource() == null || provCmd.getTargetResource().isEmpty()) &&
                    (provCmd.getTargetRole() == null || provCmd.getTargetRole().isEmpty())  ) {

                    err.rejectValue("targetResource", "required");
            }


            if (    (provCmd.getTargetResource() != null && !provCmd.getTargetResource().isEmpty()) &&
                    (provCmd.getTargetRole() != null && !provCmd.getTargetRole().isEmpty())  ) {

                    err.rejectValue("targetResource", "both");

            }
        }
		
	}




}
