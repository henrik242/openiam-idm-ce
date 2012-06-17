package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.dto.ResourceType;

import java.util.List;

public interface ResourceTypeDAO {

    void remove(ResourceType persistentInstance);

    ResourceType update(ResourceType detachedInstance);

    ResourceType findById(java.lang.String id);

    List<ResourceType> findByExample(ResourceType instance);

    ResourceType add(ResourceType instance);

    List<ResourceType> findAllResourceTypes();

    int removeAllResourceTypes();

}