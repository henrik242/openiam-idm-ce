package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.continfo.domain.PhoneEntity;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.springframework.stereotype.Component;

@Component("phoneDozerMapper")
public class PhoneDozerConverter extends AbstractDozerEntityConverter<Phone, PhoneEntity> {

    @Override
	public PhoneEntity convertEntity(PhoneEntity entity, boolean isDeep) {
		return convert(entity, isDeep, PhoneEntity.class);
	}

	@Override
	public Phone convertDTO(Phone entity, boolean isDeep) {
		return convert(entity, isDeep, Phone.class);
	}

	@Override
	public PhoneEntity convertToEntity(Phone entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, PhoneEntity.class);
	}

	@Override
	public Phone convertToDTO(PhoneEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Phone.class);
	}

	@Override
	public List<PhoneEntity> convertToEntityList(List<Phone> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, PhoneEntity.class);
	}

	@Override
	public List<Phone> convertToDTOList(List<PhoneEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Phone.class);
	}

}
