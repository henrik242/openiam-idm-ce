package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.converter.OrganizationDozerConverter;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.dto.OrgClassificationEnum;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.dto.OrganizationAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestOrganizationCrossEntityConversion  extends AbstractTestNGSpringContextTests {
    @Autowired
    private OrganizationDozerConverter organizationDozerConverter;


    @Test
    public void testOrganizationConversion() {
        final OrganizationEntity entity = createSimpleOrganizationEntity();
        OrganizationAttributeEntity attributeEntity = new OrganizationAttributeEntity();
        attributeEntity.setAttrId(rs(4));
        attributeEntity.setMetadataElementId(rs(4));
        attributeEntity.setName(rs(4));
        attributeEntity.setValue(rs(5));
        attributeEntity.setOrganization(createSimpleOrganizationEntity());
        Map<String,OrganizationAttributeEntity> entityMap = new HashMap<String,OrganizationAttributeEntity>();
        entityMap.put(rs(2),attributeEntity);
        attributeEntity = new OrganizationAttributeEntity();
        attributeEntity.setAttrId(rs(4));
        attributeEntity.setMetadataElementId(rs(4));
        attributeEntity.setName(rs(4));
        attributeEntity.setValue(rs(5));
        attributeEntity.setOrganization(createSimpleOrganizationEntity());
        entityMap.put(rs(2),attributeEntity);
        entity.setAttributes(entityMap);

        final Organization organization = organizationDozerConverter.convertToDTO(entity, true);
        checkConversion(organization, entity);
        checkAttributesConversion(organization, entity);
        final OrganizationEntity convertedEntity = organizationDozerConverter.convertToEntity(organization, true);
        checkConversion(organization, convertedEntity);
        checkAttributesConversion(organization, entity);
    }

    private void checkAttributesConversion(Organization organization, OrganizationEntity entity){
        for(Map.Entry<String, OrganizationAttribute> attrEntry : organization.getAttributes().entrySet()) {
            OrganizationAttribute organizationAttribute = attrEntry.getValue();
            OrganizationAttributeEntity attributeEntity = entity.getAttributes().get(attrEntry.getKey());
            Assert.assertNotNull(attributeEntity);
            Assert.assertEquals(attributeEntity.getAttrId(), organizationAttribute.getAttrId());
            Assert.assertEquals(attributeEntity.getMetadataElementId(), organizationAttribute.getMetadataElementId());
            Assert.assertEquals(attributeEntity.getName(), organizationAttribute.getName());
            Assert.assertEquals(attributeEntity.getValue(), organizationAttribute.getValue());
            Assert.assertEquals(attributeEntity.getOrganization().getOrgId(), organizationAttribute.getOrganizationId());
        }
    }

    private void checkConversion(Organization organization, OrganizationEntity entity) {
        Assert.assertEquals(organization.getOrgId(), entity.getOrgId());
        Assert.assertEquals(organization.getAbbreviation(), entity.getAbbreviation());
        Assert.assertEquals(organization.getAlias(), entity.getAlias());
        Assert.assertEquals(organization.getClassification(), entity.getClassification());
        Assert.assertEquals(organization.getCreateDate(), entity.getCreateDate());
        Assert.assertEquals(organization.getCreatedBy(), entity.getCreatedBy());
        Assert.assertEquals(organization.getDescription(), entity.getDescription());
        Assert.assertEquals(organization.getDomainName(), entity.getDomainName());
        Assert.assertEquals(organization.getInternalOrgId(), entity.getInternalOrgId());
        Assert.assertEquals(organization.getAbbreviation(), entity.getAbbreviation());
        Assert.assertEquals(organization.getLdapStr(), entity.getLdapStr());
        Assert.assertEquals(organization.getLstUpdate(), entity.getLstUpdate());
        Assert.assertEquals(organization.getLstUpdatedBy(), entity.getLstUpdatedBy());
        Assert.assertEquals(organization.getMetadataTypeId(), entity.getMetadataTypeId());
        Assert.assertEquals(organization.getOperation(), entity.getOperation());
        Assert.assertEquals(organization.getOrganizationName(), entity.getOrganizationName());
        Assert.assertEquals(organization.getParentId(), entity.getParentId());
        Assert.assertEquals(organization.getSelected(), entity.getSelected());
        Assert.assertEquals(organization.getStatus(), entity.getStatus());
        Assert.assertEquals(organization.getSymbol(), entity.getSymbol());
    }

    private OrganizationEntity createSimpleOrganizationEntity() {
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setOrgId(rs(4));
        organizationEntity.setAbbreviation(rs(4));
        organizationEntity.setAlias(rs(4));
        organizationEntity.setCreateDate(new Date());
        organizationEntity.setCreatedBy(rs(4));
        organizationEntity.setDescription(rs(4));
        organizationEntity.setDomainName(rs(3));
        organizationEntity.setInternalOrgId(rs(4));
        organizationEntity.setLdapStr(rs(5));
        organizationEntity.setLstUpdate(new Date());
        organizationEntity.setLstUpdatedBy(rs(4));
        organizationEntity.setMetadataTypeId(rs(4));
        organizationEntity.setOrganizationName(rs(5));
        organizationEntity.setParentId(rs(4));
        organizationEntity.setStatus(rs(3));
        organizationEntity.setSymbol(rs(3));
        organizationEntity.setClassification(OrgClassificationEnum.AGENCY);
        organizationEntity.setOperation(AttributeOperationEnum.ADD);
        organizationEntity.setSelected(Boolean.TRUE);

        return organizationEntity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
