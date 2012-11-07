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
package org.openiam.idm.srvc.org.service;

import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.domain.UserAffiliationEntity;

import java.util.List;

/**
 * DAO Interface for UserRole. Manages the relationship between user and role.
 *
 * @author Suneet Shah
 */
public interface UserAffiliationDAO {

    public abstract void add(UserAffiliationEntity transientInstance);

    public abstract void remove(UserAffiliationEntity persistentInstance);

    public abstract UserAffiliationEntity update(UserAffiliationEntity detachedInstance);

    public abstract UserAffiliationEntity findById(String id);

    public void removeUserFromOrg(String orgId, String userId);

    public void removeAllUsersInOrg(String orgId);

    /**
     * Get all the UserRole objects for this user
     *
     * @param userId
     * @return
     */
    public List<UserAffiliationEntity> findUserOrgByUser(String userId);

    /**
     * Returns a list of users in a role.
     *
     * @param userId
     * @return
     */
    List<OrganizationEntity> findOrgAffiliationsByUser(String userId);

}