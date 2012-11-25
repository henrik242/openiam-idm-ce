package org.openiam.dozer.converter;

import java.util.List;

import org.dozer.Mapper;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.springframework.stereotype.Component;

@Component("resorucePropDozerConverter")
public class ResourcePropDozerConverter extends AbstractDozerEntityConverter<ResourceProp, ResourcePropEntity> {

	@Override
	public ResourcePropEntity convertEntity(final ResourcePropEntity entity, final boolean isDeep) {
		return convert(entity, isDeep, ResourcePropEntity.class);
	}

	@Override
	public ResourceProp convertDTO(final ResourceProp entity, final boolean isDeep) {
		return convert(entity, isDeep, ResourceProp.class);
	}

	@Override
	public ResourcePropEntity convertToEntity(final ResourceProp entity, final boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourcePropEntity.class);
	}

	@Override
	public ResourceProp convertToDTO(final ResourcePropEntity entity, final boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, ResourceProp.class);
	}

	@Override
	public List<ResourcePropEntity> convertToEntityList(final List<ResourceProp> list, final boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourcePropEntity.class);
	}

	@Override
	public List<ResourceProp> convertToDTOList(final List<ResourcePropEntity> list, final boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, ResourceProp.class);
	}

}
