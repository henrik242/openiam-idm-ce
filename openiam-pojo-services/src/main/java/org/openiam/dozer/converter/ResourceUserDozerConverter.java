package org.openiam.dozer.converter;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.res.dto.ResourceUser;
import org.springframework.stereotype.Component;

@Component("resourceUserDozerConverter")
public class ResourceUserDozerConverter extends AbstractDozerEntityConverter<ResourceUser, ResourceUserEntity> {

	@Override
	public ResourceUserEntity convertEntity(final ResourceUserEntity entity, final boolean isDeep) {
		return convert(entity, isDeep, ResourceUserEntity.class);
	}

	@Override
	public ResourceUser convertDTO(ResourceUser entity, boolean isDeep) {
		return convert(entity, isDeep, ResourceUser.class);
	}

	@Override
	public ResourceUserEntity convertToEntity(ResourceUser entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourceUserEntity.class);
	}

	@Override
	public ResourceUser convertToDTO(ResourceUserEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourceUser.class);
	}

	@Override
	public List<ResourceUserEntity> convertToEntityList(List<ResourceUser> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourceUserEntity.class);
	}

	@Override
	public List<ResourceUser> convertToDTOList(List<ResourceUserEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourceUser.class);
	}

}
