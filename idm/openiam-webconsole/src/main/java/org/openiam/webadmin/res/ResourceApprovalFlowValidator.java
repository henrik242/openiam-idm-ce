package org.openiam.webadmin.res;
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


import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Validation class for the Resources.
 *
 * @author suneet
 */
public class ResourceApprovalFlowValidator implements Validator {


    public boolean supports(Class cls) {
        return ResourceApprovalFlowCommand.class.equals(cls);
    }

    public void validate(Object cmd, Errors err) {


        ResourceApprovalFlowCommand flowCmd = (ResourceApprovalFlowCommand) cmd;

        List<ApproverAssociation> approverAssocList =  flowCmd.getApproverAssoc();

        if (approverAssocList == null || approverAssocList.isEmpty()) {
            return;
        }

        String primaryApproverType = null;
        int rowCntr = 1;

        for (ApproverAssociation aa : approverAssocList ) {
            String approverType = aa.getAssociationType();

            if ((approverType == null || approverType.length() == 0) && rowCntr == 1) {
                 err.rejectValue("resourceName","required");
                return;

            }

            if (primaryApproverType == null) {
                primaryApproverType = approverType;
            }
            if (approverType == null ||approverType.isEmpty()) {
                continue;
            }
            if (rowCntr > 1) {
                if (!primaryApproverType.equalsIgnoreCase(approverType)) {
                   err.rejectValue("resourceName","mismatch");
                    return;
                }
                if ("ROLE".equalsIgnoreCase(primaryApproverType) &&
                    "ROLE".equalsIgnoreCase(approverType) &&
                    rowCntr > 1 ) {
                     err.rejectValue("resourceName","toomanyroles");
                    return;
                }

            }


           rowCntr++;

        }


    }


}
  // mismatch.approvalCmd.formError=Cannot combine User and Role based approvers
//toomanyroles.approvalCmd.formError=Only one role can be defined as an approver