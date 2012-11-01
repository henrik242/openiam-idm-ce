package org.openiam.idm.srvc.org.service;

import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;

import java.util.List;


/**
 * Data access object interface for OrganizationAttribute.
 *
 * @author Suneet Shah
 */
public interface OrganizationAttributeDAO {

    /**
     * Return an OrganizationAttribute object for the id.
     *
     * @param id
     */
    OrganizationAttributeEntity findById(java.lang.String id);

    void add(OrganizationAttributeEntity instance);

    void update(OrganizationAttributeEntity instace);

    void remove(OrganizationAttributeEntity instance);

    /**
     * Return a list of OrganizationAttribute objects for the organization that is specified by the parentId
     *
     * @param parentId
     * @return
     */
    List<OrganizationAttributeEntity> findAttributesByParent(String parentId);

    /**
     * Removes all the OrganizationAttributes that are associated with the Organization specified by the parentId.
     *
     * @param parentId
     * @return The number of entities deleted.
     */
    int removeAttributesByParent(String parentId);


}
