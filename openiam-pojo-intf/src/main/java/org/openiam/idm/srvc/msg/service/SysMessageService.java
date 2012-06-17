package org.openiam.idm.srvc.msg.service;

// Generated Nov 27, 2009 11:18:13 PM by Hibernate Tools 3.2.2.GA

import org.openiam.idm.srvc.msg.dto.SysMessage;

import java.util.List;

/**
 * Interface for the SysMessageDelivery service. The message Delivery service is allows you to create and define messages and have them
 * delivered to the audience an application such as the selfservice app..
 *
 * @author Suneet shah
 * @see org.openiam.idm.srvc.msg.dto.SysMessage
 */
public interface SysMessageService {

    public SysMessage addMessage(SysMessage transientInstance);

    public void removeMessage(String id);

    public SysMessage updateMessage(SysMessage detachedInstance);

    public SysMessage getMessageById(java.lang.String id);


    public List<SysMessage> getAllMessages();
}
