package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.continfo.domain.AddressEntity;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.springframework.stereotype.Component;

@Component("addressDozerMapper")
public class AddressDozerConverter extends AbstractDozerEntityConverter<Address, AddressEntity> {

    @Override
	public AddressEntity convertEntity(AddressEntity entity, boolean isDeep) {
		return convert(entity, isDeep, AddressEntity.class);
	}

	@Override
	public Address convertDTO(Address entity, boolean isDeep) {
		return convert(entity, isDeep, Address.class);
	}

	@Override
	public AddressEntity convertToEntity(Address entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, AddressEntity.class);
	}

	@Override
	public Address convertToDTO(AddressEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Address.class);
	}

	@Override
	public List<AddressEntity> convertToEntityList(List<Address> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, AddressEntity.class);
	}

	@Override
	public List<Address> convertToDTOList(List<AddressEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Address.class);
	}
}
