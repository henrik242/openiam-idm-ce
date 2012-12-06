package org.openiam.webadmin.admin.mailtmpl;

import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class MailTemplateCommand implements Serializable {

    private static final long serialVersionUID = -4333761095624702984L;

    private MailTemplateDto template = new MailTemplateDto();
    private MultipartFile attachmentFile;

    public MailTemplateDto getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplateDto template) {
        this.template = template;
    }

    public MultipartFile getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(MultipartFile attachmentFile) {
        this.attachmentFile = attachmentFile;
    }
}
