package org.openiam.webadmin.role;
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


import java.io.Serializable;
import java.util.List;

import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.openiam.idm.srvc.secdomain.dto.SecurityDomain;

/**
 * Command object for the RoleDetailController 
 * @author suneet
 *
 */
public class RoleDetailCommand implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8342679897533185206L;
	private Role role = new Role();
	private MetadataType[] typeList;
	private String formMode;
	private String resApproverId;
	private String resApproverName;
    private String btn;

    private List<RoleAttribute> attributeList;
	
	public MetadataType[] getTypeList() {
		return typeList;
	}
	public void setTypeList(MetadataType[] typeList) {
		this.typeList = typeList;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

    public String getFormMode() {
        return formMode;
    }

    public void setFormMode(String formMode) {
        this.formMode = formMode;
    }

    public String getResApproverId() {
		return resApproverId;
	}
	public void setResApproverId(String resApproverId) {
		this.resApproverId = resApproverId;
	}
	public String getResApproverName() {
		return resApproverName;
	}
	public void setResApproverName(String resApproverName) {
		this.resApproverName = resApproverName;
	}

    public List<RoleAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<RoleAttribute> attributeList) {
        this.attributeList = attributeList;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }
}
