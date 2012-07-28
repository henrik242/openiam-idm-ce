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


import java.util.List;

import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.meta.ws.MetadataTypeResponse;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

/**
 * Validation class for the Managed System list.
 * @author suneet
 *
 */
public class MetadataTypeValidator implements Validator {


    MetadataWebService metadataService;

    public boolean supports(Class cls) {
		 return MetadataTypeCommand.class.equals(cls);
	}

	public void validate(Object cmd, Errors err) {

        MetadataTypeCommand typeCommand =  (MetadataTypeCommand) cmd;


        MetadataType type = typeCommand.getMetadataType();
        if (type.getMetadataTypeId() == null || type.getMetadataTypeId().isEmpty()) {
            err.rejectValue("metadataType.metadataTypeId", "required");
            return;
        }
        // check for duplicates

        MetadataTypeResponse response =  metadataService.getMetadataType(type.getMetadataTypeId());
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            MetadataType existingType =  response.getMetadataType();
            if (existingType != null) {
                err.rejectValue("metadataType.metadataTypeId", "duplicate");
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
