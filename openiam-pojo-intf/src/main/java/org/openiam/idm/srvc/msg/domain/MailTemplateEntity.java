package org.openiam.idm.srvc.msg.domain;

import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.dto.MessageBodyType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MAIL_TEMPLATE")
@DozerDTOCorrespondence(MailTemplateDto.class)
public class MailTemplateEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "TMPL_ID")
    private String tmplId;

    @Column(name = "TMPL_NAME")
    private String name;

    @Column(name = "TMPL_SUBJECT")
    private String subject;

    @Column(name = "BODY_TYPE")
    @Enumerated(EnumType.STRING)
    private MessageBodyType type;

    @Column(name = "BODY")
    private String body;

    @Column(name = "ATTACHMENT_FILE_PATH")
    private String attachmentFilePath;

    public MailTemplateEntity() {
    }

    public String getTmplId() {
        return tmplId;
    }

    public void setTmplId(String tmplId) {
        this.tmplId = tmplId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailTemplateEntity that = (MailTemplateEntity) o;

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
