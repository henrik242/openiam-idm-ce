import org.openiam.idm.srvc.auth.dto.Login
import org.openiam.idm.srvc.auth.dto.LoginId
import org.openiam.idm.srvc.role.dto.Role
import org.openiam.idm.srvc.role.dto.RoleId
import org.openiam.idm.srvc.synch.dto.Attribute
import org.openiam.idm.srvc.synch.dto.LineObject
import org.openiam.idm.srvc.synch.service.AbstractTransformScript
import org.openiam.idm.srvc.synch.service.TransformScript
import org.openiam.idm.srvc.user.dto.UserAttribute
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.provision.dto.ProvisionUser

import java.text.DateFormat
import java.text.SimpleDateFormat

public class TransformADFile1 extends AbstractTransformScript {

    static String BASE_URL = "http://localhost:9090/idmsrvc";

    static String DOMAIN = "USR_SEC_DOMAIN";
    static boolean KEEP_AD_ID = true;
    static String MANAGED_SYS_ID = "110";
    static String DEFAULT_MANAGED_SYS = "0";


    public int execute(LineObject rowObj, ProvisionUser pUser) {

        /* constants - maps to a managed sys id*/

        String defaultRole = "END_USER";

        println("Transforming new record");

        populateObject(rowObj, pUser);


        pUser.setStatus(UserStatusEnum.ACTIVE);
        pUser.securityDomain = "0";


        return TransformScript.NO_DELETE;
    }

    private void populateObject(LineObject rowObj, ProvisionUser pUser) {
        Attribute attrVal = null;
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String cn = null;

        Map<String, Attribute> columnMap = rowObj.getColumnMap();

        if (isNewUser) {
            pUser.setUserId(null);
        }


        attrVal = columnMap.get("givenName");
        if (attrVal != null && attrVal.value != null) {
            pUser.setFirstName(attrVal.getValue());
        }

        attrVal = columnMap.get("sn");
        if (attrVal != null && attrVal.value != null) {
            pUser.setLastName(attrVal.getValue());
        }
        attrVal = columnMap.get("cn");
        if (attrVal != null && attrVal.value != null) {
            addAttribute(pUser, attrVal);
            cn = attrVal.value;
        }


        if (KEEP_AD_ID) {


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
                lg2.id = new LoginId(DOMAIN, attrVal.value,  MANAGED_SYS_ID);
                principalList.add(lg2);

                pUser.principalList = principalList;


            }
        }

        // set the users role

        attrVal = columnMap.get("UserType");
        if (attrVal != null) {
            // tokenize the string
            String[] roleAry = attrVal.getValue().split("\\*");
            List<Role> roleList = new ArrayList<Role>();

            for (String strR : roleAry) {

                RoleId id = new RoleId(DOMAIN, strR);
                Role r = new Role();
                r.setId(id);

                if (!isInRole(userRoleList, id)) {

                    roleList.add(r);

                }
            }

            pUser.setMemberOfRoles(roleList);
        }


        if (pUser.status == null) {
            pUser.status = UserStatusEnum.INACTIVE;
        }


    }

    private void addAttribute(ProvisionUser user, Attribute attr) {

        if (attr != null && attr.getName() != null && attr.getName().length() > 0) {
            UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
            user.getUserAttributes().put(attr.getName(), userAttr);
        }
    }

    private boolean isInRole(List<Role> currentRoleList, RoleId id) {
        for (Role r : currentRoleList) {

            if (r.id.roleId.equalsIgnoreCase(id.roleId) && r.id.serviceId.equalsIgnoreCase(id.serviceId)) {
                return true;
            }
        }
        return false;
    }


}
