package org.openiam.idm.srvc.msg.service;

import org.openiam.idm.srvc.msg.domain.MailTemplateEntity;

import java.util.List;

public interface MailTemplateDAO {

    MailTemplateEntity add(MailTemplateEntity transientInstance);

    void remove(MailTemplateEntity persistentInstance);

    MailTemplateEntity update(MailTemplateEntity detachedInstance);

    MailTemplateEntity findById(java.lang.String id);

    List<MailTemplateEntity> findAll();

}
