/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the Lesser GNU General Public License 
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
package org.openiam.idm.srvc.user.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * UserSearch is used to capture the parameters for an ad-hoc search on the user object.
 *
 * @author suneet
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelegationFilterSearch", propOrder = {

        "role",
        "delAdmin",
        "orgFilter"
})
public class DelegationFilterSearch implements Serializable {


    protected String role = null;
    protected int delAdmin = 0;
    protected String orgFilter = null;


    public DelegationFilterSearch() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getDelAdmin() {
        return delAdmin;
    }

    public void setDelAdmin(int delAdmin) {
        this.delAdmin = delAdmin;
    }

    public String getOrgFilter() {
        return orgFilter;
    }

    public void setOrgFilter(String orgFilter) {
        this.orgFilter = orgFilter;
    }

    public boolean isEmpty() {
        if (role.isEmpty() && orgFilter == null) {
            return true;
        }
        return false;
    }

}
