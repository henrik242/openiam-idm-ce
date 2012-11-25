package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.dto.OrganizationAttribute;
import org.springframework.stereotype.Component;

@Component("organizationAttributeDozerMapper")
public class OrganizationAttributeDozerConverter extends AbstractDozerEntityConverter<OrganizationAttribute, OrganizationAttributeEntity> {

    @Override
	public OrganizationAttributeEntity convertEntity(OrganizationAttributeEntity entity, boolean isDeep) {
		return convert(entity, isDeep, OrganizationAttributeEntity.class);
	}

	@Override
	public OrganizationAttribute convertDTO(OrganizationAttribute entity, boolean isDeep) {
		return convert(entity, isDeep, OrganizationAttribute.class);
	}

	@Override
	public OrganizationAttributeEntity convertToEntity(OrganizationAttribute entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, OrganizationAttributeEntity.class);
	}

	@Override
	public OrganizationAttribute convertToDTO(OrganizationAttributeEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, OrganizationAttribute.class);
	}

	@Override
	public List<OrganizationAttributeEntity> convertToEntityList(List<OrganizationAttribute> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, OrganizationAttributeEntity.class);
	}

	@Override
	public List<OrganizationAttribute> convertToDTOList(List<OrganizationAttributeEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, OrganizationAttribute.class);
	}
}
