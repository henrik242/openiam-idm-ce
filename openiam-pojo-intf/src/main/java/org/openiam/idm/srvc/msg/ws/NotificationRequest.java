package org.openiam.idm.srvc.msg.ws;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * NotificationRequest contains information for notification service to send out a pre-defined
 * notification
 *
 * @author suneet
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationRequest", propOrder = {
        "notificationType",
        "paramList"
})
@XmlRootElement(name = "NotificationRequest")
@XmlSeeAlso({
        NotificationParam.class
})
public class NotificationRequest {
    private String notificationType;
    private List<NotificationParam> paramList = new LinkedList<NotificationParam>();

    public NotificationRequest() {
    }

    public NotificationRequest(String notificationType, List<NotificationParam> paramList) {
        this.notificationType = notificationType;
        this.paramList = paramList;
    }

    public NotificationRequest(String notificationType, Map<String,String> paramMap) {
        this.notificationType = notificationType;
        for(Map.Entry<String,String> entry : paramMap.entrySet()) {
            paramList.add(new NotificationParam(entry.getKey(), entry.getValue()));
        }
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public List<NotificationParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<NotificationParam> paramList) {
        this.paramList = paramList;
    }

}
