package org.openiam.dozer;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.UserIdentityAnswerDozerConverter;
import org.openiam.dozer.converter.UserIdentityAnswerDozerConverter;
import org.openiam.idm.srvc.pswd.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.pswd.domain.UserIdentityAnswerEntity;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestGroup;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestUserIdentityAnswerCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserIdentityAnswerDozerConverter userIdentityAnswerConverter;

    @Test
    public void testShallowQuestGroupConversion() {
        final UserIdentityAnswerEntity entity = createSimpleAddressEntity();
        final UserIdentityAnswer address = userIdentityAnswerConverter.convertToDTO(entity, false);
        checkConversion(address, entity, false);
        //Assert.assertNull(address.getParent());
        final UserIdentityAnswerEntity convertedEntity = userIdentityAnswerConverter.convertToEntity(address, false);
        checkConversion(address, convertedEntity, false);
        // Assert.assertNull(convertedEntity.getParent());
    }

    @Test
    public void testDeepQuestGroupConversion() {
        final UserIdentityAnswerEntity entity = createSimpleAddressEntity();
        final UserIdentityAnswer address = userIdentityAnswerConverter.convertToDTO(entity, true);
        checkConversion(address, entity, true);
        final UserIdentityAnswerEntity convertedEntity = userIdentityAnswerConverter.convertToEntity(address, true);
        checkConversion(address, convertedEntity, true);
    }

    private void checkConversion(final UserIdentityAnswer address, final UserIdentityAnswerEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getIdentityAnsId(), entity.getIdentityAnsId());
        Assert.assertEquals(address.getQuestionText(), entity.getQuestionText());
        //Assert.assertEquals(address.getRequired(), entity.getRequired());
        Assert.assertEquals(address.getUserId(), entity.getUserId());
        Assert.assertEquals(address.getQuestionAnswer(), entity.getQuestionAnswer());
        Assert.assertEquals(address.getIdentityQuestionId(), entity.getIdentityQuestionId());
        
        if(isDeep) {
            //Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
        //Assert.assertEquals(address.getParentType(), entity.getParentType());
    }

    private UserIdentityAnswerEntity createSimpleAddressEntity() {
        final UserIdentityAnswerEntity entity = new UserIdentityAnswerEntity();
        entity.setIdentityAnsId(rs(5));
        entity.setQuestionText(rs(5));
        //entity.setRequired(rs(5));
        entity.setUserId(rs(5));
        entity.setQuestionAnswer(rs(5));
        entity.setIdentityQuestionId(rs(5));
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
