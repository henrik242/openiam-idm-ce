package org.openiam.webadmin.metadata;
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


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.openiam.idm.srvc.cat.service.CategoryDataService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * Validation class for the Managed System list.
 * @author suneet
 *
 */
public class MetadataAttributeValidator implements Validator {

    MetadataWebService metadataService;


	

	public boolean supports(Class cls) {
		 return MetadataAttributeCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {
		// TODO Auto-generated method stub
		MetadataAttributeCommand listCommand =  (MetadataAttributeCommand) cmd;
        String typeId = ((MetadataAttributeCommand) cmd).getTypeId();

        MetadataElement[] elementAry = metadataService.getMetadataElementByType(typeId).getMetadataElementAry();


        System.out.println("MetadataAttributeValidator is called");

        /**
        //Check if attribute name submitted by user is already in use
		for(int i = 0;i < elementAry.length; i++){

            if (listCommand.getElement().getAttributeName() == elementAry[i].getAttributeName()) {

                //throw validation error
                err.rejectValue("element.attributeName", "duplicate");
            }

	    }
        **/


        if (listCommand.getElement().getAttributeName() == null || listCommand.getElement().getAttributeName().length() == 0) {
                 err.rejectValue("element.attributeName", "required");
        }


        if (listCommand.getElement().getMaxLen() != null  && listCommand.getElement().getMinLen() != null) {
            if (listCommand.getElement().getMaxLen() < listCommand.getElement().getMinLen()) {
                    err.rejectValue("element.maxLen", "incorrectValue");
            }
        }





		
		
	}

    public MetadataWebService getMetadataService() {
        return metadataService;
    }

    public void setMetadataService(MetadataWebService metadataService) {
        this.metadataService = metadataService;
    }
}
