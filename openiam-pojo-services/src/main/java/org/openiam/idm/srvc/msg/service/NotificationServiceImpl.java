/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.idm.srvc.msg.service;

import java.util.List;

import org.openiam.dozer.converter.NotificationDozerConverter;
import org.openiam.idm.srvc.msg.domain.NotificationEntity;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author suneet
 *
 */
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationDozerConverter notificationDozerConvertor;

	NotificationDAO msgDao;
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#addNotification(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public NotificationDto addNotification(NotificationDto msg) {
		if (msg == null) {
			throw new NullPointerException("Config object is null");
		}
		
		NotificationEntity sysMessageEntity = msgDao.add(notificationDozerConvertor.convertToEntity(msg, true));
        return notificationDozerConvertor.convertToDTO(sysMessageEntity, true);
	}

    @Override
    public NotificationDto getNotificationByName(String name) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }
        return notificationDozerConvertor.convertToDTO( msgDao.findByName(name), true);
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#getNotificationById(java.lang.String)
      */
	public NotificationDto getNotificationById(String id) {
		if (id == null) {
			throw new NullPointerException("id is null");
		}
		
		return notificationDozerConvertor.convertToDTO(msgDao.findById(id), true);
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#removeNotification(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public void removeNotification(String id) {
		if (id == null) {
			throw new NullPointerException("id is null");
		}
        NotificationEntity msg = new NotificationEntity();
		msg.setMsgId(id);
		msgDao.remove(msg);
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#updateNotification(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public NotificationDto updateNotification(NotificationDto msg) {
		if (msg == null) {
			throw new NullPointerException("policy is null");
		}
        NotificationEntity sysMessageEntity = msgDao.update(notificationDozerConvertor.convertToEntity(msg, true));
        return notificationDozerConvertor.convertToDTO(sysMessageEntity, true);
	}



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageService#getAllNotifications()
	 */
	public List<NotificationDto> getAllNotifications() {
        return notificationDozerConvertor.convertToDTOList(msgDao.findAll(), true);
	}



	public NotificationDAO getMsgDao() {
		return msgDao;
	}



	public void setMsgDao(NotificationDAO msgDao) {
		this.msgDao = msgDao;
	}


}
