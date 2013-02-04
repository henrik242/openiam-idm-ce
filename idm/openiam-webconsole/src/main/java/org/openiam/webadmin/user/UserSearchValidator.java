package org.openiam.webadmin.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validation class for UserSearch
 */

public class UserSearchValidator implements Validator {

    private static final Log log = LogFactory.getLog(UserSearchValidator.class);

    public boolean supports(Class cls) {
        return UserSearchCommand.class.equals(cls);
    }

    /* (non-Javadoc)
      * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
      */
    public void validate(Object cmd, Errors err) {
        UserSearchCommand provCmd = (UserSearchCommand) cmd;


        // check if at least on search criteria has been selected.
        if (!provCmd.isSearchDefined()) {
            err.rejectValue("lastName", "required");
            return;
        }

    }


}
