package org.openiam.idm.srvc.auth.dto;
// Generated Feb 18, 2008 3:56:06 PM by Hibernate Tools 3.2.0.b11


import org.openiam.base.AttributeOperationEnum;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Login domain object
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Login", propOrder = {
        "id",
        "userId",
        "password",
        "pwdEquivalentToken",
        "pwdChanged",
        "pwdExp",
        "firstTimeLogin",
        "resetPassword",
        "isLocked",
        "status",
        "gracePeriod",
        "createDate",
        "createdBy",
        "currentLoginHost",
        "authFailCount",
        "lastAuthAttempt",
        "canonicalName",
        "lastLogin",
        "isDefault",
        "selected",
        "loginAttributes",
        "passwordChangeCount",
        "operation",
        "origPrincipalName",
        "managedSysName",
        "lastLoginIP",
        "prevLoginIP",
        "prevLogin",
        "pswdResetToken",
        "pswdResetTokenExp"
})
@XmlSeeAlso({
        Subject.class,
        SSOToken.class
})
public class Login implements java.io.Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -1972779170001619759L;

    protected AttributeOperationEnum operation;

    protected LoginId id;
    protected String userId;
    protected String password;
    protected String pwdEquivalentToken;
    @XmlSchemaType(name = "dateTime")
    protected Date pwdChanged;
    @XmlSchemaType(name = "dateTime")
    protected Date pwdExp;
    protected int firstTimeLogin;
    protected int resetPassword;
    protected int isLocked;
    protected String status;
    @XmlSchemaType(name = "dateTime")
    protected Date gracePeriod;
    @XmlSchemaType(name = "dateTime")
    protected Date createDate;
    protected String createdBy;
    protected String currentLoginHost;
    protected Integer authFailCount = new Integer(0);
    @XmlSchemaType(name = "dateTime")
    protected Date lastAuthAttempt;
    protected String canonicalName;
    @XmlSchemaType(name = "dateTime")
    protected Date lastLogin;
    protected Integer isDefault = new Integer(0);
    protected Integer passwordChangeCount = new Integer(0);
    protected boolean selected;
    protected Set<LoginAttribute> loginAttributes = new HashSet<LoginAttribute>(0);
    protected String origPrincipalName;
    protected String managedSysName;

    protected String lastLoginIP;
    @XmlSchemaType(name = "dateTime")
    protected Date prevLogin;
    protected String prevLoginIP;

    protected String pswdResetToken;
    @XmlSchemaType(name = "dateTime")
    protected Date pswdResetTokenExp;


    public Login() {
    }


    public Login(LoginId id, int resetPwd, int isLocked) {
        this.id = id;
        this.firstTimeLogin = resetPwd;
        this.isLocked = isLocked;
    }

    public Login(LoginId id, String userId, String password, String pwdEquivalentToken, Date pwdChanged, Date pwdExp, int resetPwd, int isLocked, String status, Date gracePeriod, Date createDate, String createdBy, String currentLoginHost, Integer authFailCount, Date lastAuthAttempt, Set<LoginAttribute> loginAttributes) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.pwdEquivalentToken = pwdEquivalentToken;
        this.pwdChanged = pwdChanged;
        this.pwdExp = pwdExp;
        this.firstTimeLogin = resetPwd;
        this.isLocked = isLocked;
        this.status = status;
        this.gracePeriod = gracePeriod;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.currentLoginHost = currentLoginHost;
        this.authFailCount = authFailCount;
        this.lastAuthAttempt = lastAuthAttempt;
        this.loginAttributes = loginAttributes;

    }

    @Override
    public Object clone() {
        Login l = new Login();
        LoginId lgId = new LoginId(id.getDomainId(), id.getLogin(), id.getManagedSysId());
        l.setId(lgId);

        l.setAuthFailCount(authFailCount);
        l.setCanonicalName(canonicalName);
        l.setCreateDate(createDate);
        l.setCreatedBy(createdBy);
        l.setCurrentLoginHost(currentLoginHost);
        l.setFirstTimeLogin(firstTimeLogin);
        l.setGracePeriod(gracePeriod);
        l.setIsDefault(isDefault);
        l.setLastAuthAttempt(lastAuthAttempt);
        l.setLastLogin(lastLogin);
        l.setLoginAttributes(loginAttributes);
        l.setOperation(operation);
        l.setPassword(password);
        l.setPasswordChangeCount(passwordChangeCount);
        l.setPwdChanged(pwdChanged);
        l.setPwdExp(pwdExp);
        l.setResetPassword(resetPassword);
        l.setSelected(selected);
        l.setStatus(status);
        l.setUserId(userId);
        return l;

    }


    public LoginId getId() {
        return this.id;
    }

    public void setId(LoginId id) {
        this.id = id;
    }


    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdEquivalentToken() {
        return this.pwdEquivalentToken;
    }

    public void setPwdEquivalentToken(String pwdEquivalentToken) {
        this.pwdEquivalentToken = pwdEquivalentToken;
    }

    public Date getPwdChanged() {
        return this.pwdChanged;
    }

    public void setPwdChanged(Date pwdChanged) {
        this.pwdChanged = pwdChanged;
    }

    public Date getPwdExp() {
        return this.pwdExp;
    }

    public void setPwdExp(Date pwdExp) {
        this.pwdExp = pwdExp;
    }

    public int getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGracePeriod() {

        return this.gracePeriod;
    }

    public void setGracePeriod(Date gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCurrentLoginHost() {
        return this.currentLoginHost;
    }

    public void setCurrentLoginHost(String currentLoginHost) {
        this.currentLoginHost = currentLoginHost;
    }

    public Integer getAuthFailCount() {
        return this.authFailCount;
    }

    public void setAuthFailCount(Integer authFailCount) {
        this.authFailCount = authFailCount;
    }

    public Date getLastAuthAttempt() {
        return this.lastAuthAttempt;
    }

    public void setLastAuthAttempt(Date lastAuthAttempt) {
        this.lastAuthAttempt = lastAuthAttempt;
    }

    public Set<LoginAttribute> getLoginAttributes() {
        return this.loginAttributes;
    }

    public void setLoginAttributes(Set<LoginAttribute> loginAttributes) {
        this.loginAttributes = loginAttributes;
    }


    public Date getLastLogin() {
        return lastLogin;
    }


    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }


    public Integer getIsDefault() {
        return isDefault;
    }


    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }


    public String getCanonicalName() {
        return canonicalName;
    }


    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;

        Login login = (Login) o;

        if (firstTimeLogin != login.firstTimeLogin) return false;
        if (isLocked != login.isLocked) return false;
        if (resetPassword != login.resetPassword) return false;
        if (selected != login.selected) return false;
        if (authFailCount != null ? !authFailCount.equals(login.authFailCount) : login.authFailCount != null)
            return false;
        if (canonicalName != null ? !canonicalName.equals(login.canonicalName) : login.canonicalName != null)
            return false;
        if (createDate != null ? !createDate.equals(login.createDate) : login.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(login.createdBy) : login.createdBy != null) return false;
        if (currentLoginHost != null ? !currentLoginHost.equals(login.currentLoginHost) : login.currentLoginHost != null)
            return false;
        if (gracePeriod != null ? !gracePeriod.equals(login.gracePeriod) : login.gracePeriod != null) return false;
        if (id != null ? !id.equals(login.id) : login.id != null) return false;
        if (isDefault != null ? !isDefault.equals(login.isDefault) : login.isDefault != null) return false;
        if (lastAuthAttempt != null ? !lastAuthAttempt.equals(login.lastAuthAttempt) : login.lastAuthAttempt != null)
            return false;
        if (lastLogin != null ? !lastLogin.equals(login.lastLogin) : login.lastLogin != null) return false;
        if (lastLoginIP != null ? !lastLoginIP.equals(login.lastLoginIP) : login.lastLoginIP != null) return false;
        if (loginAttributes != null ? !loginAttributes.equals(login.loginAttributes) : login.loginAttributes != null)
            return false;
        if (managedSysName != null ? !managedSysName.equals(login.managedSysName) : login.managedSysName != null)
            return false;
        if (operation != login.operation) return false;
        if (origPrincipalName != null ? !origPrincipalName.equals(login.origPrincipalName) : login.origPrincipalName != null)
            return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;
        if (passwordChangeCount != null ? !passwordChangeCount.equals(login.passwordChangeCount) : login.passwordChangeCount != null)
            return false;
        if (prevLogin != null ? !prevLogin.equals(login.prevLogin) : login.prevLogin != null) return false;
        if (prevLoginIP != null ? !prevLoginIP.equals(login.prevLoginIP) : login.prevLoginIP != null) return false;
        if (pswdResetToken != null ? !pswdResetToken.equals(login.pswdResetToken) : login.pswdResetToken != null)
            return false;
        if (pswdResetTokenExp != null ? !pswdResetTokenExp.equals(login.pswdResetTokenExp) : login.pswdResetTokenExp != null)
            return false;
        if (pwdChanged != null ? !pwdChanged.equals(login.pwdChanged) : login.pwdChanged != null) return false;
        if (pwdEquivalentToken != null ? !pwdEquivalentToken.equals(login.pwdEquivalentToken) : login.pwdEquivalentToken != null)
            return false;
        if (pwdExp != null ? !pwdExp.equals(login.pwdExp) : login.pwdExp != null) return false;
        if (status != null ? !status.equals(login.status) : login.status != null) return false;
        if (userId != null ? !userId.equals(login.userId) : login.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Login{" +
                "operation=" + operation +
                ", id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", pwdEquivalentToken='" + pwdEquivalentToken + '\'' +
                ", pwdChanged=" + pwdChanged +
                ", pwdExp=" + pwdExp +
                ", firstTimeLogin=" + firstTimeLogin +
                ", resetPassword=" + resetPassword +
                ", isLocked=" + isLocked +
                ", status='" + status + '\'' +
                ", gracePeriod=" + gracePeriod +
                ", createDate=" + createDate +
                ", createdBy='" + createdBy + '\'' +
                ", currentLoginHost='" + currentLoginHost + '\'' +
                ", authFailCount=" + authFailCount +
                ", lastAuthAttempt=" + lastAuthAttempt +
                ", canonicalName='" + canonicalName + '\'' +
                ", lastLogin=" + lastLogin +
                ", isDefault=" + isDefault +
                ", passwordChangeCount=" + passwordChangeCount +
                ", selected=" + selected +
                ", loginAttributes=" + loginAttributes +
                ", origPrincipalName='" + origPrincipalName + '\'' +
                ", managedSysName='" + managedSysName + '\'' +
                ", lastLoginIP='" + lastLoginIP + '\'' +
                ", prevLogin=" + prevLogin +
                ", prevLoginIP='" + prevLoginIP + '\'' +
                '}';
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getFirstTimeLogin() {
        return firstTimeLogin;
    }


    public void setFirstTimeLogin(int firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }


    public AttributeOperationEnum getOperation() {
        return operation;
    }


    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }


    /**
     * Tracks how many times the password has been changed.
     *
     * @return
     */
    public Integer getPasswordChangeCount() {
        return passwordChangeCount;
    }


    public void setPasswordChangeCount(Integer passwordChangeCount) {
        this.passwordChangeCount = passwordChangeCount;
    }

    /**
     * Indicates that the password has been reset
     *
     * @return
     */

    public int getResetPassword() {
        return resetPassword;
    }


    public void setResetPassword(int resetPassword) {
        this.resetPassword = resetPassword;
    }


    public String getOrigPrincipalName() {
        return origPrincipalName;
    }


    public void setOrigPrincipalName(String origPrincipalName) {
        this.origPrincipalName = origPrincipalName;
    }


    public String getManagedSysName() {
        return managedSysName;
    }


    public void setManagedSysName(String managedSysName) {
        this.managedSysName = managedSysName;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Date getPrevLogin() {
        return prevLogin;
    }

    public void setPrevLogin(Date prevLogin) {
        this.prevLogin = prevLogin;
    }

    public String getPrevLoginIP() {
        return prevLoginIP;
    }

    public void setPrevLoginIP(String prevLoginIP) {
        this.prevLoginIP = prevLoginIP;
    }

    public Date getPswdResetTokenExp() {
        return pswdResetTokenExp;
    }

    public void setPswdResetTokenExp(Date pswdResetTokenExp) {
        this.pswdResetTokenExp = pswdResetTokenExp;
    }

    public String getPswdResetToken() {
        return pswdResetToken;
    }

    public void setPswdResetToken(String pswdResetToken) {
        this.pswdResetToken = pswdResetToken;
    }
}

