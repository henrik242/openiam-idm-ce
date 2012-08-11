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
 *   GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 *
 */
package org.openiam.idm.srvc.pswd.dto;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Object representing a password in OpenIAM
 *
 * @author suneet
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PasswordResetTokenResponse", propOrder = {
        "passwordResetToken"
})
public class PasswordResetTokenResponse extends Response {

    protected String passwordResetToken;



    public PasswordResetTokenResponse() {
        super();
    }

    public PasswordResetTokenResponse(ResponseStatus s) {
        super(s);
    }

    public PasswordResetTokenResponse(ResponseStatus s, String passwordResetToken) {
        super(s);
        this.passwordResetToken = passwordResetToken;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}
