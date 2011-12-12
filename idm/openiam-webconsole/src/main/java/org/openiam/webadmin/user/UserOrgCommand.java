package org.openiam.webadmin.user;

import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.role.dto.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the UserUserController
 * @author suneet
 *
 */
public class UserOrgCommand implements Serializable {
	 


	protected List<Organization> orgList;
	protected String perId; // personId
	
	public String getPerId() {
		return perId;
	}
	public void setPerId(String perId) {
		this.perId = perId;
	}


    public List<Organization> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Organization> orgList) {
        this.orgList = orgList;
    }
}
