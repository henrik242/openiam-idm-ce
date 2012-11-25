package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.Group;
import org.springframework.stereotype.Component;

@Component("groupDozerMapper")
public class GroupDozerConverter extends AbstractDozerEntityConverter<Group, GroupEntity> {

    @Override
	public GroupEntity convertEntity(GroupEntity entity, boolean isDeep) {
		return convert(entity, isDeep, GroupEntity.class);
	}

	@Override
	public Group convertDTO(Group entity, boolean isDeep) {
		return convert(entity, isDeep, Group.class);
	}

	@Override
	public GroupEntity convertToEntity(Group entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, GroupEntity.class);
	}

	@Override
	public Group convertToDTO(GroupEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Group.class);
	}

	@Override
	public List<GroupEntity> convertToEntityList(List<Group> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, GroupEntity.class);
	}

	@Override
	public List<Group> convertToDTOList(List<GroupEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Group.class);
	}
}
