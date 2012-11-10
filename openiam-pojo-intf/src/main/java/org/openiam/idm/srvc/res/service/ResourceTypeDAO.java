package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;

import java.util.List;

public interface ResourceTypeDAO {

    void remove(ResourceTypeEntity persistentInstance);

    ResourceTypeEntity update(ResourceTypeEntity detachedInstance);

    ResourceTypeEntity findById(java.lang.String id);

    List<ResourceTypeEntity> findByExample(ResourceTypeEntity instance);

    ResourceTypeEntity add(ResourceTypeEntity instance);

    List<ResourceTypeEntity> findAllResourceTypes();

    int removeAllResourceTypes();

}