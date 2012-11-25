package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.springframework.stereotype.Component;

@Component("roleAttributeDozerMapper")
public class RoleAttributeDozerConverter extends AbstractDozerEntityConverter<RoleAttribute, RoleAttributeEntity> {

    @Override
	public RoleAttributeEntity convertEntity(RoleAttributeEntity entity, boolean isDeep) {
		return convert(entity, isDeep, RoleAttributeEntity.class);
	}

	@Override
	public RoleAttribute convertDTO(RoleAttribute entity, boolean isDeep) {
		return convert(entity, isDeep, RoleAttribute.class);
	}

	@Override
	public RoleAttributeEntity convertToEntity(RoleAttribute entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, RoleAttributeEntity.class);
	}

	@Override
	public RoleAttribute convertToDTO(RoleAttributeEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, RoleAttribute.class);
	}

	@Override
	public List<RoleAttributeEntity> convertToEntityList(List<RoleAttribute> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, RoleAttributeEntity.class);
	}

	@Override
	public List<RoleAttribute> convertToDTOList(List<RoleAttributeEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, RoleAttribute.class);
	}

}
