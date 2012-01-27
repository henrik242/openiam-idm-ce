package org.openiam.spml2.spi.ldap.dirtype;

import org.openiam.spml2.msg.password.SetPasswordRequestType;

import javax.naming.directory.ModificationItem;
import java.io.UnsupportedEncodingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;


/**
 * Created by IntelliJ IDEA.
 * User: suneetshah
 * Date: 1/26/12
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LdapV3 implements Directory{
    public ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException {

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", reqType.getPassword()));

        return mods;
    }
}
