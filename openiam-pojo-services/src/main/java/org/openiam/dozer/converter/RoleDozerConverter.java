package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.role.domain.RoleEntity;
import org.openiam.idm.srvc.role.dto.Role;
import org.springframework.stereotype.Component;

@Component("roleDozerMapper")
public class RoleDozerConverter extends AbstractDozerEntityConverter<Role, RoleEntity> {

    @Override
	public RoleEntity convertEntity(RoleEntity entity, boolean isDeep) {
		return convert(entity, isDeep, RoleEntity.class);
	}

	@Override
	public Role convertDTO(Role entity, boolean isDeep) {
		return convert(entity, isDeep, Role.class);
	}

	@Override
	public RoleEntity convertToEntity(Role entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, RoleEntity.class);
	}

	@Override
	public Role convertToDTO(RoleEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Role.class);
	}

	@Override
	public List<RoleEntity> convertToEntityList(List<Role> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, RoleEntity.class);
	}

	@Override
	public List<Role> convertToDTOList(List<RoleEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Role.class);
	}

}
