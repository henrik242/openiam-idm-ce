package org.openiam.idm.srvc.continfo.service;

import org.openiam.idm.srvc.continfo.domain.PhoneEntity;
import org.openiam.idm.srvc.continfo.dto.Phone;

import java.util.List;
import java.util.Map;

/**
 * Data access object for Phone. Phone usually exists with a parent entity
 * such as a user, organization, account, etc.  Client components should use
 * the service objects such as <code>UserMgr</code> instead of using the DAO
 * directly.
 *
 * @author Suneet Shah
 */
public interface PhoneDAO {
    //extends BaseDAO<EmailAddress, String> {

    /**
     * Return an object for the id.
     *
     * @param id
     */
    PhoneEntity findById(String id);

    /**
     * Adds a new instance
     *
     * @param instance
     */
    PhoneEntity add(PhoneEntity instance);

    /**
     * Updates an existing instance
     *
     * @param instace
     */
    void update(PhoneEntity instace);

    /**
     * Removes an existing instance
     *
     * @param instance
     */
    void remove(PhoneEntity instance);

    /**
     * Persist a map of Phone objects at one time. Handles add, update, delete.
     *
     * @param parentId
     * @param parentType
     * @param adrMap
     */
    void savePhoneMap(String parentId, String parentType, Map<String, PhoneEntity> adrMap);

    /**
     * Returns a Map of EmailAddress objects for the parentId and parentType combination.
     * The map is keyed on the address.description. Address.description indicates
     * the type of address that we have; ie. Shipping, Billing, etc.
     *
     * @param parentId
     * @param parentType
     * @return
     */
    Map<String, PhoneEntity> findByParent(String parentId, String parentType);

    /**
     * Returns a List of EmailAddress objects for the parentId and parentType combination.
     *
     * @param parentId
     * @param parentType
     * @return
     */
    public List<PhoneEntity> findByParentAsList(String parentId, String parentType);


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
    PhoneEntity findDefault(String parentId, String parentType);

    /**
     * Return an address object that matches the Name. Returns null if a match
     * is not found.
     *
     * @param name
     * @param parentId
     * @param parentType
     * @return
     */
    PhoneEntity findByName(String name, String parentId, String parentType);


}
