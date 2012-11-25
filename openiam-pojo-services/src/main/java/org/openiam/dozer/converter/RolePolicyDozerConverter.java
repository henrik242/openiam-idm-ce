package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.role.domain.RolePolicyEntity;
import org.openiam.idm.srvc.role.dto.RolePolicy;
import org.springframework.stereotype.Component;

@Component("rolePolicyDozerMapper")
public class RolePolicyDozerConverter extends AbstractDozerEntityConverter<RolePolicy, RolePolicyEntity> {

    @Override
	public RolePolicyEntity convertEntity(RolePolicyEntity entity, boolean isDeep) {
		return convert(entity, isDeep, RolePolicyEntity.class);
	}

	@Override
	public RolePolicy convertDTO(RolePolicy entity, boolean isDeep) {
		return convert(entity, isDeep, RolePolicy.class);
	}

	@Override
	public RolePolicyEntity convertToEntity(RolePolicy entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, RolePolicyEntity.class);
	}

	@Override
	public RolePolicy convertToDTO(RolePolicyEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, RolePolicy.class);
	}

	@Override
	public List<RolePolicyEntity> convertToEntityList(List<RolePolicy> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, RolePolicyEntity.class);
	}

	@Override
	public List<RolePolicy> convertToDTOList(List<RolePolicyEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, RolePolicy.class);
	}


}
