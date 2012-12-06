package org.openiam.dozer.converter;

import org.openiam.idm.srvc.msg.domain.NotificationEntity;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("sysMessageDozerConverter")
public class NotificationDozerConverter extends AbstractDozerEntityConverter<NotificationDto, NotificationEntity> {

    @Override
    public NotificationEntity convertEntity(NotificationEntity entity, boolean isDeep) {
        return convert(entity, isDeep, NotificationEntity.class);
    }

    @Override
    public NotificationDto convertDTO(NotificationDto entity, boolean isDeep) {
        return convert(entity, isDeep, NotificationDto.class);
    }

    @Override
    public NotificationEntity convertToEntity(NotificationDto entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, NotificationEntity.class);
    }

    @Override
    public NotificationDto convertToDTO(NotificationEntity entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, NotificationDto.class);
    }

    @Override
    public List<NotificationEntity> convertToEntityList(List<NotificationDto> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, NotificationEntity.class);
    }

    @Override
    public List<NotificationDto> convertToDTOList(List<NotificationEntity> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, NotificationDto.class);
    }

}
