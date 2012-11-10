package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;

import java.util.List;

/**
 * DAO to manage ResourcePrivilege objects
 * User: arun
 * Date: 8/8/11
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ResourcePrivilegeDAO {

    ResourcePrivilegeEntity findById(String id);


    ResourcePrivilegeEntity add(ResourcePrivilegeEntity instance);

    void remove(ResourcePrivilegeEntity instance);

    ResourcePrivilegeEntity update(ResourcePrivilegeEntity instance);


    List<ResourcePrivilegeEntity> findPrivilegesByResourceId(String resourceId);

    List<ResourcePrivilegeEntity> findPrivilegesByEntitlementType(String resourceId, String type);




}
