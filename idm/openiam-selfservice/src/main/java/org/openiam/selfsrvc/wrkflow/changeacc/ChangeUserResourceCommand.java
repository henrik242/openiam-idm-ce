package org.openiam.selfsrvc.wrkflow.changeacc;

import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.user.dto.User;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the ChangeUserResourceController
 *
 * @author suneet
 */
public class ChangeUserResourceCommand implements Serializable {

    private String operation;
    private String resourceId;
    private User selectedUser;


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
