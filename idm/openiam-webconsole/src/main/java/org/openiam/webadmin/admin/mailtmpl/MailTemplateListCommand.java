package org.openiam.webadmin.admin.mailtmpl;

import org.openiam.idm.srvc.msg.dto.MailTemplateDto;

import java.util.List;

public class MailTemplateListCommand {
    private List<MailTemplateDto> templateList;
    private MailTemplateDto selectedTemplate = new MailTemplateDto();

    public MailTemplateListCommand() {
    }

    public List<MailTemplateDto> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<MailTemplateDto> templateList) {
        this.templateList = templateList;
    }

    public MailTemplateDto getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(MailTemplateDto selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }
}
