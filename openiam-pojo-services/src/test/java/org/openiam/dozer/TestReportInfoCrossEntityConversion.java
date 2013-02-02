package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.ReportInfoDozerConverter;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;
import org.openiam.idm.srvc.report.dto.ReportInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestReportInfoCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReportInfoDozerConverter reportInfoDozerMapper;

    @Test
    public void testConversion() {
        final ReportInfoEntity entity = createSimpleEntity();
        final ReportInfoDto resource = reportInfoDozerMapper.convertToDTO(entity, false);
        confirmSimple(resource, entity);
        final ReportInfoEntity convertedEntity = reportInfoDozerMapper.convertToEntity(resource, false);
        confirmSimple(resource, convertedEntity);
    }

    private ReportInfoEntity createSimpleEntity() {
        final ReportInfoEntity entity = new ReportInfoEntity();
        entity.setId(rs(4));
        entity.setReportName(rs(4));
        entity.setReportFilePath(rs(4));
        entity.setDatasourceFilePath(rs(4));
        return entity;
    }

    private void confirmSimple(final ReportInfoDto dto, final ReportInfoEntity entity) {
        Assert.assertEquals(dto.getReportId(), entity.getId());
        Assert.assertEquals(dto.getReportDataSource(), entity.getDatasourceFilePath());
        Assert.assertEquals(dto.getReportName(), entity.getReportName());
        Assert.assertEquals(dto.getReportUrl(), entity.getReportFilePath());
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
