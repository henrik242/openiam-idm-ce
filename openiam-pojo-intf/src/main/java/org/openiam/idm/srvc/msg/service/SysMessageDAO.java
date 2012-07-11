package org.openiam.idm.srvc.msg.service;

// Generated Nov 27, 2009 11:18:13 PM by Hibernate Tools 3.2.2.GA

import org.openiam.idm.srvc.msg.dto.NotificationConfig;

import java.util.List;

/**
 * Home object for domain model class SysMessageDelivery.
 *
 * @author Hibernate Tools
 * @see org.openiam.idm.srvc.msg.dto.SysMessageDelivery
 */
public interface SysMessageDAO {


    public NotificationConfig add(NotificationConfig transientInstance);

    public void remove(NotificationConfig persistentInstance);

    public NotificationConfig update(NotificationConfig detachedInstance);

    public NotificationConfig findById(java.lang.String id);

    public List<NotificationConfig> findAll();

}
