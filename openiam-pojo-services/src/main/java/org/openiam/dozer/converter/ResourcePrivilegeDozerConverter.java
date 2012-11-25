package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;
import org.openiam.idm.srvc.res.dto.ResourcePrivilege;
import org.springframework.stereotype.Component;

@Component("resourcePrivilegeDozerMapper")
public class ResourcePrivilegeDozerConverter extends AbstractDozerEntityConverter<ResourcePrivilege, ResourcePrivilegeEntity> {

    @Override
	public ResourcePrivilegeEntity convertEntity(ResourcePrivilegeEntity entity, boolean isDeep) {
		return convert(entity, isDeep, ResourcePrivilegeEntity.class);
	}

	@Override
	public ResourcePrivilege convertDTO(ResourcePrivilege entity, boolean isDeep) {
		return convert(entity, isDeep, ResourcePrivilege.class);
	}

	@Override
	public ResourcePrivilegeEntity convertToEntity(ResourcePrivilege entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourcePrivilegeEntity.class);
	}

	@Override
	public ResourcePrivilege convertToDTO(ResourcePrivilegeEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourcePrivilege.class);
	}

	@Override
	public List<ResourcePrivilegeEntity> convertToEntityList(List<ResourcePrivilege> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourcePrivilegeEntity.class);
	}

	@Override
	public List<ResourcePrivilege> convertToDTOList(List<ResourcePrivilegeEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourcePrivilege.class);
	}
}
