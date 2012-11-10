package org.openiam.selfsrvc.myidentity;

import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.user.dto.UserAttribute;

import java.io.Serializable;
import java.util.List;


/**
 * Command object for the MyIdentityListController
 * @author suneet
 *
 */
public class MyIdentityListCommand implements Serializable {

    protected List<Login> principalList;
    protected String userId; // personId


    public List<Login> getPrincipalList() {
        return principalList;
    }

    public void setPrincipalList(List<Login> principalList) {
        this.principalList = principalList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
