package org.openiam.idm.srvc.msg.service;

import org.openiam.dozer.converter.MailTemplateDozerConverter;
import org.openiam.idm.srvc.msg.domain.MailTemplateEntity;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MailTemplateServiceImpl implements MailTemplateService {

    @Autowired
    private MailTemplateDozerConverter mailTemplateDozerConverter;

    MailTemplateDAO mailTemplateDAO;

    @Override
    public MailTemplateDto addTemplate(MailTemplateDto transientInstance) {
        if (transientInstance == null) {
            throw new NullPointerException("Config object is null");
        }

        MailTemplateEntity mailTemplateEntity = mailTemplateDAO.add(mailTemplateDozerConverter.convertToEntity(transientInstance, true));
        return mailTemplateDozerConverter.convertToDTO(mailTemplateEntity, true);
    }

    @Override
    public void removeTemplate(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        MailTemplateEntity mailTemplateEntity = new MailTemplateEntity();
        mailTemplateEntity.setTmplId(id);
        mailTemplateDAO.remove(mailTemplateEntity);
    }

    @Override
    public MailTemplateDto updateTemplate(MailTemplateDto detachedInstance) {
        if (detachedInstance == null) {
            throw new NullPointerException("policy is null");
        }
        MailTemplateEntity sysMessageEntity = mailTemplateDAO.update(mailTemplateDozerConverter.convertToEntity(detachedInstance, true));
        return mailTemplateDozerConverter.convertToDTO(sysMessageEntity, true);
    }

    @Override
    public MailTemplateDto getTemplateById(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }

        return mailTemplateDozerConverter.convertToDTO(mailTemplateDAO.findById(id), true);
    }

    @Override
    public List<MailTemplateDto> getAllTemplates() {
        return mailTemplateDozerConverter.convertToDTOList(mailTemplateDAO.findAll(), true);
    }

    public MailTemplateDAO getMailTemplateDAO() {
        return mailTemplateDAO;
    }

    public void setMailTemplateDAO(MailTemplateDAO mailTemplateDAO) {
        this.mailTemplateDAO = mailTemplateDAO;
    }

}
