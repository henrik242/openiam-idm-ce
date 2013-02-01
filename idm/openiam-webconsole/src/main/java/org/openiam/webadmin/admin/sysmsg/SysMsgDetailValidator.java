package org.openiam.webadmin.admin.sysmsg;

import org.mule.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SysMsgDetailValidator implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return SysMsgCommand.class.equals(aClass);
    }

    @Override
    public void validate(Object comm, Errors err) {
        SysMsgCommand command = (SysMsgCommand)comm;
        if (StringUtils.isEmpty(command.getMsg().getName())) {
            err.rejectValue("msg.name", "required");
        }
        if (command.getTemplateMethod() == 1 && StringUtils.isEmpty(command.getSelectedTemplateId())) {
            err.rejectValue("selectedTemplateId", "required");
        }
        if (command.getTemplateMethod() == 0 && (command.getProviderScriptFile() == null || command.getProviderScriptFile().isEmpty())) {
            err.rejectValue("providerScriptFile", "required");
        }
        if (command.getTemplateMethod() != 0 && command.getTemplateMethod() != 1) {
            err.rejectValue("templateMethod", "required");
        }
    }
}
