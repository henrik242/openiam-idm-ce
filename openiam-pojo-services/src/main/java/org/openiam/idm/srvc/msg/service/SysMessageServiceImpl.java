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

import org.openiam.idm.srvc.msg.dto.NotificationConfig;

/**
 * @author suneet
 *
 */
public class SysMessageServiceImpl implements SysMessageService {

	SysMessageDAO msgDao;
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#addMessage(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public NotificationConfig addMessage(NotificationConfig msg) {
		if (msg == null) {
			throw new NullPointerException("Config object is null");
		}
		
		return msgDao.add(msg);
	}



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#getMessageById(java.lang.String)
	 */
	public NotificationConfig getMessageById(String id) {
		if (id == null) {
			throw new NullPointerException("id is null");
		}
		
		return msgDao.findById(id);
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#removeMessage(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public void removeMessage(String id) {
		if (id == null) {
			throw new NullPointerException("id is null");
		}
		NotificationConfig msg = new NotificationConfig();
		msg.setNotificationConfigId(id);
		msgDao.remove(msg);
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageDeliveryService#updateMessage(org.openiam.idm.srvc.msg.dto.SysMessageDelivery)
	 */
	public NotificationConfig updateMessage(NotificationConfig msg) {
		if (msg == null) {
			throw new NullPointerException("policy is null");
		}
		return msgDao.update(msg);
	}



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.SysMessageService#getAllMessages()
	 */
	public List<NotificationConfig> getAllMessages() {
		return msgDao.findAll();
	}



	public SysMessageDAO getMsgDao() {
		return msgDao;
	}



	public void setMsgDao(SysMessageDAO msgDao) {
		this.msgDao = msgDao;
	}


}
