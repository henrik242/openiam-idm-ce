package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.org.domain.UserAffiliationEntity;
import org.openiam.idm.srvc.org.dto.UserAffiliation;
import org.springframework.stereotype.Component;

@Component("userAffiliationDozerConverter")
    public class UserAffiliationDozerConverter extends AbstractDozerEntityConverter<UserAffiliation, UserAffiliationEntity> {

        @Override
	public UserAffiliationEntity convertEntity(UserAffiliationEntity entity, boolean isDeep) {
		return convert(entity, isDeep, UserAffiliationEntity.class);
	}

	@Override
	public UserAffiliation convertDTO(UserAffiliation entity, boolean isDeep) {
		return convert(entity, isDeep, UserAffiliation.class);
	}

	@Override
	public UserAffiliationEntity convertToEntity(UserAffiliation entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserAffiliationEntity.class);
	}

	@Override
	public UserAffiliation convertToDTO(UserAffiliationEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, UserAffiliation.class);
	}

	@Override
	public List<UserAffiliationEntity> convertToEntityList(List<UserAffiliation> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserAffiliationEntity.class);
	}

	@Override
	public List<UserAffiliation> convertToDTOList(List<UserAffiliationEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, UserAffiliation.class);
	}

}
