package org.openiam.spml2.spi.ldap.dirtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.pswd.service.PasswordGenerator;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;

import javax.naming.NamingException;
import javax.naming.directory.ModificationItem;
import java.io.UnsupportedEncodingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

/**
 * Provides Active Directory specific functionality
 */
public class ActiveDirectoryImpl implements Directory {

    public static final int UF_ACCOUNTDISABLE     = 2;//0x0002
    public static final int UF_LOCKOUT            = 16;//0x0010
    public static final int UF_PASSWORD_EXPIRED   = 8388608;//0x800000
    public static final int UF_DONT_EXPIRE_PASSWD = 65536;//0x00010000
    public static final int UF_NORMAL_ACCOUNT     = 512;//0x0200
    public static final int UF_PASSWD_NOTREQD = 0x0020;
    public static final int UF_PASSWD_CANT_CHANGE = 0x0040;

    protected static final Log log = LogFactory.getLog(ActiveDirectoryImpl.class);


    public ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException {


        byte[] password = ("\"" + reqType.getPassword() + "\"").getBytes("UTF-16LE");

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", password));
        return mods;


    }

    public ModificationItem[] suspend(SuspendRequestType request) {

        log.debug("suspending AD user.");

        ModificationItem[] mods = new ModificationItem[1];

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                Integer.toString(UF_NORMAL_ACCOUNT +  UF_ACCOUNTDISABLE)));
        return mods;

    }

    public ModificationItem[] resume(ResumeRequestType request) {

        log.debug("Enabling AD user.");

        ModificationItem[] mods = new ModificationItem[1];

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                Integer.toString(UF_NORMAL_ACCOUNT )));
        return mods;

    }

    public void delete(DeleteRequestType reqType, LdapContext ldapctx, String ldapName, String onDelete) throws NamingException {

        if ("DELETE".equalsIgnoreCase(onDelete)) {

            ldapctx.destroySubcontext(ldapName);

        }else if ( "DISABLE".equalsIgnoreCase(onDelete)) {

            ModificationItem[] mods = new ModificationItem[1];

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                    Integer.toString(UF_NORMAL_ACCOUNT +  UF_ACCOUNTDISABLE)));

            ldapctx.modifyAttributes(ldapName, mods);

        }

    }

    public void setAttributes(String name, Object obj) {

    }
}
