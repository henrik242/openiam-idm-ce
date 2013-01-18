package org.openiam.selfsrvc.hire;

import org.mule.util.StringUtils;
import org.springframework.validation.Errors;

public class NewHireExtValidator {

    public static void validateSelectUserTypeForm(NewHireExtCommand cmd, Errors err) {
        if(StringUtils.isEmpty(cmd.getMetadataTypeId())) {
            err.rejectValue("metadataTypeId", "required");
        }
    }

    public static void validateSaveForm(NewHireExtCommand cmd, Errors err) {
        if (StringUtils.isEmpty(cmd.getFirstName())) {
            err.rejectValue("firstName", "required");

        }
        if (StringUtils.isEmpty(cmd.getLastName())) {
            err.rejectValue("lastName", "required");
        }

        if (StringUtils.isEmpty(cmd.getEmail1())) {
            err.rejectValue("email1", "required");
        }

        if (StringUtils.isEmpty(cmd.getCompanyId())) {
            err.rejectValue("companyId", "required");
        }
        if (StringUtils.isEmpty(cmd.getRoleId())) {
            err.rejectValue("roleId", "required");
        }

        String[] attrNames =  cmd.getMetaAttrName();
        for(int i =0; i<attrNames.length; i++) {
          if(Boolean.parseBoolean(cmd.getMetaAttrRequired()[i]) && StringUtils.isEmpty(cmd.getMetaAttrValue()[i])) {
              err.rejectValue("metaAttrValue", "required");
          }
        }

        if (StringUtils.isEmpty(cmd.getSupervisorId())) {
            err.rejectValue("supervisorId", "required");
        }
    }
}
