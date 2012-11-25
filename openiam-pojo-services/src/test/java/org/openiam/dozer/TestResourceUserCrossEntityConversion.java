package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.ResourceUserDozerConverter;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourceUserEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.openiam.idm.srvc.res.dto.ResourceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestResourceUserCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private ResourceUserDozerConverter resourceUserDozerConverter;

    @Test
    public void testSupervisorShallowConversion() {
        final ResourceUserEntity entity = createSimpleResourceUserEntity();
        final ResourceUser resourceUser = resourceUserDozerConverter.convertToDTO(entity, true);
        checkConversion(resourceUser, entity);
        final ResourceUserEntity convertedEntity = resourceUserDozerConverter.convertToEntity(resourceUser, true);
        checkConversion(resourceUser, convertedEntity);
    }

    private void checkConversion(final ResourceUser resourceUser,  final ResourceUserEntity entity) {
        Assert.assertEquals(resourceUser.getId().getUserId(), entity.getId().getUserId());
        Assert.assertEquals(resourceUser.getId().getPrivilegeId(), entity.getId().getPrivilegeId());
        Assert.assertEquals(resourceUser.getId().getResourceId(), entity.getId().getResourceId());
        Assert.assertEquals(resourceUser.getResource().getResourceId(), entity.getResource().getResourceId());
    }

    private ResourceUserEntity createSimpleResourceUserEntity() {
        ResourceUserEntity entity = new ResourceUserEntity();
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setResourceId(rs(4));
        entity.setResource(resourceEntity);
        ResourceUserEmbeddableId resourceUserEmbeddableId = new ResourceUserEmbeddableId(rs(3),rs(3),rs(3));
        entity.setId(resourceUserEmbeddableId);
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
