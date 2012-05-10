package org.openiam.spml2.util.connect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

import java.util.*;

/**
 * Manages connections to LDAP
 * @author Suneet Shah
 *
 */
public class LdapConnectionMgr implements ConnectionMgr {

	private String keystore;
	LdapContext ctxLdap = null;
	
	static protected ResourceBundle secres = ResourceBundle.getBundle("securityconf");
	
    private static final Log log = LogFactory.getLog(LdapConnectionMgr.class);
    public static ApplicationContext ac;


    public LdapConnectionMgr() {
    	keystore = secres.getString("KEYSTORE");
    }
    


	public LdapContext connect(ManagedSys managedSys)  throws NamingException{

		LdapContext ldapContext = null;
		Hashtable<String, String> envDC = new Hashtable();
	
		keystore = secres.getString("KEYSTORE");
        if (keystore != null && !keystore.isEmpty())  {
		    System.setProperty("javax.net.ssl.trustStore",keystore);
        }

        if (managedSys == null) {
            log.debug("ManagedSys is null");
            return null;
        }

		String hostUrl = managedSys.getHostUrl();
		if (managedSys.getPort() > 0 ) {
			hostUrl = hostUrl + ":" + String.valueOf(managedSys.getPort());
		}

        log.debug("Connecting to target system: " + managedSys.getManagedSysId() );
        log.debug("Managed = " + managedSys);

		//log.info(" directory login = " + managedSys.getUserId() );
		//log.info(" directory login passowrd= " + managedSys.getDecryptPassword() );
		
		envDC.put(Context.PROVIDER_URL,hostUrl);
		envDC.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");		
		envDC.put(Context.SECURITY_AUTHENTICATION, "simple" ); // simple
		envDC.put(Context.SECURITY_PRINCIPAL,managedSys.getUserId());  //"administrator@diamelle.local"
		envDC.put(Context.SECURITY_CREDENTIALS,managedSys.getDecryptPassword());	

        /*
        Protocol is defined in the url - ldaps vs ldap
        This is not necessary
        if (managedSys.getCommProtocol() != null && managedSys.getCommProtocol().equalsIgnoreCase("SSL")) {
			envDC.put(Context.SECURITY_PROTOCOL, managedSys.getCommProtocol());
		}
		*/


        try {

            ldapContext = new InitialLdapContext(envDC,null);

        }catch (CommunicationException ce) {
            // check if there is a secondary connection linked to this
            String secondarySysID =  managedSys.getSecondaryRepositoryId();
            if (secondarySysID != null) {

                // recursively search through the chained list of linked managed systems
                ManagedSystemDataService managedSysService =  (ManagedSystemDataService) ac.getBean("managedSysService");
                ManagedSys secondarySys =  managedSysService.getManagedSys(secondarySysID);
                return connect(secondarySys);

            }
            // no secondary repository
            throw ce;


        } catch(NamingException ne) {
            log.error(ne.toString());
            throw ne;

        }catch (Exception e) {
            log.error(e.toString());
            return null;
        }

		return ldapContext;

	}



	public void close() throws NamingException {

		if (this.ctxLdap != null) { 
    		ctxLdap.close();
		}
		ctxLdap = null;
		
	}

    public void setApplicationContext(ApplicationContext applicationContext){
        ac = applicationContext;
    }

}
