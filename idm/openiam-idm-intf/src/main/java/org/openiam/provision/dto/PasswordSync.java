/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.provision.dto;

import org.openiam.base.BaseObject;
import org.openiam.provision.type.ExtensibleAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Password object used for synchronization
 * @author suneet
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PasswordSync", propOrder = {
    "securityDomain",
    "managedSystemId",
    "principal",
    "password",
    "srcSystemId",
    "validateRequest",
    "requestorId",
    "action",
    "passThruAttributes",
    "attributeList",
    "sendPasswordToUser"
})
public class PasswordSync extends BaseObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2746720616086920826L;

	String securityDomain;
	String managedSystemId;
	String principal;
	String password;
	String srcSystemId;
	boolean validateRequest;
	String requestorId;
	String action;
    boolean sendPasswordToUser = false;

    boolean passThruAttributes = true;
    List<ExtensibleAttribute> attributeList = new ArrayList<ExtensibleAttribute>();
	
	public PasswordSync() {
		
	}
	
	public PasswordSync(String action, String managedSystemId, String password,
			String principal, String requestorId, String securityDomain,
			String srcSystemId, boolean validateRequest) {
		super();
		this.action = action;
		this.managedSystemId = managedSystemId;
		this.password = password;
		this.principal = principal;
		this.requestorId = requestorId;
		this.securityDomain = securityDomain;
		this.srcSystemId = srcSystemId;
		this.validateRequest = validateRequest;
	}

    @Override
    public String toString() {
        return "PasswordSync{" +
                "securityDomain='" + securityDomain + '\'' +
                ", managedSystemId='" + managedSystemId + '\'' +
                ", principal='" + principal + '\'' +
                ", password='" + password + '\'' +
                ", srcSystemId='" + srcSystemId + '\'' +
                ", validateRequest=" + validateRequest +
                ", requestorId='" + requestorId + '\'' +
                ", action='" + action + '\'' +
                ", sendPasswordToUser=" + sendPasswordToUser +
                ", passThruAttributes=" + passThruAttributes +
                ", attributeList=" + attributeList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordSync)) return false;

        PasswordSync that = (PasswordSync) o;

        if (passThruAttributes != that.passThruAttributes) return false;
        if (sendPasswordToUser != that.sendPasswordToUser) return false;
        if (validateRequest != that.validateRequest) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (managedSystemId != null ? !managedSystemId.equals(that.managedSystemId) : that.managedSystemId != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (principal != null ? !principal.equals(that.principal) : that.principal != null) return false;
        if (requestorId != null ? !requestorId.equals(that.requestorId) : that.requestorId != null) return false;
        if (securityDomain != null ? !securityDomain.equals(that.securityDomain) : that.securityDomain != null)
            return false;
        if (srcSystemId != null ? !srcSystemId.equals(that.srcSystemId) : that.srcSystemId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = securityDomain != null ? securityDomain.hashCode() : 0;
        result = 31 * result + (managedSystemId != null ? managedSystemId.hashCode() : 0);
        result = 31 * result + (principal != null ? principal.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (srcSystemId != null ? srcSystemId.hashCode() : 0);
        result = 31 * result + (validateRequest ? 1 : 0);
        result = 31 * result + (requestorId != null ? requestorId.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (sendPasswordToUser ? 1 : 0);
        result = 31 * result + (passThruAttributes ? 1 : 0);
        return result;
    }

    public String getSecurityDomain() {
		return securityDomain;
	}
	public void setSecurityDomain(String securityDomain) {
		this.securityDomain = securityDomain;
	}
	public String getManagedSystemId() {
		return managedSystemId;
	}
	public void setManagedSystemId(String managedSystemId) {
		this.managedSystemId = managedSystemId;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSrcSystemId() {
		return srcSystemId;
	}
	public void setSrcSystemId(String srcSystemId) {
		this.srcSystemId = srcSystemId;
	}
	public boolean isValidateRequest() {
		return validateRequest;
	}
	public void setValidateRequest(boolean validateRequest) {
		this.validateRequest = validateRequest;
	}
	public String getRequestorId() {
		return requestorId;
	}
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

    public boolean isPassThruAttributes() {
        return passThruAttributes;
    }

    public void setPassThruAttributes(boolean passThruAttributes) {
        this.passThruAttributes = passThruAttributes;
    }

    public List<ExtensibleAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<ExtensibleAttribute> attributeList) {
        this.attributeList = attributeList;
    }

    public boolean isSendPasswordToUser() {
        return sendPasswordToUser;
    }

    public void setSendPasswordToUser(boolean sendPasswordToUser) {
        this.sendPasswordToUser = sendPasswordToUser;
    }
}

