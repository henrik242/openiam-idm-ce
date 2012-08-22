package org.openiam.spml2.spi.common;

import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AddCommand {
    public AddResponseType add(AddRequestType reqType);
}
