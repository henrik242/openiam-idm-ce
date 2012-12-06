package org.openiam.idm.srvc.msg.service;

import org.openiam.idm.srvc.msg.dto.MailTemplateDto;

import java.util.List;

public interface MailTemplateService {

    public MailTemplateDto addTemplate(MailTemplateDto transientInstance);

    public void removeTemplate(String id);

    public MailTemplateDto updateTemplate(MailTemplateDto detachedInstance);

    public MailTemplateDto getTemplateById(java.lang.String id);


    public List<MailTemplateDto> getAllTemplates();
}
