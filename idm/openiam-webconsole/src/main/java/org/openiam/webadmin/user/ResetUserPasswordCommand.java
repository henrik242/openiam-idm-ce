package org.openiam.webadmin.user;

import java.io.Serializable;

/**
 * Command object for the ResetUserPasswordController
 * @author suneet
 *
 */
public class ResetUserPasswordCommand implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -934251870038074577L;
	protected String userId;
	protected String userStatus;
	protected String firstName;
	protected String lastName;
	protected String password;
	protected String confPassword;
	protected String principal;
	protected String domainId;
    protected boolean notifyUserViaEmail;
    protected boolean autoGeneratePassword;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfPassword() {
		return confPassword;
	}
	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}


    public boolean isNotifyUserViaEmail() {
        return notifyUserViaEmail;
    }

    public void setNotifyUserViaEmail(boolean notifyUserViaEmail) {
        this.notifyUserViaEmail = notifyUserViaEmail;
    }

    public boolean isAutoGeneratePassword() {
        return autoGeneratePassword;
    }

    public void setAutoGeneratePassword(boolean autoGeneratePassword) {
        this.autoGeneratePassword = autoGeneratePassword;
    }
}
