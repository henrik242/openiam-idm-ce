package org.openiam.idm.srvc.msg.service;

// Generated Nov 27, 2009 11:18:13 PM by Hibernate Tools 3.2.2.GA

import org.openiam.idm.srvc.msg.domain.NotificationEntity;

import java.util.List;

/**
 * Home object for domain model class SysMessageDelivery.
 *
 * @author Hibernate Tools
 * @see org.openiam.idm.srvc.msg.domain.NotificationEntity
 */
public interface NotificationDAO {

    NotificationEntity add(NotificationEntity transientInstance);

    void remove(NotificationEntity persistentInstance);

    NotificationEntity update(NotificationEntity detachedInstance);

    NotificationEntity findById(java.lang.String id);

    NotificationEntity findByName(String name);

    List<NotificationEntity> findAll();

    List<NotificationEntity> findConfigurableList();

    List<NotificationEntity> findSystemList();

}
