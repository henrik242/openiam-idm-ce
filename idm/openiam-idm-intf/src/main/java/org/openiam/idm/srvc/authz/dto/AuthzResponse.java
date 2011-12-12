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
package org.openiam.idm.srvc.authz.dto;

import org.openiam.base.ws.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;



/**
 * Response object for a web service operation that returns a role.
 * @author suneet
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthzResponse", propOrder = {
    "authorized",
    "authErrorCode",
    "authErrorMessage",
    "status",
    "resultAttributes"
})
public class AuthzResponse {

	@XmlAttribute(required = true)
    protected ResponseStatus status;
    protected boolean authorized;

    protected int authErrorCode;
	protected String authErrorMessage;
    protected  List<AuthAttribute> resultAttributes = new ArrayList<AuthAttribute>();

	public AuthzResponse() {
		super();
	}


    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }


    public int getAuthErrorCode() {
        return authErrorCode;
    }

    public void setAuthErrorCode(int authErrorCode) {
        this.authErrorCode = authErrorCode;
    }

    public String getAuthErrorMessage() {
        return authErrorMessage;
    }

    public void setAuthErrorMessage(String authErrorMessage) {
        this.authErrorMessage = authErrorMessage;
    }

    public List<AuthAttribute> getResultAttributes() {
        return resultAttributes;
    }

    public void setResultAttributes(List<AuthAttribute> resultAttributes) {
        this.resultAttributes = resultAttributes;
    }
}
