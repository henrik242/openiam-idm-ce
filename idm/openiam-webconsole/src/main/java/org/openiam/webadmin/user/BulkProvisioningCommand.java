package org.openiam.webadmin.user;

import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Command object for the BulkProvisioningCommand
 *
 * @author suneet
 */
public class BulkProvisioningCommand implements Serializable {
    public static final String MSG_PLAIN = "PLAIN";
    public static final String MSG_HTML = "HTML";

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

    private int resultSetSize;

    private BulkMigrationConfig config;

    private List<User> users = Collections.EMPTY_LIST;
    private List<String> selectedUserIds = Collections.EMPTY_LIST;

    private List<NotificationDto> notifications = Collections.EMPTY_LIST;
    private String selectedNotificationName;

    private Date lastLoginDate; 
    
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

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDto> notifications) {
        this.notifications = notifications;
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

        if ( lastLoginDate != null ) {
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

    public int getResultSetSize() {
        return resultSetSize;
    }

    public void setResultSetSize(int resultSetSize) {
        this.resultSetSize = resultSetSize;
    }

    public BulkMigrationConfig getConfig() {

        BulkMigrationConfig config = new BulkMigrationConfig(lastName, companyId, deptId,
                division, attributeName, attributeValue,
                null,operation, targetRole, targetResource, lastLoginDate);

        if ( userStatus != null) {
            config.setUserStatus(userStatus.toString());
        }
        if (actionType != null && !actionType.isEmpty()) {
            config.setActionType(actionType);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            config.setNewPassword(newPassword);
        }

        if (role != null && !role.isEmpty()) {
            config.setRole(role);
        }

        return config;
    }

    public void setConfig(BulkMigrationConfig config) {
        this.config = config;
    }

    public String getSelectedNotificationName() {
        return selectedNotificationName;
    }

    public void setSelectedNotificationName(String selectedNotificationName) {
        this.selectedNotificationName = selectedNotificationName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getSelectedUserIds() {
        return selectedUserIds;
    }

    public void setSelectedUserIds(List<String> selectedUserIds) {
        this.selectedUserIds = selectedUserIds;
    }
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

}


