package org.openiam.dozer.converter;

import java.util.List;
import java.util.Map;

import org.openiam.idm.srvc.continfo.domain.AddressEntity;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.openiam.idm.srvc.continfo.domain.PhoneEntity;
import org.openiam.idm.srvc.user.domain.UserAttributeEntity;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.domain.UserNoteEntity;
import org.openiam.idm.srvc.user.dto.User;
import org.springframework.stereotype.Component;

@Component("userDozerMapper")
public class UserDozerConverter extends AbstractDozerEntityConverter<User, UserEntity> {

    @Override
	public UserEntity convertEntity(UserEntity entity, boolean isDeep) {
		return convert(entity, isDeep, UserEntity.class);
	}

	@Override
	public User convertDTO(User entity, boolean isDeep) {
		return convert(entity, isDeep, User.class);
	}

	@Override
	public UserEntity convertToEntity(User entity, boolean isDeep) {
        UserEntity userEntity = convertToCrossEntity(entity, isDeep, UserEntity.class);
        for(EmailAddressEntity emailAddressEntity : userEntity.getEmailAddresses()) {
            emailAddressEntity.setParent(userEntity);
        }
        for(AddressEntity addressEntity : userEntity.getAddresses()) {
            addressEntity.setParent(userEntity);
        }
        for(PhoneEntity phoneEntity : userEntity.getPhones()) {
            phoneEntity.setParent(userEntity);
        }
        for(UserNoteEntity userNoteEntity : userEntity.getUserNotes()) {
            userNoteEntity.setUser(userEntity);
        }
        for(Map.Entry<String, UserAttributeEntity> attributeEntityEntry : userEntity.getUserAttributes().entrySet()) {
            attributeEntityEntry.getValue().setUser(userEntity);
        }
        return userEntity;
	}

	@Override
	public User convertToDTO(UserEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, User.class);
	}

	@Override
	public List<UserEntity> convertToEntityList(List<User> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserEntity.class);
	}

	@Override
	public List<User> convertToDTOList(List<UserEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, User.class);
	}

}
