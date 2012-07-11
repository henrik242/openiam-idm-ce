package org.openiam.idm.srvc.msg.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Object to deliver message through the system
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationConfig", propOrder = {
        "notificationConfigId",
        "name",
        "selectionParamXml",
        "msgSubject",
        "msgTemplateUrl",
        "msgFrom",
        "msgBcc"
})
public class NotificationConfig implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5748586827726042100L;

    private String notificationConfigId;
    private String name;
    private String selectionParamXml;
    private String msgSubject;

    private String msgTemplateUrl;
    private String msgFrom;
    private String msgBcc;




    public NotificationConfig() {
    }

    public String getNotificationConfigId() {
        return notificationConfigId;
    }

    public void setNotificationConfigId(String notificationConfigId) {
        this.notificationConfigId = notificationConfigId;
    }

    public String getSelectionParamXml() {
        return selectionParamXml;
    }

    public void setSelectionParamXml(String selectionParamXml) {
        this.selectionParamXml = selectionParamXml;
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public String getMsgTemplateUrl() {
        return msgTemplateUrl;
    }

    public void setMsgTemplateUrl(String msgTemplateUrl) {
        this.msgTemplateUrl = msgTemplateUrl;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getMsgBcc() {
        return msgBcc;
    }

    public void setMsgBcc(String msgBcc) {
        this.msgBcc = msgBcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
