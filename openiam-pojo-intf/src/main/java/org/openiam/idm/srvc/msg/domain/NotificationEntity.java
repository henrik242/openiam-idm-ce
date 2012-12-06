package org.openiam.idm.srvc.msg.domain;

import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.dto.NotificationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATION")
@DozerDTOCorrespondence(NotificationDto.class)
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 4317232961173999077L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "MSG_ID")
    private String msgId;

    @Column(name = "MSG_NAME")
    private String name;

    @Column(name = "PROVIDER_SCRIPT")
    private String providerScriptName;

    @ManyToOne
    @JoinColumn(name="TMPL_ID", referencedColumnName="TMPL_ID")
    private MailTemplateEntity mailTemplate;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "MSG_TYPE")
    @Enumerated(EnumType.STRING)
    private NotificationType type = NotificationType.CONFIGURABLE;

    public NotificationEntity() {
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

    public MailTemplateEntity getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplateEntity mailTemplate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationEntity that = (NotificationEntity) o;

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
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

