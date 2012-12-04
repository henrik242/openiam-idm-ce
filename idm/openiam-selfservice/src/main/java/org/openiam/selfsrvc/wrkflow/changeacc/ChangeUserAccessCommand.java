package org.openiam.selfsrvc.wrkflow.changeacc;

import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.io.Serializable;

/**
 * Command object for the ManageMyIdentityController
 *
 * @author suneet
 */
public class ChangeUserAccessCommand implements Serializable {

    private String operation;
    private String roleId;
    private String roleDomain;
    private String resourceId;
    private String groupId;

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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
