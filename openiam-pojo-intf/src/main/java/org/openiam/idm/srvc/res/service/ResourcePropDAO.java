package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourcePropEntity;

import java.util.List;

public interface ResourcePropDAO {

    void persist(ResourcePropEntity transientInstance);

    void remove(ResourcePropEntity persistentInstance);

    ResourcePropEntity update(ResourcePropEntity detachedInstance);

    ResourcePropEntity findById(java.lang.String id);

    List<ResourcePropEntity> findByExample(ResourcePropEntity instance);

    ResourcePropEntity add(ResourcePropEntity instance);

    List<ResourcePropEntity> findAllResourceProps();

    int removeAllResourceProps();

}