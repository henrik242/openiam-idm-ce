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
package org.openiam.idm.srvc.pswd.rule;

import org.openiam.exception.ObjectNotFoundException;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.pswd.dto.Password;
import org.openiam.idm.srvc.pswd.dto.PasswordValidationCode;
import org.openiam.idm.srvc.user.dto.User;

import java.io.IOException;

/**
 * Interface for the PasswordValidator
 *
 * @author suneet
 */
public interface PasswordValidator {

    /**
     * Validates the password against the password policy that is passed in.
     *
     * @param password
     * @return
     */
    PasswordValidationCode validate(Policy policy, Password password) throws ObjectNotFoundException, IOException;

    /**
     * Validates the password against the password policy that is passed in for the given user and login
     *
     * @param password
     * @return
     */
    PasswordValidationCode validateForUser(Policy policy, Password password, User usr, Login lg) throws ObjectNotFoundException, IOException;
}