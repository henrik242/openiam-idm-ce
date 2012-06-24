package org.openiam.webadmin.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;


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


        if (    (provCmd.getTargetResource() == null || provCmd.getTargetResource().isEmpty()) &&
                (provCmd.getTargetRole() == null || provCmd.getTargetRole().isEmpty())  ) {

            err.rejectValue("targetResource", "required");
            return;

        }


        if (    (provCmd.getTargetResource() != null && !provCmd.getTargetResource().isEmpty()) &&
                (provCmd.getTargetRole() != null && !provCmd.getTargetRole().isEmpty())  ) {

            err.rejectValue("targetResource", "both");

        }
		
	}




}
