package org.openiam.selfsrvc.reg;

import org.springframework.validation.Errors;

public class RegistrationValidator {
    public static void validate(RegistrationCommand cmd, Errors err) {
        if (cmd.getFirstName() == null || cmd.getFirstName().length() == 0) {
            err.rejectValue("firstName", "required");

        }
        if (cmd.getLastName() == null || cmd.getLastName().length() == 0) {
            err.rejectValue("lastName", "required");
        }


        if (cmd.getRoleId() == null || cmd.getRoleId().equalsIgnoreCase("-") ) {
            err.rejectValue("roleId", "required");
        }

        if (cmd.getCompanyId() == null || cmd.getCompanyId().length() == 0) {
            err.rejectValue("companyId", "required");
        }

        if (cmd.getEmail1() == null || cmd.getEmail1().length() == 0) {
            err.rejectValue("email1", "required");
        }
    }
}
