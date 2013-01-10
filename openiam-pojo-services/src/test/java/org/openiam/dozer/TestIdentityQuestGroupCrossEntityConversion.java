package org.openiam.dozer;

import java.util.Date;
import java.util.Set;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.EmailAddressDozerConverter;
import org.openiam.dozer.converter.IdentityQuestGroupDozerConverter;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestGroup;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestIdentityQuestGroupCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private IdentityQuestGroupDozerConverter identityQuestGroupConverter;

    @Test
    public void testShallowQuestGroupConversion() {
        final IdentityQuestGroupEntity entity = createSimpleAddressEntity();
        final IdentityQuestGroup address = identityQuestGroupConverter.convertToDTO(entity, false);
        checkConversion(address, entity, false);
        //Assert.assertNull(address.getParent());
        final IdentityQuestGroupEntity convertedEntity = identityQuestGroupConverter.convertToEntity(address, false);
        checkConversion(address, convertedEntity, false);
        // Assert.assertNull(convertedEntity.getParent());
    }

    @Test
    public void testDeepQuestGroupConversion() {
        final IdentityQuestGroupEntity entity = createSimpleAddressEntity();
        final IdentityQuestGroup address = identityQuestGroupConverter.convertToDTO(entity, true);
        checkConversion(address, entity, true);
        final IdentityQuestGroupEntity convertedEntity = identityQuestGroupConverter.convertToEntity(address, true);
        checkConversion(address, convertedEntity, true);
    }

    private void checkConversion(final IdentityQuestGroup address, final IdentityQuestGroupEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getName(), entity.getName());
        Assert.assertEquals(address.getIdentityQuestGrpId(), entity.getIdentityQuestGrpId());
        Assert.assertEquals(address.getStatus(), entity.getStatus());
        Assert.assertEquals(address.getCompanyOwnerId(), entity.getCompanyOwnerId());
        Assert.assertEquals(address.getCreateDate(), entity.getCreateDate());
        Assert.assertEquals(address.getCreatedBy(), entity.getCreatedBy());
        Assert.assertEquals(address.getLastUpdate(), entity.getLastUpdate());
        Assert.assertEquals(address.getLastUpdatedBy(), entity.getLastUpdatedBy());
        Assert.assertEquals(address.getIdentityQuestions(), entity.getIdentityQuestions());
        
        if(isDeep) {
           // Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
       // Assert.assertEquals(address.getParentType(), entity.getParentType());
    }

    private IdentityQuestGroupEntity createSimpleAddressEntity() {
        final IdentityQuestGroupEntity entity = new IdentityQuestGroupEntity();
        
        entity.setName(rs(5));
        entity.setIdentityQuestGrpId(rs(5));
        entity.setCompanyOwnerId(rs(5));
        entity.setCreateDate(new Date());
        entity.setCreatedBy(rs(5));
        entity.setLastUpdate(new Date());
        entity.setStatus(rs(5));
        entity.setLastUpdatedBy(rs(5));
        entity.setIdentityQuestions(null);
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
