package org.openiam.idm.srvc.auth.context;

import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * Interface to define credential objects that will be used during the authentication process
 *
 * @author Suneet Shah
 */
@XmlSeeAlso({
        PasswordCredential.class
})
public interface Credential {

}
