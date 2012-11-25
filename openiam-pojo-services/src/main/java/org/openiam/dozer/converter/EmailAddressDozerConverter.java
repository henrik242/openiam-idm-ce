package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.springframework.stereotype.Component;

@Component("emailAddressDozerMapper")
public class EmailAddressDozerConverter extends AbstractDozerEntityConverter<EmailAddress, EmailAddressEntity> {

    @Override
	public EmailAddressEntity convertEntity(EmailAddressEntity entity, boolean isDeep) {
		return convert(entity, isDeep, EmailAddressEntity.class);
	}

	@Override
	public EmailAddress convertDTO(EmailAddress entity, boolean isDeep) {
		return convert(entity, isDeep, EmailAddress.class);
	}

	@Override
	public EmailAddressEntity convertToEntity(EmailAddress entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, EmailAddressEntity.class);
	}

	@Override
	public EmailAddress convertToDTO(EmailAddressEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, EmailAddress.class);
	}

	@Override
	public List<EmailAddressEntity> convertToEntityList(List<EmailAddress> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, EmailAddressEntity.class);
	}

	@Override
	public List<EmailAddress> convertToDTOList(List<EmailAddressEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, EmailAddress.class);
	}

}
