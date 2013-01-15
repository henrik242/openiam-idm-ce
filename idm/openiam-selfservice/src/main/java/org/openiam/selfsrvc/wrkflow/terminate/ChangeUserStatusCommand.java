package org.openiam.selfsrvc.wrkflow.terminate;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;

/**
 * Command object for the ManageMyIdentityController
 *
 * @author suneet
 */
public class ChangeUserStatusCommand implements Serializable {

    private User selectedUser;
    UserStatusEnum newStatus;


    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }



    public UserStatusEnum getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(UserStatusEnum newStatus) {
        this.newStatus = newStatus;
    }
}
