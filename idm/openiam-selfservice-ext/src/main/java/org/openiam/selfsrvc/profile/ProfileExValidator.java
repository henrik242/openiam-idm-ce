package org.openiam.selfsrvc.profile;

import org.springframework.validation.Errors;

public class ProfileExValidator {
    public static void validate(ProfileExCommand cmd, Errors err) {
        if (cmd.getFirstName() == null || cmd.getFirstName().length() == 0) {
            err.rejectValue("firstName", "required");

        }
        if (cmd.getLastName() == null || cmd.getLastName().length() == 0) {
            err.rejectValue("lastName", "required");
        }

        if (cmd.getEmail1() == null || cmd.getEmail1().length() == 0) {
            err.rejectValue("email1", "required");
        }

    }
}
