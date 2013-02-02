package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.ReportCriteriaParamDozerConverter;
import org.openiam.idm.srvc.report.domain.ReportCriteriaParamEntity;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestReportCriteriaParamCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReportCriteriaParamDozerConverter criteriaParamDozerConverter;

    @Test
    public void testConversion() {
        final ReportCriteriaParamEntity entity = createSimpleEntity();
        final ReportCriteriaParamDto resource = criteriaParamDozerConverter.convertToDTO(entity, false);
        confirmSimple(resource, entity);
        final ReportCriteriaParamEntity convertedEntity = criteriaParamDozerConverter.convertToEntity(resource, false);
        confirmSimple(resource, convertedEntity);
    }

    private ReportCriteriaParamEntity createSimpleEntity() {
        final ReportCriteriaParamEntity entity = new ReportCriteriaParamEntity();
        entity.setId(rs(4));
        ReportInfoEntity report = new ReportInfoEntity();
        report.setId(rs(4));
        entity.setReport(report);
        entity.setName(rs(4));
        entity.setValue(rs(4));
        return entity;
    }

    private void confirmSimple(final ReportCriteriaParamDto dto, final ReportCriteriaParamEntity entity) {
        Assert.assertEquals(dto.getId(), entity.getId());
        Assert.assertEquals(dto.getName(), entity.getName());
        Assert.assertEquals(dto.getValue(), entity.getValue());
        Assert.assertEquals(dto.getReportId(), entity.getReport().getId());
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
