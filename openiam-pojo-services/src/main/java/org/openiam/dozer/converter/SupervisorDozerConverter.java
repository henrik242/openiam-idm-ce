package org.openiam.dozer.converter;

import java.util.List;
import org.openiam.idm.srvc.user.domain.SupervisorEntity;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.springframework.stereotype.Component;

@Component("supervisorDozerMapper")
public class SupervisorDozerConverter extends AbstractDozerEntityConverter<Supervisor, SupervisorEntity> {

    @Override
	public SupervisorEntity convertEntity(SupervisorEntity entity, boolean isDeep) {
		return convert(entity, isDeep, SupervisorEntity.class);
	}

	@Override
	public Supervisor convertDTO(Supervisor entity, boolean isDeep) {
		return convert(entity, isDeep, Supervisor.class);
	}

	@Override
	public SupervisorEntity convertToEntity(Supervisor entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, SupervisorEntity.class);
	}

	@Override
	public Supervisor convertToDTO(SupervisorEntity entity, boolean isDeep) {
		return convertToCrossEntity(entity, isDeep, Supervisor.class);
	}

	@Override
	public List<SupervisorEntity> convertToEntityList(List<Supervisor> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, SupervisorEntity.class);
	}

	@Override
	public List<Supervisor> convertToDTOList(List<SupervisorEntity> list, boolean isDeep) {
		return convertListToCrossEntity(list, isDeep, Supervisor.class);
	}


}
