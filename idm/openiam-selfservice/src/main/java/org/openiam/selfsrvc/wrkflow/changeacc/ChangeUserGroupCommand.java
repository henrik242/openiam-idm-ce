package org.openiam.selfsrvc.wrkflow.changeacc;

import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.grp.dto.Group;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the ChangeUserGroupController
 *
 * @author suneet
 */
public class ChangeUserGroupCommand implements Serializable {

    private String operation;
    private String groupId;
    private User selectedUser;
    private List<Group> currentGroupMemberships;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Group> getCurrentGroupMemberships() {
        return currentGroupMemberships;
    }

    public void setCurrentGroupMemberships(List<Group> currentGroupMemberships) {
        this.currentGroupMemberships = currentGroupMemberships;
    }
}
