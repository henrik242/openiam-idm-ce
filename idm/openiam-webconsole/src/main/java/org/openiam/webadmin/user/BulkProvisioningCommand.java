package org.openiam.webadmin.user;

import org.openiam.idm.srvc.user.dto.UserStatusEnum;

import java.io.Serializable;


/**
 * Command object for the BulkProvisioningCommand
 *
 * @author suneet
 */
public class BulkProvisioningCommand implements Serializable {

    private String lastName;
    private String companyId;
    private String deptId;
    private String division;
    private String attributeName;
    private String attributeValue;
    private UserStatusEnum userStatus;
    /* role is used to select the users for this operation */
    private String role;

    /* ACTION TYPE - change access or bulk password reset*/
    private String actionType;

    /* New password */
    private String newPassword;


    private String operation;
    private String targetRole;
    private String targetResource;





    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyID) {
        this.companyId = companyID;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getTargetResource() {
        return targetResource;
    }

    public void setTargetResource(String targetResource) {
        this.targetResource = targetResource;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isSearchDefined() {

        if (lastName != null && !lastName.isEmpty()) {
            return true;
        }

        if (companyId != null && !companyId.isEmpty()) {
            return true;
        }

        if (deptId != null && !deptId.isEmpty()) {
            return true;
        }

        if (deptId != null && !deptId.isEmpty()) {
            return true;
        }

        if (attributeName != null && !attributeName.isEmpty()) {
            return true;
        }


        if (attributeValue != null && !attributeValue.isEmpty()) {
            return true;
        }

        if ( role != null && !role.isEmpty()) {
            return true;

        }


        if (userStatus != null) {
            return true;
        }

        return false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}


