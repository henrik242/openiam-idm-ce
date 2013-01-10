package org.openiam.dozer;

import junit.framework.Assert;
import org.openiam.idm.srvc.pswd.dto.PasswordHistory;
import org.openiam.dozer.converter.PasswordHistoryDozerConverter;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.EmailAddressDozerConverter;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.openiam.idm.srvc.continfo.domain.PasswordHistoryEntity;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestGroup;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestPasswordHistoryCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private PasswordHistoryDozerConverter passwordHistoryConverter;

    @Test
    public void testShallowQuestGroupConversion() {
        final PasswordHistoryEntity entity = createSimpleAddressEntity();
        final PasswordHistory address = passwordHistoryConverter.convertToDTO(entity, false);
        checkConversion(address, entity, false);
        //Assert.assertNull(address.getParent());
        final PasswordHistoryEntity convertedEntity = passwordHistoryConverter.convertToEntity(address, false);
        checkConversion(address, convertedEntity, false);
        // Assert.assertNull(convertedEntity.getParent());
    }

    @Test
    public void testDeepQuestGroupConversion() {
        final PasswordHistoryEntity entity = createSimpleAddressEntity();
        final PasswordHistory address = passwordHistoryConverter.convertToDTO(entity, true);
        checkConversion(address, entity, true);
        final PasswordHistoryEntity convertedEntity = passwordHistoryConverter.convertToEntity(address, true);
        checkConversion(address, convertedEntity, true);
    }

    private void checkConversion(final PasswordHistory address, final PasswordHistoryEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getPwdHistoryId(), entity.getPwdHistoryId());
        Assert.assertEquals(address.getLogin(), entity.getLogin());
        Assert.assertEquals(address.getServiceId(), entity.getServiceId());
        Assert.assertEquals(address.getManagedSysId(), entity.getManagedSysId());
        Assert.assertEquals(address.getDateCreated(), entity.getDateCreated());
        Assert.assertEquals(address.getPassword(), entity.getPassword());
        if(isDeep) {
           // Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
        //Assert.assertEquals(address.getParentType(), entity.getParentType());
    }

    private PasswordHistoryEntity createSimpleAddressEntity() {
        final PasswordHistoryEntity entity = new PasswordHistoryEntity();
        entity.setPwdHistoryId(rs(5));
        entity.setLogin(rs(5));
        entity.setServiceId(rs(5));
        entity.setManagedSysId(rs(5));
        entity.setDateCreated(new java.util.Date());
        entity.setPassword(rs(5));
        
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

}
