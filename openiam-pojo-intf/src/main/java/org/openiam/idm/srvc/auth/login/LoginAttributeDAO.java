package org.openiam.idm.srvc.auth.login;

import org.openiam.idm.srvc.auth.dto.LoginAttribute;

import java.util.List;

/**
 * Data access interface for domain model class LoginAttribute.
 *
 * @author Suneet Shah
 */
public interface LoginAttributeDAO {

    public abstract void add(LoginAttribute transientInstance);

    public abstract void remove(LoginAttribute persistentInstance);

    public abstract LoginAttribute update(LoginAttribute detachedInstance);

    public abstract LoginAttribute findById(java.lang.String id);

    public abstract List<LoginAttribute> findByExample(LoginAttribute instance);

}