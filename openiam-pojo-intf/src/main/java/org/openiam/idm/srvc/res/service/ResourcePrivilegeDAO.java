package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.dto.ResourcePrivilege;

import java.util.List;

/**
 * DAO to manage ResourcePrivilege objects
 * User: arun
 * Date: 8/8/11
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ResourcePrivilegeDAO {

    ResourcePrivilege findById(String id);


    ResourcePrivilege add(ResourcePrivilege instance);

    void remove(ResourcePrivilege instance);

    ResourcePrivilege update(ResourcePrivilege instance);


    List<ResourcePrivilege> findPrivilegesByResourceId(String resourceId);

    List<ResourcePrivilege> findPrivilegesByEntitlementType(String resourceId, String type);




}
