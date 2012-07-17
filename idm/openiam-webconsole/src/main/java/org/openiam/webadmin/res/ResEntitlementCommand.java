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
package org.openiam.webadmin.res;

import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.res.dto.ResourcePrivilege;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Command object for Resource Entitlements
 *
 * @author suneet
 */
public class ResEntitlementCommand implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4939438179419596312L;
    /**
     *
     */
    private List<ResourcePrivilege> resourcePrivileges;
    private String resourceName;
    private String resId;
    private String managedSysId;
    private String privlegeType;

    public ResEntitlementCommand() {

    }


    public String formatDate(Date dt) {
        if (dt == null) {
            return null;
        }
        DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
        return fmt.format(dt);
    }




    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }


    public String getResId() {
        return resId;
    }


    public void setResId(String resId) {
        this.resId = resId;
    }


    public String getManagedSysId() {
        return managedSysId;
    }


    public void setManagedSysId(String managedSysId) {
        this.managedSysId = managedSysId;
    }

    public List<ResourcePrivilege> getResourcePrivileges() {
        return resourcePrivileges;
    }

    public void setResourcePrivileges(List<ResourcePrivilege> resourcePrivileges) {
        this.resourcePrivileges = resourcePrivileges;
    }

    public String getPrivlegeType() {
        return privlegeType;
    }

    public void setPrivlegeType(String privlegeType) {
        this.privlegeType = privlegeType;
    }
}
