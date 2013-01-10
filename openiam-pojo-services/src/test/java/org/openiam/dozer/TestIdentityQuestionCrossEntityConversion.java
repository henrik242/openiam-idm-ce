package org.openiam.dozer;

import junit.framework.Assert;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.EmailAddressDozerConverter;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestionEntity;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.dozer.converter.IdentityQuestionDozerConverter;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestIdentityQuestionCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private IdentityQuestionDozerConverter identityQuestionConverter;

    @Test
    public void testShallowQuestGroupConversion() {
        final IdentityQuestionEntity entity = createSimpleAddressEntity();
        final IdentityQuestion address = identityQuestionConverter.convertToDTO(entity, false);
        checkConversion(address, entity, false);
        //Assert.assertNull(address.getParent());
        final IdentityQuestionEntity convertedEntity = identityQuestionConverter.convertToEntity(address, false);
        checkConversion(address, convertedEntity, false);
        // Assert.assertNull(convertedEntity.getParent());
    }

    @Test
    public void testDeepQuestGroupConversion() {
        final IdentityQuestionEntity entity = createSimpleAddressEntity();
        final IdentityQuestion address = identityQuestionConverter.convertToDTO(entity, true);
        checkConversion(address, entity, true);
        final IdentityQuestionEntity convertedEntity = identityQuestionConverter.convertToEntity(address, true);
        checkConversion(address, convertedEntity, true);
    }

    private void checkConversion(final IdentityQuestion address, final IdentityQuestionEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getIdentityQuestionId(), entity.getIdentityQuestionId());
        Assert.assertEquals(address.getIdentityQuestGrp(), entity.getIdentityQuestGrp());
        Assert.assertEquals(address.getQuestionText(), entity.getQuestionText());
        Assert.assertEquals(address.getRequired(), entity.getRequired());
        Assert.assertEquals(address.getUserId(), entity.getUserId());
        Assert.assertEquals(address.getActive(), entity.getActive());
        if(isDeep) {
           // Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
     //   Assert.assertEquals(address.getParentType(), entity.getParentType());
    }

    private IdentityQuestionEntity createSimpleAddressEntity() {
        final IdentityQuestionEntity entity = new IdentityQuestionEntity();
        entity.setIdentityQuestionId(rs(5));
        entity.setIdentityQuestGrp(null);
        entity.setQuestionText(rs(5));
        entity.setRequired(1);
        entity.setUserId(rs(5));
        entity.setActive(1);
        
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
