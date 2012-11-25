package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.user.domain.UserNoteEntity;
import org.openiam.idm.srvc.user.dto.UserNote;
import org.springframework.stereotype.Component;

@Component("userNoteDozerMapper")
public class UserNoteDozerConverter extends AbstractDozerEntityConverter<UserNote, UserNoteEntity> {

    @Override
	public UserNoteEntity convertEntity(UserNoteEntity entity, boolean isDeep) {
		return convert(entity, isDeep, UserNoteEntity.class);
	}

	@Override
	public UserNote convertDTO(UserNote entity, boolean isDeep) {
		return convert(entity, isDeep, UserNote.class);
	}

	@Override
	public UserNoteEntity convertToEntity(UserNote entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserNoteEntity.class);
	}

	@Override
	public UserNote convertToDTO(UserNoteEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserNote.class);
	}

	@Override
	public List<UserNoteEntity> convertToEntityList(List<UserNote> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserNoteEntity.class);
	}

	@Override
	public List<UserNote> convertToDTOList(List<UserNoteEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserNote.class);
	}

}
