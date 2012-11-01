package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.UserAttributeEntity;
import org.openiam.idm.srvc.user.dto.UserAttribute;

import java.util.List;

public interface UserAttributeDAO {

    void add(UserAttributeEntity transientInstance);

    void attachDirty(UserAttributeEntity instance);

    void attachClean(UserAttributeEntity instance);

    void remove(UserAttributeEntity persistentInstance);

    UserAttributeEntity update(UserAttributeEntity detachedInstance);

    UserAttributeEntity findById(java.lang.String id);

    List<UserAttributeEntity> findUserAttributes(String userId);

    void deleteUserAttributes(String userId);
}
