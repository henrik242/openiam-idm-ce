package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.SupervisorDozerConverter;
import org.openiam.idm.srvc.user.domain.SupervisorEntity;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestSupervisorCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private SupervisorDozerConverter supervisorDozerConverter;

    @Test
    public void testSupervisorShallowConversion() {
        final SupervisorEntity entity = createSimpleSupervisorEntity();
        final Supervisor supervisor = supervisorDozerConverter.convertToDTO(entity, false);
        checkConversion(supervisor, entity, false);
        final SupervisorEntity convertedEntity = supervisorDozerConverter.convertToEntity(supervisor, false);
        checkConversion(supervisor, convertedEntity, false);
    }

    @Test
    public void testSupervisorDeepConversion() {
        final SupervisorEntity entity = createSimpleSupervisorEntity();
        final Supervisor supervisor = supervisorDozerConverter.convertToDTO(entity, true);
        checkConversion(supervisor, entity, true);
        final SupervisorEntity convertedEntity = supervisorDozerConverter.convertToEntity(supervisor, true);
        checkConversion(supervisor, convertedEntity, true);
    }

    private SupervisorEntity createSimpleSupervisorEntity() {
        final SupervisorEntity entity = new SupervisorEntity();
        entity.setOrgStructureId(rs(2));
        entity.setComments(rs(2));
        entity.setEndDate(new Date());
        entity.setPrimarySuper(1);
        entity.setStartDate(new Date());
        entity.setStatus(rs(2));
        entity.setSupervisorType(rs(3));
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(rs(5));
        entity.setSupervisor(userEntity);
        UserEntity employer = new UserEntity();
        employer.setUserId(rs(5));
        entity.setEmployee(employer);
        return entity;
    }

    private void checkConversion(final Supervisor supervisor, final SupervisorEntity entity, boolean isDeep) {
        Assert.assertEquals(supervisor.getComments(), entity.getComments());
        if(isDeep) {
            Assert.assertEquals(supervisor.getEmployee().getUserId(), entity.getEmployee().getUserId());
        } else {
            Assert.assertNull(supervisor.getEmployee());
        }
        Assert.assertEquals(supervisor.getEndDate(), entity.getEndDate());
        Assert.assertEquals(supervisor.getIsPrimarySuper(), entity.getPrimarySuper());
        Assert.assertEquals(supervisor.getOrgStructureId(), entity.getOrgStructureId());
        Assert.assertEquals(supervisor.getStartDate(), entity.getStartDate());
        Assert.assertEquals(supervisor.getStatus(), entity.getStatus());
        if(isDeep) {
            Assert.assertEquals(supervisor.getSupervisor().getUserId(), entity.getSupervisor().getUserId());
        } else {
            Assert.assertNull(supervisor.getSupervisor());
        }
        Assert.assertEquals(supervisor.getSupervisorType(), entity.getSupervisorType());
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
