package org.openiam.selfsrvc.wrkflow.changeacc;

import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.User;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the ManageMyIdentityController
 *
 * @author suneet
 */
public class ChangeUserRoleCommand implements Serializable {

    private String operation;
    private String roleId;
    private String roleDomain;
    private User selectedUser;
    private List<Role> currentRoleMemberships;


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleDomain() {
        return roleDomain;
    }

    public void setRoleDomain(String roleDomain) {
        this.roleDomain = roleDomain;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Role> getCurrentRoleMemberships() {
        return currentRoleMemberships;
    }

    public void setCurrentRoleMemberships(List<Role> currentRoleMemberships) {
        this.currentRoleMemberships = currentRoleMemberships;
    }
}
