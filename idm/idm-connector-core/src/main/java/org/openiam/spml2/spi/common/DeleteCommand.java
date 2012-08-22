package org.openiam.spml2.spi.common;

import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.ResponseType;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DeleteCommand {
    public ResponseType delete(final DeleteRequestType reqType);
}
