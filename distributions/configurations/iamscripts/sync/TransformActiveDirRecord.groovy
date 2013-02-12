
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
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.ContactConstants
import javax.xml.ws.Service
import javax.xml.namespace.QName
import javax.xml.ws.soap.SOAPBinding;


public class TransformActiveDirRecord extends AbstractTransformScript {

    static String BASE_URL= "http://localhost:8080/openiam-idm-esb/idmsrvc";


	public int execute(LineObject rowObj, ProvisionUser pUser) {

        /* constants - maps to a managed sys id*/
        String DOMAIN = "USR_SEC_DOMAIN";
        String MANAGED_SYS_ID = "110";
        String defaultRole = "END_USER";


	
		println("Is New User:" + isNewUser)
		println("User Object:" + user)
		println("PrincipalList: " + principalList)
		println("User Roles:" + userRoleList)
		
		populateObject(rowObj, pUser);
		

		pUser.setStatus(UserStatusEnum.ACTIVE);
		pUser.securityDomain = "0"
		
	
		
		// Set default role
		List<Role> roleList = new ArrayList<Role>();
		RoleId id = new RoleId(DOMAIN, defaultRole);
		Role r = new Role();
		r.setId(id);
		roleList.add(r);
		
		pUser.setMemberOfRoles(roleList);
			
		return TransformScript.NO_DELETE;
	}
	
	private void populateObject(LineObject rowObj, ProvisionUser pUser) {
		Attribute attrVal = null;
		DateFormat df =  new SimpleDateFormat("MM-dd-yyyy"); 
		
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();
        def OrganizationDataService orgService = orgService();


        if (isNewUser) {
            pUser.setUserId(null);
        }

        attrVal = columnMap.get("sAMAccountName");
        if (attrVal != null) {
            addAttribute(pUser, attrVal);
        }

        attrVal = columnMap.get("company");
        if (attrVal != null) {
            if (attrVal != null && attrVal.value != null) {
                String orgName = attrVal.value;
                List<Organization> orgList = orgService.search(orgName, null, null, null);
                if (orgList != null && orgList.size() > 0) {
                    Organization o = orgList.get(0);
                    pUser.companyId = o.orgId;
                }
            }
        }
        attrVal = columnMap.get("department");
        if (attrVal != null) {
            if (attrVal != null && attrVal.value != null) {
                String orgName = attrVal.value;
                List<Organization> orgList = orgService.search(orgName, null, null, null);
                if (orgList != null && orgList.size() > 0) {
                    Organization o = orgList.get(0);
                    pUser.deptCd = o.orgId;
                }
            }
        }


		attrVal = columnMap.get("givenName");
		if (attrVal != null) {
			pUser.setFirstName(attrVal.getValue());
		}
		
		attrVal = columnMap.get("sn");
		if (attrVal != null) {
			pUser.setLastName(attrVal.getValue());
		}
		

		
		attrVal = columnMap.get("mail");
		if (attrVal != null) {
            if (attrVal != null) {
                // check if we already have a value for EMAIL1
                addEmailAddress(attrVal, pUser, user);

            }
		}

	
		
		attrVal = columnMap.get("street");
		if (attrVal != null) {
			pUser.address1 = attrVal.getValue();
		}			

        attrVal = columnMap.get("l");
        if (attrVal != null) {
            pUser.city = attrVal.getValue();
        }
        attrVal = columnMap.get("postalCode");
        if (attrVal != null) {
            pUser.postalCd = attrVal.getValue();
        }
        attrVal = columnMap.get("st");
        if (attrVal != null) {
            pUser.state = attrVal.getValue();
        }

        attrVal = columnMap.get("ou");
        if (attrVal != null) {
            addAttribute(pUser, attrVal);
        }


        attrVal = columnMap.get("mobile");
        if (attrVal != null && attrVal.getValue() != null) {

            addPhone("CELL PHONE", attrVal, pUser, user);


        }

        attrVal = columnMap.get("telephoneNumber");
        if (attrVal != null && attrVal.getValue() != null) {

            addPhone("DESK PHONE", attrVal, pUser, user);

        }

        attrVal = columnMap.get("facsimileTelephoneNumber");
        if (attrVal != null && attrVal.getValue() != null) {

            addPhone("FAX", attrVal, pUser, user);

        }



    }

    private void addEmailAddress(Attribute attr, ProvisionUser pUser, User origUser) {

        // if there are existing email addresses with this user, then update them and add it to provision user
        // object to avoid duplication
        if (origUser != null) {
            Set<EmailAddress> existingEmailList = origUser.getEmailAddresses();

            for (EmailAddress e :  existingEmailList) {
                if ("EMAIL1".equalsIgnoreCase(e.getName())) {
                    e.setEmailAddress(attr.value)
                    pUser.getEmailAddresses().add(e);
                    return;

                }

            }
        }
        EmailAddress email1 = new EmailAddress();
        email1.setEmailAddress(attr.value);
        email1.setName("EMAIL1");
        email1.setParentType(ContactConstants.PARENT_TYPE_USER);
        pUser.getEmailAddresses().add(email1);



    }

    private void addPhone(String name, Attribute attr, ProvisionUser pUser, User origUser) {

        String phoneString = attr.getValue();

        // if there are existing email addresses with this user, then update them and add it to provision user
        // object to avoid duplication
        if (origUser != null) {
            Set<Phone> existingPhoneList = origUser.getPhones();

            for (Phone p :  existingPhoneList) {
                if (name.equalsIgnoreCase(p.getName())) {

                    // assumes phone format is:  xxx-xxx-xxxx
                    p.setAreaCd(phoneString.substring(0,3));
                    p.setPhoneNbr(phoneString.substring(4));
                    pUser.getPhones().add(p);
                    return;

                }

            }
        }
        Phone newPhone = new Phone();
        newPhone.setAreaCd(phoneString.substring(0,3));
        newPhone.setPhoneNbr(phoneString.substring(4));
        newPhone.setParentType(ContactConstants.PARENT_TYPE_USER);
        newPhone.setName(name);
        pUser.getPhones().add(newPhone);

    }

    private void addAttribute(ProvisionUser user, Attribute attr) {
		
		if (attr != null && attr.getName() != null && attr.getName().length() > 0) {
			UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
			user.getUserAttributes().put(attr.getName(), userAttr);
		}
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
