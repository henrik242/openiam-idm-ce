package org.openiam.spml2.spi.common;

import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PasswordCommand {
    public ResponseType setPassword(SetPasswordRequestType reqType);
}
