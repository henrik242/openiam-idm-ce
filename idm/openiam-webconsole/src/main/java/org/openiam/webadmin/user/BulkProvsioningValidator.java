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
		NewUserCommand newHireCmd =  (NewUserCommand) cmd;
		
	}

	
	public void validateNewUserType(Object cmd, Errors err) {
		// TODO Auto-generated method stub
		NewUserCommand newUserCmd =  (NewUserCommand) cmd;
		
		User user = newUserCmd.getUser();
	
		if (user.getMetadataTypeId() == null ||
			user.getMetadataTypeId().length() == 0) {
			err.rejectValue("user.metadataTypeId", "required");
			return;
		}

		
		
	}

	private List<UserAttribute> toAttributeList(MetadataElement[] elementAry) {
		List<UserAttribute> attrList = new ArrayList<UserAttribute>();
		if (elementAry == null) {
			return null;
		}
		
		for (MetadataElement elem  :elementAry) {
			UserAttribute attr = new UserAttribute();
			attr.setMetadataElementId(elem.getMetadataElementId());
			attr.setName(elem.getAttributeName());
			attrList.add(attr);
		}
		return attrList;
		
	}
	
	public void validateNewUserForm(Object cmd, Errors err) {
		// TODO Auto-generated method stub
		NewUserCommand newHireCmd =  (NewUserCommand) cmd;
		
		User user = newHireCmd.getUser();
		log.info("User from form = " + user);
		
		
		if (newHireCmd.getUser().getFirstName() == null || newHireCmd.getUser().getFirstName().length() == 0) {
			err.rejectValue("user.firstName", "required");
		}
		if (user.getLastName() == null || user.getLastName().length() == 0) {
			err.rejectValue("user.lastName", "required");
		}	
	
		
	}




}
