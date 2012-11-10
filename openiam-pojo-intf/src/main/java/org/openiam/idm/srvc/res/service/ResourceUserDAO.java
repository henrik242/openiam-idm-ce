package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourceUserEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.openiam.idm.srvc.res.dto.ResourceUserId;

import java.util.List;

public interface ResourceUserDAO {

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#remove(org.openiam.idm.srvc.res.dto.ResourceUser)
      */
    void remove(ResourceUserEntity persistentInstance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#update(org.openiam.idm.srvc.res.dto.ResourceUser)
      */
    ResourceUserEntity update(ResourceUserEntity detachedInstance);

    ResourceUserEntity findById(ResourceUserEmbeddableId id);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#findByExample(org.openiam.idm.srvc.res.dto.ResourceUser)
      */
    List<ResourceUserEntity> findByExample(ResourceUserEntity instance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#add(org.openiam.idm.srvc.res.dto.ResourceUser)
      */
    ResourceUserEntity add(ResourceUserEntity instance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#findAllResourceUsers()
      */
    List<ResourceUserEntity> findAllResourceUsers();

    List<ResourceUserEntity> findAllResourceForUsers(String userId);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceUserDAO#removeAllResourceUsers()
      */
    void removeAllResourceUsers();

    void removeUserFromAllResources(String userId);

}