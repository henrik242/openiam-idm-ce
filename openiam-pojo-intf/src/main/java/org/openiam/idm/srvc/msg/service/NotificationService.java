package org.openiam.idm.srvc.msg.service;

// Generated Nov 27, 2009 11:18:13 PM by Hibernate Tools 3.2.2.GA

import org.openiam.idm.srvc.msg.dto.NotificationDto;

import java.util.List;

/**
 * Interface for the SysMessageDelivery service. The message Delivery service is allows you to create and define messages and have them
 * delivered to the audience an application such as the selfservice app..
 *
 * @author Suneet shah
 * @see org.openiam.idm.srvc.msg.dto.NotificationDto
 */
public interface NotificationService {

    NotificationDto addNotification(NotificationDto transientInstance);

    void removeNotification(String id);

    NotificationDto updateNotification(NotificationDto detachedInstance);

    NotificationDto getNotificationById(java.lang.String id);

    NotificationDto getNotificationByName(String name);

    List<NotificationDto> getAllNotifications();
}
