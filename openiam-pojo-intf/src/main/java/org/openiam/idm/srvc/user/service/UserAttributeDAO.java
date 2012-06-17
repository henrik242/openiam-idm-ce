package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.dto.UserAttribute;

import java.util.List;

public interface UserAttributeDAO {

    void add(UserAttribute transientInstance);

    void attachDirty(UserAttribute instance);

    void attachClean(UserAttribute instance);

    void remove(UserAttribute persistentInstance);

    UserAttribute update(UserAttribute detachedInstance);

    UserAttribute findById(java.lang.String id);

    List<UserAttribute> findUserAttributes(String userId);

    void deleteUserAttributes(String userId);
}
