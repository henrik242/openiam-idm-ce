package org.openiam.idm.srvc.msg.dto;

import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.msg.domain.MailTemplateEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailTemplateDto", propOrder = {
        "tmplId",
        "name",
        "subject",
        "type",
        "body",
        "attachmentFilePath"
})

@DozerDTOCorrespondence(MailTemplateEntity.class)
public class MailTemplateDto implements Serializable {

    private static final long serialVersionUID = -406594689219258805L;

    private String tmplId;
    private String name;
    private String subject;
    private MessageBodyType type;

    private String body;
    private String attachmentFilePath;

    public MailTemplateDto() {
    }

    public String getTmplId() {
        return tmplId;
    }

    public void setTmplId(String tmplId) {
        this.tmplId = tmplId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MessageBodyType getType() {
        return type;
    }

    public void setType(MessageBodyType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachmentFilePath() {
        return attachmentFilePath;
    }

    public void setAttachmentFilePath(String attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailTemplateDto that = (MailTemplateDto) o;

        if (attachmentFilePath != null ? !attachmentFilePath.equals(that.attachmentFilePath) : that.attachmentFilePath != null)
            return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (tmplId != null ? !tmplId.equals(that.tmplId) : that.tmplId != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tmplId != null ? tmplId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (attachmentFilePath != null ? attachmentFilePath.hashCode() : 0);
        return result;
    }
}
