package org.openiam.dozer.converter;

import org.openiam.idm.srvc.msg.domain.MailTemplateEntity;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("mailTemplateDozerConverter")
public class MailTemplateDozerConverter extends AbstractDozerEntityConverter<MailTemplateDto, MailTemplateEntity> {

    @Override
    public MailTemplateEntity convertEntity(MailTemplateEntity entity, boolean isDeep) {
        return convert(entity, isDeep, MailTemplateEntity.class);
    }

    @Override
    public MailTemplateDto convertDTO(MailTemplateDto entity, boolean isDeep) {
        return convert(entity, isDeep, MailTemplateDto.class);
    }

    @Override
    public MailTemplateEntity convertToEntity(MailTemplateDto entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, MailTemplateEntity.class);
    }

    @Override
    public MailTemplateDto convertToDTO(MailTemplateEntity entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, MailTemplateDto.class);
    }

    @Override
    public List<MailTemplateEntity> convertToEntityList(List<MailTemplateDto> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, MailTemplateEntity.class);
    }

    @Override
    public List<MailTemplateDto> convertToDTOList(List<MailTemplateEntity> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, MailTemplateDto.class);
    }
}
