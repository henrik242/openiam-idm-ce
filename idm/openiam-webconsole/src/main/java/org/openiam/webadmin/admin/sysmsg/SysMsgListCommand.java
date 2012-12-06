package org.openiam.webadmin.admin.sysmsg;

import org.openiam.idm.srvc.msg.dto.NotificationDto;

import java.util.List;

public class SysMsgListCommand {
    private List<NotificationDto> msgList;
    private NotificationDto selectedMsg = new NotificationDto();

    public SysMsgListCommand() {
    }

    public List<NotificationDto> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<NotificationDto> msgList) {
        this.msgList = msgList;
    }

    public NotificationDto getSelectedMsg() {
        return selectedMsg;
    }

    public void setSelectedMsg(NotificationDto selectedMsg) {
        this.selectedMsg = selectedMsg;
    }
}
