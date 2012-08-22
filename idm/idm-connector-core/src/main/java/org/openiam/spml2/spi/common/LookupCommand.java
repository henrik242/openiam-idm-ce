package org.openiam.spml2.spi.common;

import org.openiam.spml2.msg.LookupRequestType;
import org.openiam.spml2.msg.LookupResponseType;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface LookupCommand {
    public LookupResponseType lookup(LookupRequestType reqType);
}
