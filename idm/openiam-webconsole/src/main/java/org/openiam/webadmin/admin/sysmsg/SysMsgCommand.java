package org.openiam.webadmin.admin.sysmsg;

import java.io.Serializable;

import org.openiam.idm.srvc.msg.dto.NotificationConfig;

/**
 * Command object for the SysMsgDetailController
 * @author suneet
 *
 */
public class SysMsgCommand implements Serializable {


	

	protected NotificationConfig msg = new NotificationConfig();
    
	public SysMsgCommand() {
    	
    }

	public NotificationConfig getMsg() {
		return msg;
	}

	public void setMsg(NotificationConfig msg) {
		this.msg = msg;
	}


	


	

}
