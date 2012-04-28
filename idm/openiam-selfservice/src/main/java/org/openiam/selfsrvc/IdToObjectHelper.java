package org.openiam.selfsrvc;

import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.selfsrvc.usradmin.DelegationFilterHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts a list of IDs to a list of their Objects.
 * User: suneetshah
 * Date: 4/27/12
 * Time: 3:10 PM
 */
public class IdToObjectHelper {
    private RoleDataWebService roleDataService;
    private GroupDataWebService groupManager;
    private OrganizationDataService orgManager;


    public List<Organization> organizationList( Map<String, UserAttribute> attrMap) {

        List<Organization> orgList = new ArrayList<Organization>() ;

        List<String> orgFilterList  = DelegationFilterHelper.getOrgIdFilterFromString(attrMap);
        if (orgFilterList != null && orgFilterList.size() > 0) {
            for (String oId : orgFilterList) {
                Organization org = orgManager.getOrganization(oId);
                if (org != null) {
                    orgList.add(org);
                }


            }
        }
        return orgList;
    }

    public List<Group> groupList( Map<String, UserAttribute> attrMap) {

        List<Group> groupList = new ArrayList<Group>() ;

        List<String> grpFilterList  =  DelegationFilterHelper.getGroupFilterFromString(attrMap);
        if (grpFilterList != null && grpFilterList.size() > 0) {
            for (String gId : grpFilterList) {
                Group grp = groupManager.getGroup(gId).getGroup();
                if (grp != null) {
                    groupList.add(grp);
                }


            }
        }
        return groupList;
    }

    public List<Role> roleList( Map<String, UserAttribute> attrMap) {

        List<Role> roleList = new ArrayList<Role>() ;

        List<String> roleFilterList  =  DelegationFilterHelper.getRoleFilterFromString(attrMap);
        if (roleFilterList != null && roleFilterList.size() > 0) {
            for (String r : roleFilterList) {
                int indx = r.indexOf("*");
                String roleId = r.substring(indx + 1, r.length());
                String domainId = r.substring(0, indx);

                Role role = roleDataService.getRole(domainId,roleId).getRole();
                if (role != null) {
                    roleList.add(role);
                }


            }
        }
        return roleList;
    }


    public RoleDataWebService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataWebService roleDataService) {
        this.roleDataService = roleDataService;
    }

    public GroupDataWebService getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }
}
