package org.openiam.dozer.converter;

import java.util.List;

import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.openiam.idm.srvc.res.dto.ResourceRole;
import org.openiam.idm.srvc.res.dto.ResourceUser;
import org.springframework.stereotype.Component;

@Component("resourceRoleDozerConverter")
public class ResourceRoleDozerConverter extends AbstractDozerEntityConverter<ResourceRole, ResourceRoleEntity> {

	@Override
	public ResourceRoleEntity convertEntity(ResourceRoleEntity entity, boolean isDeep) {
		return convert(entity, isDeep, ResourceRoleEntity.class);
	}

	@Override
	public ResourceRole convertDTO(ResourceRole entity, boolean isDeep) {
		return convert(entity, isDeep, ResourceRole.class);
	}

	@Override
	public ResourceRoleEntity convertToEntity(ResourceRole entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourceRoleEntity.class);
	}

	@Override
	public ResourceRole convertToDTO(ResourceRoleEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourceRole.class);
	}

	@Override
	public List<ResourceRoleEntity> convertToEntityList(List<ResourceRole> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourceRoleEntity.class);
	}

	@Override
	public List<ResourceRole> convertToDTOList(List<ResourceRoleEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourceRole.class);
	}

}
