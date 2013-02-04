package org.openiam.dozer.converter;

import org.openiam.idm.srvc.report.domain.ReportParamTypeEntity;
import org.openiam.idm.srvc.report.dto.ReportParamTypeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("reportParamTypeDozerMapper")
public class ReportParamTypeDozerConverter extends AbstractDozerEntityConverter<ReportParamTypeDto, ReportParamTypeEntity>{

    @Override
    public ReportParamTypeEntity convertEntity(ReportParamTypeEntity entity, boolean isDeep) {
        return convert(entity, isDeep, ReportParamTypeEntity.class);
    }

    @Override
    public ReportParamTypeDto convertDTO(ReportParamTypeDto entity, boolean isDeep) {
        return convert(entity, isDeep, ReportParamTypeDto.class);
    }

    @Override
    public ReportParamTypeEntity convertToEntity(ReportParamTypeDto entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, ReportParamTypeEntity.class);
    }

    @Override
    public ReportParamTypeDto convertToDTO(ReportParamTypeEntity entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, ReportParamTypeDto.class);
    }

    @Override
    public List<ReportParamTypeEntity> convertToEntityList(List<ReportParamTypeDto> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, ReportParamTypeEntity.class);
    }

    @Override
    public List<ReportParamTypeDto> convertToDTOList(List<ReportParamTypeEntity> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, ReportParamTypeDto.class);
    }
}
