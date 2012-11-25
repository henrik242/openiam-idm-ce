package org.openiam.dozer;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.dozer.Mapper;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;
import org.openiam.idm.srvc.grp.dto.GroupStatus;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.openiam.idm.srvc.role.dto.RolePolicy;
import org.openiam.idm.srvc.role.dto.RoleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestGroupConversion extends AbstractTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("deepDozerMapper")
	private Mapper deepDozerMapper;
	
	@Autowired
	@Qualifier("shallowDozerMapper")
	private Mapper shallowDozerMapper;
	
	@Test
	public void testGroupConversion() {
		final Group original = createGroupWithNoSubgroups();
		
		compareGroup(original, deepDozerMapper.map(original, Group.class), true);
		compareGroup(original, shallowDozerMapper.map(original, Group.class), false);
	}
	
	@Test
	public void testRoleConversion() {
		final Role original = createSimpleRole();
		
		final Set<Group> groupSet = new HashSet<Group>();
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		groupSet.add(createGroupWithNoSubgroups());
		original.setGroups(groupSet);
		
		final List<Role> childRoleList = new LinkedList<Role>();
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		childRoleList.add(createSimpleRole());
		
		compareRole(original, deepDozerMapper.map(original, Role.class), true);
		compareRole(original, shallowDozerMapper.map(original, Role.class), false);
	}
	
	private Group createGroupWithNoSubgroups() {
		final Group group = createSimpleGroup();
		
		final Set<Role> roleSet = new HashSet<Role>();
		roleSet.add(createSimpleRole());
		roleSet.add(createSimpleRole());
		roleSet.add(createSimpleRole());
		roleSet.add(createSimpleRole());
		roleSet.add(createSimpleRole());
		group.setRoles(roleSet);
		
		final Map<String, GroupAttribute> groupAttributeMap = new HashMap<String, GroupAttribute>();
		groupAttributeMap.put(rs(2), createGroupAttribute());
		groupAttributeMap.put(rs(2), createGroupAttribute());
		groupAttributeMap.put(rs(2), createGroupAttribute());
		groupAttributeMap.put(rs(2), createGroupAttribute());
		groupAttributeMap.put(rs(2), createGroupAttribute());
		group.setAttributes(groupAttributeMap);
		
		return group;
	}
	
	private Group createSimpleGroup() {
		final Group group = new Group();
		group.setCompanyId(rs(2));
		group.setCreateDate(new Date());
		group.setCreatedBy(rs(2));
		group.setDescription(rs(2));
		group.setGroupClass(rs(2));
		group.setGroupStatus(GroupStatus.ACTIVE);
		group.setGrpId(rs(2));
		group.setGrpName(rs(2));
		group.setInternalGroupId(rs(2));
		group.setLastUpdate(new Date());
		group.setLastUpdatedBy(rs(2));
		group.setMetadataTypeId(rs(2));
		group.setOperation(AttributeOperationEnum.ADD);
		group.setOwnerId(rs(2));
		group.setProvisionMethod(rs(2));
		group.setProvisionObjName(rs(2));
		group.setSelected(true);
		group.setStatus(rs(2));
		return group;
	}
	
	private GroupAttribute createGroupAttribute() {
		final GroupAttribute groupAttribute = new GroupAttribute();
		groupAttribute.setGroupId(rs(2));
		groupAttribute.setId(rs(2));
		groupAttribute.setMetadataElementId(rs(2));
		groupAttribute.setName(rs(2));
		groupAttribute.setValue(rs(2));
		return groupAttribute;
	}
	
	private Role createSimpleRole() {
		final Role role = new Role();
		role.setCreateDate(new Date());
		role.setCreatedBy(rs(2));
		role.setDescription(rs(2));
		role.setEndDate(new Date());
		role.setInternalRoleId(rs(2));
		role.setMetadataTypeId(rs(2));
		role.setObjectState(rs(2));
		role.setOperation(AttributeOperationEnum.ADD);
		role.setOwnerId(rs(2));
		role.setProvisionObjName(rs(2));
		role.setRequestClientIP(rs(2));
		role.setRequestorDomain(rs(2));
		role.setRequestorLogin(rs(2));
		role.setRoleName(rs(2));
		role.setRoleStatus(RoleStatus.ACTIVE);
		role.setSelected(true);
		role.setStartDate(new Date());
		role.setStatus(rs(2));
		role.setUserAssociationMethod(2);		
		final Set<RolePolicy> rolePolicySet = new HashSet<RolePolicy>();
		rolePolicySet.add(createRolePolicy());
		rolePolicySet.add(createRolePolicy());
		rolePolicySet.add(createRolePolicy());
		rolePolicySet.add(createRolePolicy());
		role.setRolePolicy(rolePolicySet);
		
		//role.setChildRoles(childRoles);
		//	role.setGroups(value);
		
		final  Set<RoleAttribute> roleAttributeSet = new HashSet<RoleAttribute>();
		roleAttributeSet.add(createRoleAttribute());
		roleAttributeSet.add(createRoleAttribute());
		roleAttributeSet.add(createRoleAttribute());
		roleAttributeSet.add(createRoleAttribute());
		roleAttributeSet.add(createRoleAttribute());
		role.setRoleAttributes(roleAttributeSet);
		return role;
	}
	
	private RoleAttribute createRoleAttribute() {
		final RoleAttribute roleAttribute = new RoleAttribute();
		roleAttribute.setAttrGroup(rs(2));
		roleAttribute.setMetadataElementId(rs(2));
		roleAttribute.setName(rs(2));
		roleAttribute.setRoleAttrId(rs(2));
		roleAttribute.setRoleId(rs(2));
		roleAttribute.setValue(rs(2));
		return roleAttribute;
	}
	
	private RolePolicy createRolePolicy() {
		final RolePolicy rolePolicy = new RolePolicy();
		rolePolicy.setAction(RolePolicy.NEW);
		rolePolicy.setActionQualifier(rs(2));
		rolePolicy.setExecutionOrder(2);
		rolePolicy.setName(rs(2));
		rolePolicy.setObjectState(rs(2));
		rolePolicy.setPolicyScript(rs(2));
		rolePolicy.setRequestClientIP(rs(2));
		rolePolicy.setRequestorDomain(rs(2));
		rolePolicy.setRequestorLogin(rs(2));
		rolePolicy.setRoleId(rs(2));
		rolePolicy.setRolePolicyId(rs(2));
		rolePolicy.setSelected(true);
		rolePolicy.setValue1(rs(2));
		rolePolicy.setValue2(rs(2));
		return rolePolicy;
	}
	
	
	private String rs(final int size) {
		return RandomStringUtils.randomAlphanumeric(size);
	}
	
	private void compareGroup(final Group original, final Group copy, final boolean isDeep) {
		Assert.assertEquals(original.getCompanyId(), copy.getCompanyId());
		Assert.assertEquals(original.getCreatedBy(), copy.getCreatedBy());
		Assert.assertEquals(original.getDescription(), copy.getDescription());
		Assert.assertEquals(original.getGroupClass(), copy.getGroupClass());
		Assert.assertEquals(original.getGrpId(), copy.getGrpId());
		Assert.assertEquals(original.getGrpName(), copy.getGrpName());
		Assert.assertEquals(original.getInternalGroupId(), copy.getInternalGroupId());
		Assert.assertEquals(original.getLastUpdatedBy(), copy.getLastUpdatedBy());
		Assert.assertEquals(original.getLastUpdate(), copy.getLastUpdate());
		Assert.assertEquals(original.getMetadataTypeId(), copy.getMetadataTypeId());
		Assert.assertEquals(original.getOwnerId(), copy.getOwnerId());
		Assert.assertEquals(original.getOperation(), copy.getOperation());
		Assert.assertEquals(original.getProvisionMethod(), copy.getProvisionMethod());
		Assert.assertEquals(original.getProvisionObjName(), copy.getProvisionObjName());
		Assert.assertEquals(original.getStatus(), copy.getStatus());
		Assert.assertEquals(original.getSelected(), copy.getSelected());
		
		if(isDeep) {
			//TODO:  assert
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getRoles()));
		}
	}
	
	private void compareRole(final Role original, final Role copy, final boolean isDeep) {
		Assert.assertEquals(original.getCreatedBy(), copy.getCreatedBy());
		Assert.assertEquals(original.getDescription(), copy.getDescription());
		Assert.assertEquals(original.getInternalRoleId(), copy.getInternalRoleId());
		Assert.assertEquals(original.getMetadataTypeId(), copy.getMetadataTypeId());
		Assert.assertEquals(original.getObjectState(), copy.getObjectState());
		Assert.assertEquals(original.getOwnerId(), copy.getOwnerId());
		Assert.assertEquals(original.getProvisionObjName(), copy.getProvisionObjName());
		Assert.assertEquals(original.getRequestClientIP(), copy.getRequestClientIP());
		Assert.assertEquals(original.getRequestorDomain(), copy.getRequestorDomain());
		Assert.assertEquals(original.getRequestorLogin(), copy.getRequestorLogin());
		Assert.assertEquals(original.getRoleName(), copy.getRoleName());
		Assert.assertEquals(original.getStatus(), copy.getStatus());
		Assert.assertEquals(original.getSelected(), copy.getSelected());
		Assert.assertEquals(original.getStartDate(), copy.getStartDate());
		Assert.assertEquals(original.getUserAssociationMethod(), copy.getUserAssociationMethod());
		
		if(isDeep) {
			//TODO:  compare
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getRoleAttributes()));
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getRolePolicy()));
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getChildRoles()));
		}
	}
}
