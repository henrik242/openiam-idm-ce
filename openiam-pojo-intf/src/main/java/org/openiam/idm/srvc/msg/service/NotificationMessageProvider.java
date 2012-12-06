package org.openiam.idm.srvc.msg.service;

import java.util.List;
import java.util.Map;

/**
 * This interface should be implemented in Groovy script
 */
public interface NotificationMessageProvider {

    List<Message> build(Map<String,String> args);

}
