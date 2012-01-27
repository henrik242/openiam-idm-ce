package org.openiam.spml2.spi.ldap.dirtype;

import org.openiam.spml2.msg.password.SetPasswordRequestType;

import javax.naming.directory.ModificationItem;
import java.io.UnsupportedEncodingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;

/**
 * Provides Active Directory specific functionality
 */
public class ActiveDirectoryImpl implements Directory {

    public ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException {


        byte[] password = ("\"" + reqType.getPassword() + "\"").getBytes("UTF-16LE");

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", password));
        return mods;


    }
}
