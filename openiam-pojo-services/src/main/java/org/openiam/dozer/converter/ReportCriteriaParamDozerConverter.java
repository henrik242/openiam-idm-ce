package org.openiam.dozer.converter;

import org.openiam.idm.srvc.report.domain.ReportCriteriaParamEntity;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("reportCriteriaParamDozerMapper")
public class ReportCriteriaParamDozerConverter extends AbstractDozerEntityConverter<ReportCriteriaParamDto, ReportCriteriaParamEntity>{

    @Override
    public ReportCriteriaParamEntity convertEntity(ReportCriteriaParamEntity entity, boolean isDeep) {
        return convert(entity, isDeep, ReportCriteriaParamEntity.class);
    }

    @Override
    public ReportCriteriaParamDto convertDTO(ReportCriteriaParamDto entity, boolean isDeep) {
        return convert(entity, isDeep, ReportCriteriaParamDto.class);
    }

    @Override
    public ReportCriteriaParamEntity convertToEntity(ReportCriteriaParamDto entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, ReportCriteriaParamEntity.class);
    }

    @Override
    public ReportCriteriaParamDto convertToDTO(ReportCriteriaParamEntity entity, boolean isDeep) {
        return convertToCrossEntity(entity, isDeep, ReportCriteriaParamDto.class);
    }

    @Override
    public List<ReportCriteriaParamEntity> convertToEntityList(List<ReportCriteriaParamDto> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, ReportCriteriaParamEntity.class);
    }

    @Override
    public List<ReportCriteriaParamDto> convertToDTOList(List<ReportCriteriaParamEntity> list, boolean isDeep) {
        return convertListToCrossEntity(list, isDeep, ReportCriteriaParamDto.class);
    }
}
