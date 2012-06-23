package org.openiam.idm.srvc.synch.dto;

import org.openiam.idm.srvc.user.dto.UserStatusEnum;

/**
 * BulkMigrationConfig defines the parameters that are to be used for migrating user to and from resources and roles
 * User: suneetshah
 * Date: 6/21/12
 * Time: 2:38 PM

 */
public class BulkMigrationConfig {

    String lastName;
    String organizationId;
    String deptId;
    String division;
    String attributeName;
    String attributeValue;
    String userStatus;

    String operation;
    String targetRole;
    String targetResource;

    public BulkMigrationConfig() {

    }

    public BulkMigrationConfig(String lastName, String organizationId, String deptId,
                               String division, String attributeName, String attributeValue,
                               String userStatus, String operation, String targetRole, String targetResource) {
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
}
