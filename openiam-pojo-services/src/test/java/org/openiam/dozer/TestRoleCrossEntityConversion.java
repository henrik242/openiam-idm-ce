package org.openiam.dozer;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.converter.RoleDozerConverter;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;
import org.openiam.idm.srvc.role.domain.RoleEmbeddableId;
import org.openiam.idm.srvc.role.domain.RoleEntity;
import org.openiam.idm.srvc.role.domain.RolePolicyEntity;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.openiam.idm.srvc.role.dto.RolePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.*;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestRoleCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private RoleDozerConverter roleDozerConverter;


    @Test
    public void testRoleConversion() {
        final RoleEntity entity = createSimpleRoleEntity();
        List<RoleEntity> children = new LinkedList<RoleEntity>();
        children.add(createSimpleRoleEntity());
        children.add(createSimpleRoleEntity());
        children.add(createSimpleRoleEntity());
        entity.setChildRoles(children);

        Set<GroupEntity> groups = new HashSet<GroupEntity>();
        groups.add(TestGroupCrossEntityConversion.createSimpleGroupEntity());
        groups.add(TestGroupCrossEntityConversion.createSimpleGroupEntity());
        entity.setGroups(groups);
        Set<RoleAttributeEntity> roleAttributeEntities = new HashSet<RoleAttributeEntity>();
        RoleAttributeEntity attributeEntity = new RoleAttributeEntity();
        attributeEntity.setRoleAttrId(rs(4));
        attributeEntity.setAttrGroup(rs(4));
        attributeEntity.setMetadataElementId(rs(4));
        attributeEntity.setName(rs(4));
        attributeEntity.setRoleId(rs(4));
        attributeEntity.setServiceId(rs(4));
        attributeEntity.setValue(rs(5));
        roleAttributeEntities.add(attributeEntity);
        attributeEntity = new RoleAttributeEntity();
        attributeEntity.setRoleAttrId(rs(4));
        attributeEntity.setAttrGroup(rs(4));
        attributeEntity.setMetadataElementId(rs(4));
        attributeEntity.setName(rs(4));
        attributeEntity.setRoleId(rs(4));
        attributeEntity.setServiceId(rs(4));
        attributeEntity.setValue(rs(5));
        roleAttributeEntities.add(attributeEntity);
        entity.setRoleAttributes(roleAttributeEntities);

        Set<RolePolicyEntity> rolePolicies = new HashSet<RolePolicyEntity>();
        RolePolicyEntity policyEntity = new RolePolicyEntity();
        policyEntity.setRoleId(rs(4));
        policyEntity.setAction(rs(5));
        policyEntity.setActionQualifier(rs(4));
        policyEntity.setExecutionOrder(1);
        policyEntity.setName(rs(4));
        policyEntity.setPolicyScript(rs(4));
        policyEntity.setRolePolicyId(rs(4));
        policyEntity.setServiceId(rs(4));
        policyEntity.setValue1(rs(4));
        policyEntity.setValue2(rs(4));

        rolePolicies.add(policyEntity);
        entity.setRolePolicy(rolePolicies);

        final Role role = roleDozerConverter.convertToDTO(entity, true);
        checkConversion(role, entity);
        checkCollectionsConversion(role, entity);

        final RoleEntity convertedEntity = roleDozerConverter.convertToEntity(role, true);
        checkConversion(role, convertedEntity);
    }

    private void checkCollectionsConversion(Role role, RoleEntity entity) {
        Assert.assertEquals(role.getChildRoles().size(), entity.getChildRoles().size());
        for(int i = 0; i < role.getChildRoles().size(); i++) {
            Role child = role.getChildRoles().get(i);
            RoleEntity childEntity = entity.getChildRoles().get(i);
            Assert.assertNotNull(childEntity);
            checkConversion(child, childEntity);
        }
        Assert.assertEquals(role.getGroups().size(), entity.getGroups().size());
        for(int i = 0; i < role.getGroups().size(); i++) {
            Group group = (Group)role.getGroups().toArray()[i];
            GroupEntity groupEntity = (GroupEntity)entity.getGroups().toArray()[i];
            Assert.assertNotNull(groupEntity);
            TestGroupCrossEntityConversion.checkConversion(group, groupEntity, true);
        }
        Assert.assertEquals(role.getRoleAttributes().size(), entity.getRoleAttributes().size());
        for(int i = 0; i < role.getRoleAttributes().size(); i++) {
            RoleAttribute attr = (RoleAttribute)role.getRoleAttributes().toArray()[i];
            RoleAttributeEntity attrEntity = (RoleAttributeEntity)entity.getRoleAttributes().toArray()[i];
            Assert.assertNotNull(attrEntity);
            Assert.assertEquals(attr.getRoleId(), attrEntity.getRoleId());
            Assert.assertEquals(attr.getAttrGroup(), attrEntity.getAttrGroup());
            Assert.assertEquals(attr.getMetadataElementId(), attrEntity.getMetadataElementId());
            Assert.assertEquals(attr.getName(), attr.getName());
            Assert.assertEquals(attr.getRoleAttrId(), attr.getRoleAttrId());
            Assert.assertEquals(attr.getServiceId(), attr.getServiceId());
            Assert.assertEquals(attr.getValue(), attr.getValue());
        }
        Assert.assertEquals(role.getRolePolicy().size(), entity.getRolePolicy().size());
        for(int i = 0; i < role.getRolePolicy().size(); i++) {
            RolePolicy policy = (RolePolicy)role.getRolePolicy().toArray()[i];
            RolePolicyEntity policyEntity = (RolePolicyEntity)entity.getRolePolicy().toArray()[i];
            Assert.assertNotNull(policyEntity);
            Assert.assertEquals(policy.getAction(), policyEntity.getAction());
            Assert.assertEquals(policy.getActionQualifier(), policyEntity.getActionQualifier());
            Assert.assertEquals(policy.getExecutionOrder(), policyEntity.getExecutionOrder());
            Assert.assertEquals(policy.getName(), policyEntity.getName());
            Assert.assertEquals(policy.getPolicyScript(), policyEntity.getPolicyScript());
            Assert.assertEquals(policy.getRoleId(), policyEntity.getRoleId());
            Assert.assertEquals(policy.getRolePolicyId(), policyEntity.getRolePolicyId());
            Assert.assertEquals(policy.getServiceId(), policyEntity.getServiceId());
            Assert.assertEquals(policy.getValue1(), policyEntity.getValue1());
            Assert.assertEquals(policy.getValue2(), policyEntity.getValue2());
        }
    }

    private void checkConversion(Role role, RoleEntity entity) {
        Assert.assertEquals(role.getId().getRoleId(),entity.getRoleId().getRoleId());
        Assert.assertEquals(role.getId().getServiceId(), entity.getRoleId().getServiceId());
        Assert.assertEquals(role.getCreateDate(), entity.getCreateDate());
        Assert.assertEquals(role.getCreatedBy(), entity.getCreatedBy());
        Assert.assertEquals(role.getDescription(), entity.getDescription());
        Assert.assertEquals(role.getEndDate(), entity.getEndDate());
        Assert.assertEquals(role.getInheritFromParent(), entity.getInheritFromParent());
        Assert.assertEquals(role.getMetadataTypeId(), entity.getMetadataTypeId());
        Assert.assertEquals(role.getInternalRoleId(), entity.getInternalRoleId());
        Assert.assertEquals(role.getOwnerId(), entity.getOwnerId());
        Assert.assertEquals(role.getParentRoleId(), entity.getParentRoleId());
        Assert.assertEquals(role.getOperation(), entity.getOperation());
        Assert.assertEquals(role.getProvisionObjName(), entity.getProvisionObjName());
        Assert.assertEquals(role.getInheritFromParent(), entity.getInheritFromParent());
        Assert.assertEquals(role.getRoleName(), entity.getRoleName());
        Assert.assertEquals(role.getSelected(), entity.getSelected());
        Assert.assertEquals(role.getStatus(), entity.getStatus());

    }

    private RoleEntity createSimpleRoleEntity() {
        RoleEntity entity = new RoleEntity();
        RoleEmbeddableId roleEmbeddableId = new RoleEmbeddableId(rs(4), rs(4));
        entity.setRoleId(roleEmbeddableId);
        entity.setCreateDate(new Date());
        entity.setCreatedBy(rs(4));
        entity.setDescription(rs(4));
        entity.setInheritFromParent(1);
        entity.setInternalRoleId(rs(3));
        entity.setMetadataTypeId(rs(4));
        entity.setOperation(AttributeOperationEnum.ADD);
        entity.setOwnerId(rs(4));
        entity.setParentRoleId(rs(3));
        entity.setProvisionObjName(rs(4));
        entity.setRoleName(rs(3));
        entity.setSelected(Boolean.TRUE);
        entity.setStatus(rs(3));
        entity.setUserAssociationMethod(1);
        entity.setEndDate(new Date());
        return entity;
    }

    private String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
