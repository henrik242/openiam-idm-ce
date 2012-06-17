package org.openiam.base;

import java.util.List;


/**
 * Base interface for all attribute objects which are name value pairs
 *
 * @param <T>
 * @param <ID>
 * @author Suneet Shah
 */
public interface BaseAttributeDAO<T, ID> extends BaseDAO<T, ID> {
    /**
     * Return a list of OrganizationAttribute objects for the organization that is specified by the parentId
     *
     * @param parentId
     * @return
     */
    List<T> findAttributesByParent(String parentId);

    /**
     * Removes all the OrganizationAttributes that are associated with the Organization specified by the parentId.
     *
     * @param parentId
     */
    void deleteAttributesByParent(String parentId);

}
