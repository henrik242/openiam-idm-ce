package org.openiam.idm.srvc.msg.service;

// Generated Nov 27, 2009 11:18:13 PM by Hibernate Tools 3.2.2.GA

import org.openiam.idm.srvc.msg.dto.SysMessage;

import java.util.List;

/**
 * Home object for domain model class SysMessageDelivery.
 *
 * @author Hibernate Tools
 * @see org.openiam.idm.srvc.msg.dto.SysMessageDelivery
 */
public interface SysMessageDAO {


    public SysMessage add(SysMessage transientInstance);

    public void remove(SysMessage persistentInstance);

    public SysMessage update(SysMessage detachedInstance);

    public SysMessage findById(java.lang.String id);

    public List<SysMessage> findAll();

}
