package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.user.domain.UserAttributeEntity;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.springframework.stereotype.Component;

@Component("userAttributeDozerMapper")
public class UserAttributeDozerConverter extends AbstractDozerEntityConverter<UserAttribute, UserAttributeEntity> {

    @Override
	public UserAttributeEntity convertEntity(UserAttributeEntity entity, boolean isDeep) {
		return convert(entity, isDeep, UserAttributeEntity.class);
	}

	@Override
	public UserAttribute convertDTO(UserAttribute entity, boolean isDeep) {
		return convert(entity, isDeep, UserAttribute.class);
	}

	@Override
	public UserAttributeEntity convertToEntity(UserAttribute entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserAttributeEntity.class);
	}

	@Override
	public UserAttribute convertToDTO(UserAttributeEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserAttribute.class);
	}

	@Override
	public List<UserAttributeEntity> convertToEntityList(List<UserAttribute> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserAttributeEntity.class);
	}

	@Override
	public List<UserAttribute> convertToDTOList(List<UserAttributeEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserAttribute.class);
	}

}
