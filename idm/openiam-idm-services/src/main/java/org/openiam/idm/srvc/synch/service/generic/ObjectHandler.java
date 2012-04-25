package org.openiam.idm.srvc.synch.service.generic;

import  org.openiam.idm.srvc.synch.dto.LineObject;
/**
 * Interface which each Object specific Handler must implement. The Group, Org, Role Handlers much each implement this
 * interface
 * User: suneetshah
 * Date: 4/1/12
 * Time: 10:37 PM
 */
public interface ObjectHandler {

    /**
     * Maps the contents of the LineObject into a concrete object such as Group, Role, etc.
     */
    Object populateObject(LineObject rowObj);

    /**
     * If this object already exists, then retrieve it
     * @param obj
     * @return
     */
    Object existingObject(Object obj);
}
