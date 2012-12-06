package org.openiam.idm.srvc.msg.dto;

import org.apache.commons.lang.StringUtils;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.msg.domain.NotificationEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationDto", propOrder = {
        "msgId",
        "name",
        "providerScriptName",
        "mailTemplate",
        "startDate",
        "endDate",
        "type"
})
@XmlSeeAlso({
        MailTemplateDto.class
})
@DozerDTOCorrespondence(NotificationEntity.class)
public class NotificationDto implements Serializable {

    private static final long serialVersionUID = 3440156648087333068L;

    private String msgId;

    private String name;

    private String providerScriptName;

    private MailTemplateDto mailTemplate;

    private NotificationType type = NotificationType.CONFIGURABLE;

    private Date startDate;

    private Date endDate;

    public NotificationDto() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderScriptName() {
        return providerScriptName;
    }

    public void setProviderScriptName(String providerScriptName) {
        this.providerScriptName = providerScriptName;
    }

    public MailTemplateDto getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplateDto mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isUseProviderScript() {
        return StringUtils.isNotEmpty(this.providerScriptName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationDto that = (NotificationDto) o;

        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (msgId != null ? !msgId.equals(that.msgId) : that.msgId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (providerScriptName != null ? !providerScriptName.equals(that.providerScriptName) : that.providerScriptName != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = msgId != null ? msgId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (providerScriptName != null ? providerScriptName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
