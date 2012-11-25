package org.openiam.dozer;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.AddressDozerConverter;
import org.openiam.idm.srvc.continfo.domain.AddressEntity;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestAddressCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private AddressDozerConverter addressConverter;

    @Test
    public void testShallowAddressConversion() {
        final AddressEntity entity = createSimpleAddressEntity();
        final Address address = addressConverter.convertToDTO(entity, false);
        Assert.assertNull(address.getParentId());
        confirmSimple(address, entity, false);
        final AddressEntity convertedEntity = addressConverter.convertToEntity(address, false);
        Assert.assertNull(convertedEntity.getParent());
        confirmSimple(address, convertedEntity, false);
    }

    @Test
    public void testDeepAddressConversion() {
        final AddressEntity entity = createSimpleAddressEntity();
        final Address address = addressConverter.convertToDTO(entity, true);
        confirmSimple(address, entity, true);
        final AddressEntity convertedEntity = addressConverter.convertToEntity(address, true);
        confirmSimple(address, convertedEntity, true);
    }

    private void confirmSimple(final Address address, final AddressEntity entity, boolean isDeep) {
        Assert.assertEquals(address.getAddressId(), entity.getAddressId());
        Assert.assertEquals(address.getAddress1(), entity.getAddress1());
        Assert.assertEquals(address.getAddress2(), entity.getAddress2());
        Assert.assertEquals(address.getAddress3(), entity.getAddress3());
        Assert.assertEquals(address.getAddress4(), entity.getAddress4());
        Assert.assertEquals(address.getAddress5(), entity.getAddress5());
        Assert.assertEquals(address.getAddress6(), entity.getAddress6());
        Assert.assertEquals(address.getAddress7(), entity.getAddress7());
        Assert.assertEquals(address.getName(), entity.getName());
        Assert.assertEquals(address.getBldgNumber(), entity.getBldgNumber());
        Assert.assertEquals(address.getCity(), entity.getCity());
        Assert.assertEquals(address.getCountry(), entity.getCountry());
        Assert.assertEquals(address.getDescription(), entity.getDescription());
        Assert.assertEquals(address.getIsDefault(), entity.getDefault());
        Assert.assertEquals(address.getOperation(), entity.getOperation());
        Assert.assertEquals(address.getParentType(), entity.getParentType());
        if(isDeep) {
            Assert.assertEquals(address.getParentId(), entity.getParent().getUserId());
        }
        Assert.assertEquals(address.getPostalCd(), entity.getPostalCd());
        Assert.assertEquals(address.getState(), entity.getState());
        Assert.assertEquals(address.getStreetDirection(), entity.getStreetDirection());
        Assert.assertEquals(address.getSuite(), entity.getSuite());
    }

    private AddressEntity createSimpleAddressEntity() {
        final AddressEntity entity = new AddressEntity();
        entity.setActive(Boolean.FALSE);
        entity.setAddress1(rs(5));
        entity.setAddress2(rs(5));
        entity.setAddress3(rs(5));
        entity.setAddress4(rs(5));
        entity.setAddress5(rs(5));
        entity.setAddress6(rs(5));
        entity.setAddress7(rs(5));

        entity.setAddressId(rs(5));
        entity.setBldgNumber(rs(5));
        entity.setCity(rs(5));
        entity.setCountry(rs(5));
        entity.setDescription(rs(5));
        entity.setName(rs(5));
        UserEntity parent = new UserEntity();
        parent.setUserId(rs(5));
        parent.setNickname(rs(5));
        entity.setParent(parent);

        entity.setParentType(rs(5));
        entity.setPostalCd(rs(5));
        entity.setState(rs(4));
        entity.setStreetDirection(rs(5));
        entity.setSuite(rs(5));
        entity.setDefault(1);

        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
