
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.service.AbstractTransformScript;
import org.openiam.idm.srvc.synch.service.TransformScript;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.continfo.dto.*;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;


public class TransformSrcRecord extends AbstractTransformScript {
	
	static String BASE_URL= "http://localhost:8080/openiam-idm-esb/idmsrvc";
	
	public int execute(LineObject rowObj, ProvisionUser pUser) {
		
		
		System.out.println("** 2-Transform object called. **");
		
		def loginManager = loginService();
		
	
		println("Is New User:" + isNewUser)
		println("User Object:" + user)
		println("PrincipalList: " + principalList)
		println("User Roles:" + userRoleList)
		
		populateObject(rowObj, pUser);
		
		if (isNewUser) {
			pUser.setUserId(null);
		}
		pUser.setStatus(UserStatusEnum.ACTIVE);
		pUser.securityDomain = "0"
		
	
		
		// Set role based on a job code
		
		List<Role> roleList = new ArrayList<Role>();
		if (pUser.jobCode == "100") {
				RoleId id = new RoleId("USR_SEC_DOMAIN", "MANAGER");
				Role r = new Role();
				r.setId(id);
				roleList.add(r);
		}else {
		
			RoleId id = new RoleId("USR_SEC_DOMAIN", "END_USER");
			Role r = new Role();
			r.setId(id);
			roleList.add(r);
		}		
		pUser.setMemberOfRoles(roleList);
		
		/* Set Email and Address objects */
		
		manageEmail(rowObj,  pUser) 
		
		
		/* Notify the user that an account has been created for them. */
		
		if (isNewUser) {
			pUser.emailCredentialsToNewUsers = true;
			
		}
		
		
		// update the identity object if the user exists
		/*if (!isNewUser) {
			 if ( principalList != null && !principalList.isEmpty()) {
			 	println("Updating identity");
			 	// get the 
			 	Login myIDM = principalList.first();
			 	myIDM.isLocked = 99;
			 			 	
			 	myIDM.operation = AttributeOperationEnum.REPLACE;
			 	
			 	if (pUser.principalList == null) {
			 		pUser.principalList = principalList;
			 	}
			 }else {
			 		println("No identities found...");
			 }
		*/
			
		
			
		return TransformScript.NO_DELETE;
	}
	
	private void manageEmail(LineObject rowObj, ProvisionUser pUser) {
		 Attribute attrVal = null;
		 Set<EmailAddress> currentEmlSet = user.emailAddresses;
		 Map<String,Attribute> columnMap =  rowObj.getColumnMap();
		 
		 attrVal = columnMap.get("EMAIL");
		 if (attrVal != null) {
		 	def emailStr = attrVal.getValue();
		 	if (currentEmlSet != null) {
		 		boolean found = false;
		 		for (EmailAddress eml :currentEmlSet) {
		 			if (eml.name == "EMAIL1") {
		 				eml.emailAddress = emailStr;
		 				found = true;
		 			}
		 			pUser.emailAddress.add(eml);
		 		}
		 		if (!found) {
		 			def email1 = new EmailAddress();
   					email1.setEmailAddress(emailStr);
   					email1.setName("EMAIL1");
   					email1.setParentType(ContactConstants.PARENT_TYPE_USER);
   					pUser.emailAddresses.add(email1)
		 		}
		 		
		 	}else {
		 		def email1 = new EmailAddress();
   				email1.setEmailAddress(emailStr);
   				email1.setName("EMAIL1");
   				email1.setParentType(ContactConstants.PARENT_TYPE_USER);
   				pUser.emailAddresses.add(email1)
		 	}
		 }
		
		
	
	}
	
	private void populateObject(LineObject rowObj, ProvisionUser pUser) {
		Attribute attrVal = null;
		DateFormat df =  new SimpleDateFormat("MM-dd-yyyy"); 
		
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();

		attrVal = columnMap.get("FIRST_NAME");
		if (attrVal != null) {
			pUser.setFirstName(attrVal.getValue());
		}
		
		attrVal = columnMap.get("LAST_NAME");
		if (attrVal != null) {
			pUser.setLastName(attrVal.getValue());
		}
		
		attrVal = columnMap.get("EMPL_ID");
		if (attrVal != null) {
			pUser.employeeId = attrVal.getValue();
		}	
		
		attrVal = columnMap.get("JOB_CODE");
		if (attrVal != null) {
			pUser.jobCode = attrVal.getValue();
		}
				
	}
	
	
	private void addAttribute(ProvisionUser user, Attribute attr) {
		
		if (attr != null && attr.getName() != null && attr.getName().length() > 0) {
			UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
			user.getUserAttributes().put(attr.getName(), userAttr);
			println("Attribute added to the user object.");
		}
	}
	
	
	static LoginDataWebService loginService() {
		String serviceUrl = BASE_URL + "/LoginDataWebService"
		String port ="LoginDataWebServicePort"
		String nameSpace = "urn:idm.openiam.org/srvc/auth/service"
		
		Service service = Service.create(QName.valueOf(serviceUrl))
			
		service.addPort(new QName(nameSpace,port),
				SOAPBinding.SOAP11HTTP_BINDING,	serviceUrl)
		
		return service.getPort(new QName(nameSpace,	port),
				LoginDataWebService.class);
	}
	
}
