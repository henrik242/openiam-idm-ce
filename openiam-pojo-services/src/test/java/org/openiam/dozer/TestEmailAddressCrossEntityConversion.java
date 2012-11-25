package org.openiam.dozer;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.EmailAddressDozerConverter;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestEmailAddressCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private EmailAddressDozerConverter emailAddressConverter;


    @Test
    public void testShallowAddressConversion() {
        final EmailAddressEntity entity = createSimpleAddressEntity();
        final EmailAddress address = emailAddressConverter.convertToDTO(entity, false);
        checkConversion(address, entity, false);
        Assert.assertNull(address.getParentId());
        final EmailAddressEntity convertedEntity = emailAddressConverter.convertToEntity(address, false);
        checkConversion(address, convertedEntity, false);
        Assert.assertNull(convertedEntity.getParent());
    }

    @Test
    public void testDeepAddressConversion() {
        final EmailAddressEntity entity = createSimpleAddressEntity();
        final EmailAddress address = emailAddressConverter.convertToDTO(entity, true);
        checkConversion(address, entity, true);
        final EmailAddressEntity convertedEntity = emailAddressConverter.convertToEntity(address, true);
        checkConversion(address, convertedEntity, true);
    }

    private void checkConversion(final EmailAddress address, final EmailAddressEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getDescription(), entity.getDescription());
        Assert.assertEquals(address.getEmailAddress(), entity.getEmailAddress());
        Assert.assertEquals(address.getEmailId(), entity.getEmailId());
        Assert.assertEquals(address.getIsDefault(), entity.getDefault());
        Assert.assertEquals(address.getName(), entity.getName());
        Assert.assertEquals(address.getOperation(), entity.getOperation());
        if(isDeep) {
            Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
        Assert.assertEquals(address.getParentType(), entity.getParentType());
    }

    private EmailAddressEntity createSimpleAddressEntity() {
        final EmailAddressEntity entity = new EmailAddressEntity();
        entity.setActive(Boolean.TRUE);
        entity.setDefault(1);
        entity.setDescription(rs(5));
        entity.setEmailAddress(rs(5));
        entity.setEmailId(rs(5));
        entity.setName(rs(5));
        UserEntity parent = new UserEntity();
        parent.setUserId(rs(5));
        parent.setNickname(rs(5));
        entity.setParent(parent);
        entity.setParentType(rs(5));
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
