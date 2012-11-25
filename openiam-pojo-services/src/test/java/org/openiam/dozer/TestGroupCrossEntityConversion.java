package org.openiam.dozer;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.GroupDozerConverter;
import org.openiam.idm.srvc.grp.domain.GroupAttributeEntity;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestGroupCrossEntityConversion extends AbstractTestNGSpringContextTests {

    @Autowired
    private GroupDozerConverter groupDozerConverter;

    @Test
    public void testSupervisorShallowConversion() {
        final GroupEntity entity = createDeepGroupEntity();
        final Group group = groupDozerConverter.convertToDTO(entity, false);
        checkConversion(group, entity, false);
        final GroupEntity convertedEntity = groupDozerConverter.convertToEntity(group, false);
        checkConversion(group, convertedEntity, false);
    }

    @Test
    public void testSupervisorDeepConversion() {
        final GroupEntity entity = createDeepGroupEntity();
        final Group group = groupDozerConverter.convertToDTO(entity, true);
        checkConversion(group, entity, true);
        confirmCollections(group, entity);
        final GroupEntity convertedEntity = groupDozerConverter.convertToEntity(group, true);
        checkConversion(group, convertedEntity, true);
        confirmCollections(group, entity);
    }

    static void checkConversion(Group group, GroupEntity entity, boolean isDeep) {
        Assert.assertEquals(group.getCompanyId(), entity.getCompanyId());
        Assert.assertEquals(group.getCreateDate(), entity.getCreateDate());
        Assert.assertEquals(group.getCreatedBy(), entity.getCreatedBy());
        Assert.assertEquals(group.getDescription(), entity.getDescription());
        Assert.assertEquals(group.getGroupClass(), entity.getGroupClass());
        Assert.assertEquals(group.getGrpId(), entity.getGrpId());
        Assert.assertEquals(group.getGrpName(), entity.getGrpName());
        Assert.assertEquals(group.getInheritFromParent(), entity.getInheritFromParent());
        Assert.assertEquals(group.getInternalGroupId(), entity.getInternalGroupId());
        Assert.assertEquals(group.getLastUpdate(), entity.getLastUpdate());
        Assert.assertEquals(group.getLastUpdatedBy(), entity.getLastUpdatedBy());
        Assert.assertEquals(group.getMetadataTypeId(), entity.getMetadataTypeId());
        Assert.assertEquals(group.getOperation(), entity.getOperation());
        Assert.assertEquals(group.getOwnerId(), entity.getOwnerId());
        Assert.assertEquals(group.getParentGrpId(), entity.getParentGrpId());
        Assert.assertEquals(group.getProvisionMethod(), entity.getProvisionMethod());
        Assert.assertEquals(group.getProvisionObjName(), entity.getProvisionObjName());
    }

    private void confirmCollections(final Group group, final GroupEntity entity) {
        Assert.assertEquals(group.getAttributes().size(), entity.getAttributes().size());
        Assert.assertEquals(group.getSubGroup().size(), entity.getSubGroup().size());
        Assert.assertEquals(group.getRoles().size(), entity.getRoles().size());
    }

    static GroupEntity createDeepGroupEntity() {

        GroupEntity groupEntity = createSimpleGroupEntity();
        Map<String, GroupAttributeEntity> attributeEntityMap = new HashMap<String, GroupAttributeEntity>();
        GroupAttributeEntity attributeEntity = new GroupAttributeEntity();
        attributeEntity.setId(rs(4));
        attributeEntity.setGroupId(rs(3));
        attributeEntity.setMetadataElementId(rs(3));
        attributeEntity.setName(rs(4));
        attributeEntity.setValue(rs(5));
        attributeEntityMap.put(rs(3),attributeEntity);
        attributeEntity.setId(rs(4));
        attributeEntity.setGroupId(rs(3));
        attributeEntity.setMetadataElementId(rs(3));
        attributeEntity.setName(rs(4));
        attributeEntity.setValue(rs(5));
        attributeEntityMap.put(rs(3),attributeEntity);
        groupEntity.setAttributes(attributeEntityMap);
        List<GroupEntity> subGroupEntityList = new LinkedList<GroupEntity>();
        subGroupEntityList.add(createSimpleGroupEntity());
        subGroupEntityList.add(createSimpleGroupEntity());
        groupEntity.setSubGroup(subGroupEntityList);

        return groupEntity;
    }

    static GroupEntity createSimpleGroupEntity() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setGrpId(rs(5));
        groupEntity.setCompanyId(rs(5));
        groupEntity.setCreateDate(new Date());
        groupEntity.setCreatedBy(rs(2));
        groupEntity.setDescription(rs(5));
        groupEntity.setGroupClass(rs(4));
        groupEntity.setGrpName(rs(4));
        groupEntity.setInheritFromParent(Boolean.TRUE);
        groupEntity.setInternalGroupId(rs(4));
        groupEntity.setLastUpdate(new Date());
        groupEntity.setLastUpdatedBy(rs(4));
        groupEntity.setMetadataTypeId(rs(2));
        groupEntity.setOwnerId(rs(2));
        groupEntity.setParentGrpId(rs(3));
        groupEntity.setProvisionMethod(rs(4));
        groupEntity.setProvisionObjName(rs(5));
        groupEntity.setStatus(rs(2));

        return groupEntity;
    }

    private static String rs(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }
}
