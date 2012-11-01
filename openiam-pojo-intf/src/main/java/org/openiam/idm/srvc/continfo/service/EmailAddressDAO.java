package org.openiam.idm.srvc.continfo.service;

import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * Data access object for EmailAddressEntity. EmailAddressEntity usually exists with a parent entity
 * such as a user, organization, account, etc. Client components should use
 * the service objects such as <code>UserMgr</code> instead of using the DAO
 * directly.
 *
 * @author Suneet Shah
 */
public interface EmailAddressDAO {
    //extends BaseDAO<EmailAddress, String> {

    /**
     * Return an object for the id.
     *
     * @param id
     */
    EmailAddressEntity findById(String id);

    /**
     * Adds a new instance
     *
     * @param instance
     */
    EmailAddressEntity add(EmailAddressEntity instance);

    /**
     * Updates an existing instance
     *
     * @param instace
     */
    void update(EmailAddressEntity instace);

    /**
     * Removes an existing instance
     *
     * @param instance
     */
    void remove(EmailAddressEntity instance);

    /**
     * Persist a map of EmailAddress objects at one time. Handles add, update, delete.
     *
     * @param parentId
     * @param parentType
     * @param adrMap
     */
    void saveEmailAddressMap(String parentId, String parentType, Map<String, EmailAddressEntity> adrMap);

    /**
     * Returns a Map of EmailAddress objects for the parentId and parentType combination.
     * The map is keyed on the address.description. Address.description indicates
     * the type of address that we have; ie. Shipping, Billing, etc.
     *
     * @param parentId
     * @param parentType
     * @return
     */
    Map<String, EmailAddressEntity> findByParent(String parentId, String parentType);

    /**
     * Returns a List of EmailAddress objects for the parentId and parentType combination.
     *
     * @param parentId
     * @param parentType
     * @return
     */
    public List<EmailAddressEntity> findByParentAsList(String parentId, String parentType);


    /**
     * Removes all EmailAddresses for a parent
     *
     * @param parentId
     * @param parentType
     */
    void removeByParent(String parentId, String parentType);

    /**
     * Returns a default address for the parentId and parentType combination.
     * Returns null if a match is not found.
     *
     * @return
     */
    EmailAddressEntity findDefault(String parentId, String parentType);

    /**
     * Return an address object that matches the Name field. Returns null if a match
     * is not found.
     *
     * @param name
     * @param parentId
     * @param parentType
     * @return
     */
    EmailAddressEntity findByName(String name, String parentId, String parentType);


}
