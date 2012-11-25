package org.openiam.dozer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.openiam.dozer.converter.ResourceDozerConverter;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourceGroupEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.CollectionUtils;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestResourceCrossEntityConversion extends AbstractTestNGSpringContextTests {

	@Autowired
	private ResourceDozerConverter resourceConverter;
	
	@Test
    public void testShallowResourceConversion() {
        final ResourceEntity entity = createSimpleResourceEntity();
        final Resource resource = resourceConverter.convertToDTO(entity, false);
        confirmSimple(resource, entity);
        confirmEmptyCollections(resource);
        final ResourceEntity convertedEntity = resourceConverter.convertToEntity(resource, false);
        confirmSimple(resource, convertedEntity);
        confirmEmptyCollections(convertedEntity);
    }
	
	@Test
	public void testDeepResouceConversion() {
		final ResourceEntity entity = createDeepResourceEntity();
		final Resource resource = resourceConverter.convertToDTO(entity, true);
		confirmSimple(resource, entity);
		confirmCollections(resource, entity);
		final ResourceEntity converted = resourceConverter.convertToEntity(resource, true);
		confirmCollections(resource, converted);
	}
	
	private void confirmCollections(final Resource resource, final ResourceEntity entity) {
		Assert.assertEquals(resource.getChildResources().size(), entity.getChildResources().size());
		Assert.assertEquals(resource.getResourceGroups().size(), entity.getResourceGroups().size());
		Assert.assertEquals(resource.getResourceProps().size(), entity.getResourceProps().size());
		Assert.assertEquals(resource.getResourceRoles().size(), entity.getResourceRoles().size());
	}
	
	private void confirmEmptyCollections(final ResourceEntity entity) {
		Assert.assertTrue(CollectionUtils.isEmpty(entity.getChildResources()));
		Assert.assertTrue(CollectionUtils.isEmpty(entity.getResourceGroups()));
		Assert.assertTrue(CollectionUtils.isEmpty(entity.getResourceProps()));
		Assert.assertTrue(CollectionUtils.isEmpty(entity.getResourceRoles()));
	}
	
	private void confirmEmptyCollections(final Resource resource) {
		Assert.assertTrue(CollectionUtils.isEmpty(resource.getChildResources()));
		Assert.assertTrue(CollectionUtils.isEmpty(resource.getResourceGroups()));
		Assert.assertTrue(CollectionUtils.isEmpty(resource.getResourceProps()));
		Assert.assertTrue(CollectionUtils.isEmpty(resource.getResourceRoles()));
	}

	private void confirmSimple(final Resource resource, final ResourceEntity entity) {
		Assert.assertEquals(resource.getBranchId(), entity.getBranchId());
		Assert.assertEquals(resource.getCategoryId(), entity.getCategoryId());
		Assert.assertEquals(resource.getDescription(), entity.getDescription());
		Assert.assertEquals(resource.getDisplayOrder(), entity.getDisplayOrder());
		Assert.assertEquals(resource.getManagedSysId(), entity.getManagedSysId());
		Assert.assertEquals(resource.getName(), entity.getName());
		Assert.assertEquals(resource.getNodeLevel(), entity.getNodeLevel());
		Assert.assertEquals(resource.getResourceId(), entity.getResourceId());
		Assert.assertEquals(resource.getResOwnerGroupId(), entity.getResOwnerGroupId());
		Assert.assertEquals(resource.getResOwnerUserId(), entity.getResOwnerUserId());
		Assert.assertEquals(resource.getSensitiveApp(), entity.getSensitiveApp());
		Assert.assertEquals(resource.getURL(), entity.getURL());
		confirm(resource.getResourceType(), entity.getResourceType());
	}
	
	private void confirm(final ResourceType resourceType, final ResourceTypeEntity entity) {
		Assert.assertEquals(resourceType.getDescription(), entity.getDescription());
		Assert.assertEquals(resourceType.getMetadataTypeId(), entity.getMetadataTypeId());
		Assert.assertEquals(resourceType.getProcessName(), entity.getProcessName());
		Assert.assertEquals(resourceType.getResourceTypeId(), entity.getResourceTypeId());
		Assert.assertEquals(resourceType.getProvisionResource(), entity.getProvisionResource());
	}
	
	private ResourceEntity createDeepResourceEntity() {
		final ResourceEntity entity = createSimpleResourceEntity();
		final Set<ResourceEntity> childResources = new HashSet<ResourceEntity>();
		childResources.add(createSimpleResourceEntity());
		childResources.add(createSimpleResourceEntity());
		childResources.add(createSimpleResourceEntity());
		childResources.add(createSimpleResourceEntity());
		childResources.add(createSimpleResourceEntity());
		entity.setChildResources(childResources);
		
		final Set<ResourceGroupEntity> resourceGroups = new HashSet<ResourceGroupEntity>();
		resourceGroups.add(createResoruceGroupEntity());
		resourceGroups.add(createResoruceGroupEntity());
		resourceGroups.add(createResoruceGroupEntity());
		resourceGroups.add(createResoruceGroupEntity());
		resourceGroups.add(createResoruceGroupEntity());
		entity.setResourceGroups(resourceGroups);
		
		final Set<ResourcePropEntity> resourceProps = new HashSet<ResourcePropEntity>();
		resourceProps.add(createResourcePropEntity());
		resourceProps.add(createResourcePropEntity());
		resourceProps.add(createResourcePropEntity());
		resourceProps.add(createResourcePropEntity());
		resourceProps.add(createResourcePropEntity());
		resourceProps.add(createResourcePropEntity());
		entity.setResourceProps(resourceProps);
		
		final Set<ResourceRoleEntity> resourceRoles = new HashSet<ResourceRoleEntity>();
		resourceRoles.add(createResourceRoleEntity());
		resourceRoles.add(createResourceRoleEntity());
		resourceRoles.add(createResourceRoleEntity());
		resourceRoles.add(createResourceRoleEntity());
		resourceRoles.add(createResourceRoleEntity());
		resourceRoles.add(createResourceRoleEntity());
		entity.setResourceRoles(resourceRoles);
		
		return entity;
	}
	
	private ResourceRoleEntity createResourceRoleEntity() {
		final ResourceRoleEntity entity = new ResourceRoleEntity();
		entity.setEndDate(new Date());
		
		final ResourceRoleEmbeddableId id = new ResourceRoleEmbeddableId();
		id.setResourceId(rs(2));
		id.setRoleId(rs(2));
		entity.setId(id);
		entity.setStartDate(new Date());
		return entity;
	}
	
	private ResourcePropEntity createResourcePropEntity() {
		final ResourcePropEntity entity = new ResourcePropEntity();
		entity.setMetadataId(rs(2));
		entity.setName(rs(2));
		entity.setPropValue(rs(2));
		entity.setResourceId(rs(2));
		entity.setResourcePropId(rs(2));
		return entity;
	}
	
	private ResourceGroupEntity createResoruceGroupEntity() {
		final ResourceGroupEntity entity = new ResourceGroupEntity();
		entity.setEndDate(new Date());
		entity.setGroupId(rs(2));
		entity.setResGroupId(rs(2));
		entity.setResourceId(rs(2));
		entity.setStartDate(new Date());
		return entity;
	}
	
	private ResourceEntity createSimpleResourceEntity() {
		final ResourceEntity entity = new ResourceEntity();
		entity.setBranchId(rs(2));
		entity.setCategoryId(rs(2));
		entity.setDescription(rs(2));
		entity.setDisplayOrder(3);
		entity.setManagedSysId(rs(2));
		entity.setName(rs(2));
		entity.setNodeLevel(3);
		entity.setResourceId(rs(2));
		entity.setResourceType(createResourceTypeEntity());
		entity.setResOwnerGroupId(rs(2));
		entity.setResOwnerUserId(rs(2));
		entity.setSensitiveApp(3);
		entity.setURL(rs(2));
		return entity;
	}
	
	private ResourceTypeEntity createResourceTypeEntity() {
		final ResourceTypeEntity entity = new ResourceTypeEntity();
		entity.setDescription(rs(2));
		entity.setMetadataTypeId(rs(2));
		entity.setProcessName(rs(2));
		entity.setProvisionResource(2);
		entity.setResourceTypeId(rs(2));
		return entity;
	}
	
	private String rs(final int size) {
		return RandomStringUtils.randomAlphanumeric(size);
	}
}
