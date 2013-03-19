import org.openiam.base.AttributeOperationEnum
import org.openiam.idm.srvc.auth.dto.Login
import org.openiam.idm.srvc.auth.dto.LoginId
import org.openiam.idm.srvc.continfo.dto.ContactConstants
import org.openiam.idm.srvc.continfo.dto.EmailAddress
import org.openiam.idm.srvc.org.dto.Organization
import org.openiam.idm.srvc.org.service.OrganizationDataService
import org.openiam.idm.srvc.role.dto.Role
import org.openiam.idm.srvc.role.dto.RoleId
import org.openiam.idm.srvc.synch.dto.Attribute
import org.openiam.idm.srvc.synch.dto.LineObject
import org.openiam.idm.srvc.synch.service.AbstractTransformScript
import org.openiam.idm.srvc.synch.service.TransformScript
import org.openiam.idm.srvc.user.dto.User
import org.openiam.idm.srvc.user.dto.UserAttribute
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.provision.dto.ProvisionUser

import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.xml.namespace.QName
import javax.xml.ws.Service
import javax.xml.ws.soap.SOAPBinding

public class TransformADCombined extends AbstractTransformScript {

    static String BASE_URL = "http://localhost:9090/idmsrvc";

    static String DOMAIN = "USR_SEC_DOMAIN";
    static boolean KEEP_AD_ID = true;
    static String MANAGED_SYS_ID = "110";
    static String DEFAULT_MANAGED_SYS = "0";

    public int execute(LineObject rowObj, ProvisionUser pUser) {


        if (isNewUser) {
            pUser.setUserId(null);
        } else {
            pUser.setUserAttributes(user.getUserAttributes());
        }

        populateObject(rowObj, pUser);


        pUser.setStatus(UserStatusEnum.ACTIVE);
        pUser.securityDomain = "0";

        if (this.userRoleList == null || this.userRoleList.isEmpty()) {
            // add a default role
            List<Role> roleList = new ArrayList<Role>();
            RoleId id = new RoleId(DOMAIN, "Active Employee");
            Role r = new Role();
            r.setId(id)
            roleList.add(r);
            pUser.setMemberOfRoles(roleList);

        }


        return TransformScript.NO_DELETE;
    }


    private void populateObject(LineObject rowObj, ProvisionUser pUser) {
        Attribute attrVal = null;
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String cn = null;

        Map<String, Attribute> columnMap = rowObj.getColumnMap();
        def OrganizationDataService orgService = orgService();




        attrVal = columnMap.get("employeeID");
        if (attrVal != null) {
            pUser.employeeId = attrVal.getValue();
        }

        attrVal = columnMap.get("givenName");
        if (attrVal != null && attrVal.value != null) {
            pUser.setFirstName(attrVal.getValue());
        }

        attrVal = columnMap.get("SN");
        if (attrVal != null && attrVal.value != null) {
            pUser.setLastName(attrVal.getValue());
        }
        attrVal = columnMap.get("cn");
        if (attrVal != null && attrVal.value != null) {
            addAttribute(pUser, attrVal);
            cn = attrVal.value;
        }

        attrVal = columnMap.get("title");
        if (attrVal != null && attrVal.value != null) {
            pUser.title = attrVal.getValue();
        }
        attrVal = columnMap.get("physicalDeliveryOfficeName");
        if (attrVal != null && attrVal.value != null) {
            addAttribute(pUser, attrVal);
        }


        attrVal = columnMap.get("userAccountcontrol");
        if (attrVal != null && attrVal.value != null) {
            if ("514".equals(attrVal.value)) {
                pUser.secondaryStatus = UserStatusEnum.DISABLED;
            } else {
                pUser.secondaryStatus = null;
            }
        }
        attrVal = columnMap.get("samAccountName");
        if (attrVal != null && attrVal.value != null) {
            addAttribute(pUser, attrVal);
        }



        attrVal = columnMap.get("company");
        if (attrVal != null && attrVal.value != null) {
            String orgName = attrVal.value;
            List<Organization> orgList = orgService.search(orgName, null, null, null);
            if (orgList != null && orgList.size() > 0) {
                Organization o = orgList.get(0);
                pUser.companyId = o.orgId;
            }
        }

        attrVal = columnMap.get("variation1");
        if (attrVal != null && attrVal.value != null) {
            // check if we already have a value for EMAIL1
            addEmailAddress(attrVal, pUser, user, "EMAIL1");

        }
        attrVal = columnMap.get("variation2");
        if (attrVal != null && attrVal.value != null) {
            // check if we already have a value for EMAIL1
            addEmailAddress(attrVal, pUser, user, "EMAIL2");

        }



        if (isNewUser) {


            List<Login> principalList = new ArrayList<Login>();

            attrVal = columnMap.get("UserPrincipalName");
            if (attrVal != null && attrVal.value != null) {

                if (attrVal != null && attrVal.value != null) {

                    println("Principal name:" + attrVal.value);

                    addAttribute(pUser, attrVal);
                }
                String[] upn = attrVal.getValue().split("\\@");

                // UserPrincipalName will be openiam identity that is used for validation

                if (upn != null) {
                    Login lg = new Login();
                    lg.id = new LoginId(DOMAIN, upn[0], DEFAULT_MANAGED_SYS);
                    principalList.add(lg);

                }

                Login lg2 = new Login();
                lg2.id = new LoginId(DOMAIN, attrVal.value, MANAGED_SYS_ID);
                principalList.add(lg2);

                pUser.principalList = principalList;
            }
        }


    }

    private void addEmailAddress(Attribute attr, ProvisionUser pUser, User origUser, String emailName) {

        // if there are existing email addresses with this user, then update them and add it to provision user
        // object to avoid duplication
        if (origUser != null) {
            Set<EmailAddress> existingEmailList = origUser.getEmailAddresses();

            for (EmailAddress e : existingEmailList) {
                if (emailName.equalsIgnoreCase(e.getName())) {
                    e.setEmailAddress(attr.value)
                    pUser.getEmailAddresses().add(e);
                    return;

                }

            }
        }
        EmailAddress email1 = new EmailAddress();
        email1.setEmailAddress(attr.value);
        email1.setName(emailName);
        email1.setParentType(ContactConstants.PARENT_TYPE_USER);
        pUser.getEmailAddresses().add(email1);


    }

    private void addAttribute(ProvisionUser u, Attribute attr) {

        if (attr != null && attr.getName() != null && attr.getName().length() > 0) {

            UserAttribute existingAttr = u.getUserAttributes().get(attr.getName());
            if (existingAttr != null) {
                existingAttr.setValue(attr.getValue());
                existingAttr.setOperation(AttributeOperationEnum.REPLACE);

            } else {
                UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
                userAttr.setOperation(AttributeOperationEnum.ADD);
                if (u.getUserAttributes() != null) {
                    u.getUserAttributes().put(userAttr.getName(),userAttr);

                }
                else {

                    Map attrMap = new HashMap<UserAttribute>();
                    attrMap.put(userAttr);
                    u.setUserAttributes(attrMap);

                }
            }
        }
    }








}
