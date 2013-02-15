
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
import org.openiam.idm.srvc.continfo.dto.ContactConstants
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;



public class TransformCSVRecord extends AbstractTransformScript {

    static String BASE_URL= "http://localhost:8080/openiam-idm-esb/idmsrvc";

    static String DOMAIN = "USR_SEC_DOMAIN";

    public int execute(LineObject rowObj, ProvisionUser pUser) {

        /* constants - maps to a managed sys id*/
        String MANAGED_SYS_ID = "110";
        String defaultRole = "END_USER";



        println("Is New User:" + isNewUser)
        println("User Object:" + user)
        println("PrincipalList: " + principalList)
        println("User Roles:" + userRoleList)

        populateObject(rowObj, pUser);


        pUser.setStatus(UserStatusEnum.ACTIVE);
        pUser.securityDomain = "0";


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



        attrVal = columnMap.get("ORGANIZATION");
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
        attrVal = columnMap.get("DEPARTMENT");
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


        attrVal = columnMap.get("FIRST_NAME");
        if (attrVal != null) {
            pUser.setFirstName(attrVal.getValue());
        }

        attrVal = columnMap.get("LAST_NAME");
        if (attrVal != null) {
            pUser.setLastName(attrVal.getValue());
        }

        attrVal = columnMap.get("MIDDLE_INIT");
        if (attrVal != null) {
            pUser.setMiddleInit(attrVal.getValue());
        }

        attrVal = columnMap.get("TITLE");
        if (attrVal != null) {
            pUser.setTitle(attrVal.getValue());
        }



        attrVal = columnMap.get("EMAIL");
        if (attrVal != null) {
            if (attrVal != null) {
                // check if we already have a value for EMAIL1
                addEmailAddress(attrVal, pUser, user);

            }
        }



        attrVal = columnMap.get("ADDRESS1");
        if (attrVal != null) {
            pUser.address1 = attrVal.getValue();
        }

        attrVal = columnMap.get("CITY");
        if (attrVal != null) {
            pUser.city = attrVal.getValue();
        }
        attrVal = columnMap.get("POSTAL_CD");
        if (attrVal != null) {
            pUser.postalCd = attrVal.getValue();
        }
        attrVal = columnMap.get("STATE");
        if (attrVal != null) {
            pUser.state = attrVal.getValue();
        }

        attrVal = columnMap.get("GENDER");
        if (attrVal != null) {
            pUser.sex = attrVal.getValue();
        }

        attrVal = columnMap.get("PRIMARY_LANGUAGE");
        if (attrVal != null) {
            addAttribute(pUser, attrVal);
        }

        attrVal = columnMap.get("LOGINID");
        if (attrVal != null) {
            addAttribute(pUser, attrVal);

            // PRE-POPULATE THE USER LOGIN. IN SOME CASES THE COMPANY WANTS TO KEEP THE LOGIN THAT THEY HAVE
            // THIS SHOWS HOW WE CAN DO THAT

            if (isNewUser) {
                Login lg = new Login();
                lg.id = new LoginId(DOMAIN, attrVal.value, "0");

                List<Login> principalList = new ArrayList<Login>();
                principalList.add(lg);
                pUser.principalList = principalList;
            }


        }

        // set the users role

        attrVal = columnMap.get("ROLE");
        if (attrVal != null) {
            // tokenize the string
            String[]  roleAry = attrVal.getValue().split("\\*");
            List<Role> roleList = new ArrayList<Role>();

            for (String strR:  roleAry) {

                RoleId id = new RoleId(DOMAIN, strR);
                Role r = new Role();
                r.setId(id);

                if (!isInRole(userRoleList,  id)) {

                    roleList.add(r);

                }
            }

            pUser.setMemberOfRoles(roleList);
        }



        attrVal = columnMap.get("PRIMARY_PHONE");
        if (attrVal != null && attrVal.getValue() != null) {

            addPhone("DESK PHONE", attrVal, pUser, user);

        }

        attrVal = columnMap.get("FAX");
        if (attrVal != null && attrVal.getValue() != null) {

            addPhone("FAX", attrVal, pUser, user);

        }

        attrVal = columnMap.get("STATUS");
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

    private boolean isInRole(List<Role> currentRoleList, RoleId id) {
        for (Role r : currentRoleList) {

            if (r.id.roleId.equalsIgnoreCase(id.roleId) && r.id.serviceId.equalsIgnoreCase( id.serviceId)) {
                return true;
            }
        }
        return false;
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
