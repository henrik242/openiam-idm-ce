package org.openiam.idm.srvc.synch.dto;

import java.util.Date;

/**
 * BulkMigrationConfig defines the parameters that are to be used for migrating user to and from resources and roles
 * User: suneetshah
 * Date: 6/21/12
 * Time: 2:38 PM

 */
public class BulkMigrationConfig {

    public static final String ACTION_RESET_PASSWORD = "RESET_PASSWORD";
    public static final String ACTION_MODIFY_ACCESS = "MODIFY_ACCESS";
    public static final String ACTION_SEND_EMAIL = "SEND_EMAIL";

    private String lastName;
    private String organizationId;
    private String deptId;
    private String division;
    private String attributeName;
    private String attributeValue;
    private String userStatus;

    private String operation;
    private String targetRole;
    private String targetResource;
    private Integer maxResultSize;
    /* role is used to select the users for this operation */
    private String role;

    /* ACTION TYPE - change access or bulk password reset*/
    private String actionType;

    /* New password */
    private String newPassword;

    private String requestorLogin;

    private Date lastLoginDate;
    
    public BulkMigrationConfig() {

    }

    public BulkMigrationConfig(String lastName, String organizationId, String deptId,
                               String division, String attributeName, String attributeValue,
                               String userStatus, String operation, String targetRole, String targetResource, Date lastLoginDate) {
        this.lastName = lastName;
        this.organizationId = organizationId;
        this.deptId = deptId;
        this.division = division;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.userStatus = userStatus;
        this.operation = operation;
        this.targetRole = targetRole;
        this.targetResource = targetResource;
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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

    public String getRequestorLogin() {
        return requestorLogin;
    }

    public void setRequestorLogin(String requestorLogin) {
        this.requestorLogin = requestorLogin;
    }

    public Integer getMaxResultSize() {
        return maxResultSize;
    }

    public void setMaxResultSize(Integer maxResultSize) {
        this.maxResultSize = maxResultSize;
    }

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
}
