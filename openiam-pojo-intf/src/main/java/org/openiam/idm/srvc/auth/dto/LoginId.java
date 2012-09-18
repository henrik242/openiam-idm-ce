package org.openiam.idm.srvc.auth.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
// Generated Feb 18, 2008 3:56:06 PM by Hibernate Tools 3.2.0.b11


/**
 * PrimaryKey for a Login object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginId", propOrder = {
        "domainId",
        "login",
        "managedSysId"
})
public class LoginId implements java.io.Serializable {


    private String domainId;
    private String login;
    private String managedSysId;


    public String toString() {
        String str = "serviceId=" + domainId +
                "  login=" + login +
                "  managedSysId=" + managedSysId;
        return str;
    }

    public LoginId() {
    }

    public LoginId(String domainId, String login, String managedSysId) {
        this.domainId = domainId;
        this.login = login;
        this.managedSysId = managedSysId;
    }

    public String getDomainId() {
        return this.domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getManagedSysId() {
        return managedSysId;
    }

    public void setManagedSysId(String managedSysId) {
        this.managedSysId = managedSysId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginId)) return false;

        LoginId loginId = (LoginId) o;

        if (domainId != null ? !domainId.equals(loginId.domainId) : loginId.domainId != null) return false;
        if (login != null ? !login.equals(loginId.login) : loginId.login != null) return false;
        if (managedSysId != null ? !managedSysId.equals(loginId.managedSysId) : loginId.managedSysId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = domainId != null ? domainId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (managedSysId != null ? managedSysId.hashCode() : 0);
        return result;
    }
}


