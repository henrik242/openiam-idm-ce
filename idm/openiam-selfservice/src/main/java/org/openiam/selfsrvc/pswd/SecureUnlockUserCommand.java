package org.openiam.selfsrvc.pswd;

import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the SecureUnlockUser
 * @author suneet
 *
 */
public class SecureUnlockUserCommand implements Serializable {


	protected String principal;


	public SecureUnlockUserCommand() {
		super();

	}

	public SecureUnlockUserCommand(String principal) {
		super();

		this.principal = principal;
	}

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
