
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
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;



public class TransformCSVUserSrcRecord extends AbstractTransformScript {
	
	static String BASE_URL= "http://localhost:8080/openiam-idm-esb/idmsrvc";
	
	public int execute(LineObject rowObj, ProvisionUser pUser) {
		
		
		System.out.println("** 2-Transform object called. **");
		
		def loginManager = loginService();
		def OrganizationDataService orgService = orgService();
		
		Attribute attrVal = null;
	
		println("Is New User:" + isNewUser)
		println("User Object:" + user)
		println("PrincipalList: " + principalList)
		println("User Roles:" + userRoleList)
		
		
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();
		
		
		populateObject(rowObj, pUser);
		
		if (isNewUser) {
			pUser.setUserId(null);
		}
		
		attrVal = columnMap.get("status");
		if (attrVal != null) {
			if (attrVal.getValue().equals("ACTIVE")) {
				pUser.status = UserStatusEnum.ACTIVE;
			}else {
				pUser.status = UserStatusEnum.INACTIVE;
			}
		}	
		
		if (pUser.status == null) {
			pUser.status = UserStatusEnum.INACTIVE;
		}
		pUser.securityDomain = "0"
		
		println("User status = " + pUser.status);
	
		// associate the user to an organization
		attrVal = columnMap.get("org");
		if (attrVal != null && attrVal.value != null) {
			String orgName = attrVal.value;
			List<Organization> orgList = orgService.search(orgName, null, null, null);
			if (orgList != null && orgList.size() > 0) {
				Organization o = orgList.get(0);
				pUser.companyId = o.orgId;
			}
		}
		
		// Set role

		attrVal = columnMap.get("role");
		if (attrVal != null) {
			// tokenize the string
			println("Role = " + attrVal.getValue());
			String[]  roleAry = attrVal.getValue().split("\\*");
			
			println("roleAry=" + roleAry);
			
			List<Role> roleList = new ArrayList<Role>();
			
			for (String strR:  roleAry) {
			
				RoleId id = new RoleId("USR_SEC_DOMAIN", strR);
				Role r = new Role();
				r.setId(id);
				
				if (!isInRole(userRoleList,  id)) {
				
					roleList.add(r);
					
					// check if its local admin
					
					if (strR.equalsIgnoreCase("LOCAL_ADMIN")) {
						UserAttribute userAttr = new UserAttribute("DLG_FLT_ORG", pUser.companyId);
						pUser.getUserAttributes().put("DLG_FLT_ORG", userAttr);
						pUser.delAdmin = 1; 
					}
				
					
				}
				println("Role=" + strR);
			}
		
			pUser.setMemberOfRoles(roleList);
		}
		
		
		
		//  set fax and phone
		List<Phone> phoneList = new ArrayList<Phone>();
		List<EmailAddress> emailList = new ArrayList<EmailAddress>();
		
		
		attrVal = columnMap.get("fax");
		if (attrVal != null && attrVal.getValue() != null) {
			String fax  = attrVal.getValue();
			println("fax=" + fax);
			if (fax.length() > 3){
			Phone faxPhone = new Phone();
			faxPhone.setAreaCd(fax.substring(0,3));
			faxPhone.setPhoneNbr(fax.substring(4));
			faxPhone.setParentType(ContactConstants.PARENT_TYPE_USER);
			faxPhone.setName("FAX");
			//phoneList.add(faxPhone);
			pUser.getPhones().add(faxPhone);
			}
			
		}	
		
		attrVal = columnMap.get("phone");
		if (attrVal != null && attrVal.getValue() != null) {
			String phone = attrVal.getValue();
			println("Phone=" + phone);
			if (phone.length() > 3) {
				Phone ph = new Phone();
				ph.setAreaCd(phone.substring(0,3));
				ph.setPhoneNbr(phone.substring(4));
				ph.setParentType(ContactConstants.PARENT_TYPE_USER);
				ph.setName("DESK PHONE");
				//phoneList.add(ph);
				pUser.getPhones().add(ph);
			}
			
		}

		attrVal = columnMap.get("email");
		if (attrVal != null) {
			String email = attrVal.getValue();
			EmailAddress email1 = new EmailAddress();
			email1.setEmailAddress(email);
			email1.setName("EMAIL1");
			email1.setParentType(ContactConstants.PARENT_TYPE_USER);
        	emailList.add(email1);
        	pUser.getEmailAddresses().add(email1);
        
		}
		

		
	
			
		return TransformScript.NO_DELETE;
	}
	
	private boolean isInRole(List<Role> currentRoleList, RoleId id) {
		for (Role r : currentRoleList) {

			if (r.id.roleId.equalsIgnoreCase(id.roleId) && r.id.serviceId.equalsIgnoreCase( id.serviceId)) {
				return true;
			}
		}
		return false;
	}	
	
	private void populateObject(LineObject rowObj, ProvisionUser pUser) {
		Attribute attrVal = null;
		DateFormat df =  new SimpleDateFormat("MM-dd-yyyy"); 
		
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();
		
		attrVal = columnMap.get("userid");
		if (attrVal != null) {
			addAttribute(pUser, attrVal);
		}

		attrVal = columnMap.get("first name");
		if (attrVal != null) {
			pUser.setFirstName(attrVal.getValue());
		}
		
		attrVal = columnMap.get("second name");
		if (attrVal != null) {
			pUser.setLastName(attrVal.getValue());
		}
		
		attrVal = columnMap.get("email");
		if (attrVal != null) {
			pUser.email = attrVal.getValue();
		}	

		attrVal = columnMap.get("title");
		if (attrVal != null) {
			pUser.title = attrVal.getValue();
		}	
		
		attrVal = columnMap.get("preferred language");
		if (attrVal != null) {
			addAttribute(pUser, attrVal);
		}	
		
		attrVal = columnMap.get("inactive timeout");
		if (attrVal != null) {
			addAttribute(pUser, attrVal);
		}
		
		// set org
		attrVal = columnMap.get("org");
		if (attrVal != null) {
			addAttribute(pUser, attrVal);
		}
		// set org
		attrVal = columnMap.get("manager");
		if (attrVal != null) {
			addAttribute(pUser, attrVal);
		}
		
		attrVal = columnMap.get("org domain name");
			if (attrVal != null) {
				addAttribute(pUser, attrVal);
		}
		
		attrVal = columnMap.get("Org Unit");
			if (attrVal != null) {
				addAttribute(pUser, attrVal);
		}

		
	}
	
	
	private void addAttribute(ProvisionUser user, Attribute attr) {
		
		if (attr != null && attr.getName() != null && attr.getName().length() > 0) {
			UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
			user.getUserAttributes().put(attr.getName(), userAttr);
			println("Attribute added to the user object.");
		}
	}
	
	/* Helper methods to access Web services */
	
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
	
	static OrganizationDataService orgService() {
		String serviceUrl = BASE_URL + "/OrganizationDataService"
		String port ="OrganizationDataWebServicePort"
		String nameSpace = "urn:idm.openiam.org/srvc/org/service"
		
		Service service = Service.create(QName.valueOf(serviceUrl))
			
		service.addPort(new QName(nameSpace,port),
				SOAPBinding.SOAP11HTTP_BINDING,	serviceUrl)
		
		return service.getPort(new QName(nameSpace,	port),
				OrganizationDataService.class);
	}
	
}
