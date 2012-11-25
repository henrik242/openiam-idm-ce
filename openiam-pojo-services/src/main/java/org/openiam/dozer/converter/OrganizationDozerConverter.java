package org.openiam.dozer.converter;

import java.util.List;
import java.util.Map;

import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.dto.Organization;
import org.springframework.stereotype.Component;

@Component("organizationDozerMapper")
public class OrganizationDozerConverter extends AbstractDozerEntityConverter<Organization, OrganizationEntity> {

    @Override
	public OrganizationEntity convertEntity(OrganizationEntity entity, boolean isDeep) {
		return convert(entity, isDeep, OrganizationEntity.class);
	}

	@Override
	public Organization convertDTO(Organization entity, boolean isDeep) {
		return convert(entity, isDeep, Organization.class);
	}

	@Override
	public OrganizationEntity convertToEntity(Organization entity, boolean isDeep) {
        OrganizationEntity organizationEntity = convertToCrossEntity(entity, isDeep, OrganizationEntity.class);
        for(Map.Entry<String,OrganizationAttributeEntity> attributeEntity : organizationEntity.getAttributes().entrySet()) {
            attributeEntity.getValue().setOrganization(organizationEntity);
        }
        return organizationEntity;
	}

	@Override
	public Organization convertToDTO(OrganizationEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Organization.class);
	}

	@Override
	public List<OrganizationEntity> convertToEntityList(List<Organization> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, OrganizationEntity.class);
	}

	@Override
	public List<Organization> convertToDTOList(List<OrganizationEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Organization.class);
	}
}
