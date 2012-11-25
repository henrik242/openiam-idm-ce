package org.openiam.dozer;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.dozer.Mapper;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginAttribute;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.idm.srvc.user.dto.UserNote;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:test-application-context.xml")
public class TestDozerConversion extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("deepDozerMapper")
	private Mapper deepDozerMapper;
	
	@Autowired
	@Qualifier("shallowDozerMapper")
	private Mapper shallowDozerMapper;
	
	@Test
	public void testUserConversion() {
		final User user = new User();
		user.setAddress1(rs(5));
		user.setAddress2(rs(5));
		user.setAddress3(rs(5));
		user.setAddress4(rs(5));
		user.setAddress5(rs(5));
		user.setAddress6(rs(5));
		user.setAddress7(rs(5));
		
		final Set<Address> addressSet = new HashSet<Address>();
		addressSet.add(new Address(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), 1, rs(2)));
		addressSet.add(new Address(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), 0, rs(2)));
		addressSet.add(new Address(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), 0, rs(2)));
		addressSet.add(new Address(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), 0, rs(2)));
		addressSet.add(new Address(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), 0, rs(2)));
		user.setAddresses(addressSet);
		
		user.setAlternateContactId(rs(2));
		user.setAreaCd(rs(2));
		user.setBirthdate(new Date());
		user.setBldgNum(rs(2));
		user.setCity(rs(2));
		user.setClassification(rs(2));
		user.setCompanyId(rs(2));
		user.setCompanyOwnerId(rs(2));
		user.setCostCenter(rs(2));
		user.setCountry(rs(2));
		user.setCountryCd(rs(2));
		user.setDateChallengeRespChanged(new Date());
		user.setDatePasswordChanged(new Date());
		user.setDelAdmin(2);
		user.setDeptCd(rs(2));
		user.setDeptName(rs(2));
		user.setDivision(rs(2));
		user.setEmail(rs(2));
		
		final Set<EmailAddress> emailAddresses = new HashSet<EmailAddress>();
		emailAddresses.add(new EmailAddress(rs(2), rs(2), rs(2), 1));
		emailAddresses.add(new EmailAddress(rs(2), rs(2), rs(2), 1));
		emailAddresses.add(new EmailAddress(rs(2), rs(2), rs(2), 1));
		user.setEmailAddresses(emailAddresses);
		user.setEmployeeId(rs(2));
		user.setEmployeeType(rs(2));
		user.setFirstName(rs(2));
		user.setJobCode(rs(2));
		user.setLastDate(new Date());
		user.setLastName(rs(2));
		user.setLastUpdate(new Date());
		user.setLastUpdatedBy(rs(2));
		user.setLocationCd(rs(2));
		user.setLocationName(rs(2));
		user.setMaidenName(rs(2));
		user.setMailCode(rs(2));
		user.setManagerId(rs(2));
		user.setMetadataTypeId(rs(2));
		user.setMiddleInit(rs(2));
		user.setNickname(rs(2));
		user.setObjectState(rs(2));
		user.setPasswordTheme(rs(2));
		
		final Set<Phone> phones = new HashSet<Phone>();
		phones.add(new Phone(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		phones.add(new Phone(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		phones.add(new Phone(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		phones.add(new Phone(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		user.setPhones(phones);
		user.setPhoneExt(rs(2));
		user.setPhoneNbr(rs(2));
		user.setPostalCd(rs(2));
		user.setPrefix(rs(2));
		
		final List<Login> principalList = new LinkedList<Login>();
		principalList.add(new Login(new LoginId(rs(2), rs(2), rs(2)), 1, 1));
		principalList.add(new Login(new LoginId(rs(2), rs(2), rs(2)), 1, 1));
		principalList.add(new Login(new LoginId(rs(2), rs(2), rs(2)), 1, 1));
		principalList.add(new Login(new LoginId(rs(2), rs(2), rs(2)), 1, 1));
		user.setPrincipalList(principalList);
		user.setRequestClientIP(rs(2));
		user.setRequestorDomain(rs(2));
		user.setRequestorLogin(rs(2));
		user.setSecondaryStatus(UserStatusEnum.ACTIVE);
		user.setSecurityDomain(rs(2));
		user.setSelected(true);
		user.setSex(rs(2));
		user.setShowInSearch(2);
		user.setStartDate(new Date());
		user.setState(rs(2));
		user.setStatus(UserStatusEnum.ACTIVE);
		user.setStreetDirection(rs(2));
		user.setSuffix(rs(2));
		user.setSuite(rs(2));
		
		final Supervisor supervisor = new Supervisor();
		supervisor.setComments(rs(4));
		supervisor.setEmployee(null);
		supervisor.setEndDate(null);
		supervisor.setIsPrimarySuper(2);
		supervisor.setOrgStructureId(rs(4));
		supervisor.setStartDate(null);
		supervisor.setStatus(rs(4));
		supervisor.setSupervisor(null);
		supervisor.setSupervisorType(rs(3));
		user.setSupervisor(supervisor);
		user.setTitle(rs(2));
		user.setUserAttributes(new HashMap<String, UserAttribute>());
		user.setUserId(rs(2));
		
		final Set<UserNote> userNotes = new HashSet<UserNote>();
		userNotes.add(new UserNote());
		userNotes.add(new UserNote());
		userNotes.add(new UserNote());
		userNotes.add(new UserNote());
		user.setUserNotes(userNotes);
		user.setUserOwnerId(rs(2));
		user.setUserTypeInd(rs(2));
		
		final HashMap<String, UserAttribute> userAttributeMap = new HashMap<String, UserAttribute>();
		userAttributeMap.put(rs(2), new UserAttribute(rs(2), rs(2)));
		userAttributeMap.put(rs(2), new UserAttribute(rs(2), rs(2)));
		userAttributeMap.put(rs(2), new UserAttribute(rs(2), rs(2)));
		userAttributeMap.put(rs(2), new UserAttribute(rs(2), rs(2)));
		userAttributeMap.put(rs(2), new UserAttribute(rs(2), rs(2)));
		user.setUserAttributes(userAttributeMap);
		
		final User deepCopy = deepDozerMapper.map(user, User.class);
		final User shallowCopy = shallowDozerMapper.map(user, User.class);
		compareUser(user, deepCopy, true);
		compareUser(user, shallowCopy, false);
		
	}
	
	@Test
	public void testLoginConversion() {
		final Login login = new Login();
		login.setAuthFailCount(2);
		login.setCanonicalName(rs(2));
		login.setCreateDate(new Date());
		login.setCreatedBy(rs(2));
		login.setCurrentLoginHost(rs(2));
		login.setFirstTimeLogin(2);
		login.setGracePeriod(new Date());
		login.setId(new LoginId(rs(2), rs(2), rs(2)));
		login.setIsDefault(3);
		login.setIsLocked(3);
		login.setLastAuthAttempt(new Date());
		login.setLastLogin(new Date());
		login.setLastLoginIP(rs(2));
		
		final Set<LoginAttribute> loginAttributes = new LinkedHashSet<LoginAttribute>();
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		loginAttributes.add(new LoginAttribute(rs(2), rs(2), rs(2), rs(2), rs(2), rs(2), rs(2)));
		login.setLoginAttributes(loginAttributes);
		login.setManagedSysName(rs(2));
		login.setOperation(AttributeOperationEnum.ADD);
		login.setOrigPrincipalName(rs(2));
		login.setPassword(rs(2));
		login.setPasswordChangeCount(5);
		login.setPrevLogin(new Date());
		login.setPrevLoginIP(rs(2));
		login.setPswdResetToken(rs(2));
		login.setPswdResetTokenExp(new Date());
		login.setPwdChanged(new Date());
		login.setPwdEquivalentToken(rs(2));
		login.setPwdExp(new Date());
		login.setResetPassword(4);
		login.setSelected(true);
		login.setStatus(rs(2));
		login.setUserId(rs(2));
		
		final Login deepCopy = deepDozerMapper.map(login, Login.class);
		final Login shallowCopy = shallowDozerMapper.map(login, Login.class);
		compareLogin(login, deepCopy, true);
		compareLogin(login, shallowCopy, false);
	}
	
	@Test
	public void testSupervisorConvert() {
		final Supervisor original = new Supervisor();
		original.setComments(rs(2));
		original.setEmployee(new User(rs(2)));
		original.setEndDate(new Date());
		original.setIsPrimarySuper(3);
		original.setOrgStructureId(rs(2));
		original.setStartDate(new Date());
		original.setStatus(rs(2));
		original.setSupervisor(new User(rs(2)));
		original.setSupervisorType(rs(2));
		
		final Supervisor deepCopy = deepDozerMapper.map(original, Supervisor.class);
		final Supervisor shallowCopy = shallowDozerMapper.map(original, Supervisor.class);
		comapreSupervisor(original, deepCopy, true);
		comapreSupervisor(original, shallowCopy, true);
	}
	
	@Test
	public void testConvertLoginId() {
		final LoginId original = new LoginId();
		original.setDomainId(rs(2));
		original.setLogin(rs(2));
		original.setManagedSysId(rs(2));
		
		compareLoginId(original, deepDozerMapper.map(original, LoginId.class));
		compareLoginId(original, shallowDozerMapper.map(original, LoginId.class));
	}

	
	@Test
	public void testConvertUserNote() {

	}
	
	@Test
	public void testConvertUserAttribute() {

	}
	
	@Test
	public void testConvertAddress() {
		
	}
	
	@Test
	public void testConvertPhone() {
		
	}
	
	@Test
	public void testConvertEmailAddress() {
		
	}
	
	private String rs(final int size) {
		return RandomStringUtils.randomAlphanumeric(size);
	}
	
	private void compareLoginId(final LoginId original, final LoginId copy) {
		Assert.assertEquals(original.getDomainId(), copy.getDomainId());
		Assert.assertEquals(original.getLogin(), copy.getLogin());
		Assert.assertEquals(original.getManagedSysId(), copy.getManagedSysId());
	}
	
	private void comapreSupervisor(final Supervisor original, final Supervisor copy, final boolean isDeep) {
		Assert.assertEquals(original.getComments(), copy.getComments());
		Assert.assertEquals(original.getEndDate(), copy.getEndDate());
		Assert.assertEquals(original.getIsPrimarySuper(), copy.getIsPrimarySuper());
		Assert.assertEquals(original.getOrgStructureId(), copy.getOrgStructureId());
		Assert.assertEquals(original.getStartDate(), copy.getStartDate());
		Assert.assertEquals(original.getStatus(), copy.getStatus());
		Assert.assertEquals(original.getSupervisorType(), copy.getSupervisorType());
		if(isDeep) {
			//Assert.assertEquals(original.getEmployee(), copy.getEmployee());
			//Assert.assertEquals(original.getSupervisor(), copy.getSupervisor());
		} else {
			Assert.assertNull(copy.getEmployee());
			Assert.assertNull(copy.getSupervisor());
		}
	}
	
	private void compareLogin(final Login original, final Login copy, final boolean isDeep) {
		Assert.assertEquals(original.getAuthFailCount(), copy.getAuthFailCount());
		Assert.assertEquals(original.getCanonicalName(), copy.getCanonicalName());
		Assert.assertEquals(original.getCreateDate(), copy.getCreateDate());
		Assert.assertEquals(original.getCreatedBy(), copy.getCreatedBy());
		Assert.assertEquals(original.getCurrentLoginHost(), copy.getCurrentLoginHost());
		Assert.assertEquals(original.getFirstTimeLogin(), copy.getFirstTimeLogin());
		Assert.assertEquals(original.getGracePeriod(), copy.getGracePeriod());
		Assert.assertEquals(original.getId(), copy.getId());
		Assert.assertEquals(original.getIsDefault(), copy.getIsDefault());
		Assert.assertEquals(original.getIsLocked(), copy.getIsLocked());
		Assert.assertEquals(original.getLastAuthAttempt(), copy.getLastAuthAttempt());
		Assert.assertEquals(original.getLastLogin(), copy.getLastLogin());
		Assert.assertEquals(original.getLastLoginIP(), copy.getLastLoginIP());
		
		if(isDeep) {
			//Assert.assertEquals(original.getLoginAttributes(), copy.getLoginAttributes());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getLoginAttributes()));
		}
		Assert.assertEquals(original.getManagedSysName(), copy.getManagedSysName());
		Assert.assertEquals(original.getOperation(), copy.getOperation());
		Assert.assertEquals(original.getOrigPrincipalName(), copy.getOrigPrincipalName());
		Assert.assertEquals(original.getPassword(), copy.getPassword());
		Assert.assertEquals(original.getPasswordChangeCount(), copy.getPasswordChangeCount());
		Assert.assertEquals(original.getPrevLogin(), copy.getPrevLogin());
		Assert.assertEquals(original.getPrevLoginIP(), copy.getPrevLoginIP());
		Assert.assertEquals(original.getPswdResetToken(), copy.getPswdResetToken());
		Assert.assertEquals(original.getPswdResetTokenExp(), copy.getPswdResetTokenExp());
		Assert.assertEquals(original.getPwdChanged(), copy.getPwdChanged());
		Assert.assertEquals(original.getPwdEquivalentToken(), copy.getPwdEquivalentToken());
		Assert.assertEquals(original.getPwdEquivalentToken(), copy.getPwdEquivalentToken());
		Assert.assertEquals(original.getPwdExp(), copy.getPwdExp());
		Assert.assertEquals(original.getResetPassword(), copy.getResetPassword());
		Assert.assertEquals(original.isSelected(), copy.isSelected());
		Assert.assertEquals(original.getStatus(), copy.getStatus());
		Assert.assertEquals(original.getUserId(), copy.getUserId());
	}
	
	private void compareUser(final User original, final User copy, final boolean isDeep) {
		Assert.assertEquals(original.getAddress1(), copy.getAddress1());
		Assert.assertEquals(original.getAddress2(), copy.getAddress2());
		Assert.assertEquals(original.getAddress3(), copy.getAddress3());
		Assert.assertEquals(original.getAddress4(), copy.getAddress4());
		Assert.assertEquals(original.getAddress5(), copy.getAddress5());
		Assert.assertEquals(original.getAddress6(), copy.getAddress6());
		Assert.assertEquals(original.getAddress7(), copy.getAddress7());
		if(isDeep) {
			//Assert.assertEquals(user.getAddresses(), copy.getAddresses());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getAddresses()));
		}
		
		Assert.assertEquals(original.getAlternateContactId(), copy.getAlternateContactId());
		Assert.assertEquals(original.getAreaCd(), copy.getAreaCd());
		Assert.assertEquals(original.getBirthdate(), copy.getBirthdate());
		Assert.assertEquals(original.getBldgNum(), copy.getBldgNum());
		Assert.assertEquals(original.getCity(), copy.getCity());
		Assert.assertEquals(original.getClassification(), copy.getClassification());
		Assert.assertEquals(original.getCompanyId(), copy.getCompanyId());
		Assert.assertEquals(original.getCompanyOwnerId(), copy.getCompanyOwnerId());
		Assert.assertEquals(original.getCostCenter(), copy.getCostCenter());
		Assert.assertEquals(original.getCountry(), copy.getCountry());
		Assert.assertEquals(original.getCountryCd(), copy.getCountryCd());
		Assert.assertEquals(original.getDateChallengeRespChanged(), copy.getDateChallengeRespChanged());
		Assert.assertEquals(original.getDatePasswordChanged(), copy.getDatePasswordChanged());
		Assert.assertEquals(original.getDelAdmin(), copy.getDelAdmin());
		Assert.assertEquals(original.getDeptCd(), copy.getDeptCd());
		Assert.assertEquals(original.getDeptName(), copy.getDeptName());
		Assert.assertEquals(original.getDivision(), copy.getDivision());
		Assert.assertEquals(original.getEmail(), copy.getEmail());
		
		if(isDeep) {
			//Assert.assertEquals(original.getEmailAddress(), copy.getEmailAddress());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getEmailAddresses()));
		}
		Assert.assertEquals(original.getEmployeeId(), copy.getEmployeeId());
		Assert.assertEquals(original.getEmployeeType(), copy.getEmployeeType());
		Assert.assertEquals(original.getFirstName(), copy.getFirstName());
		Assert.assertEquals(original.getJobCode(), copy.getJobCode());
		Assert.assertEquals(original.getLastDate(), copy.getLastDate());
		Assert.assertEquals(original.getLastName(), copy.getLastName());
		Assert.assertEquals(original.getLastUpdate(), copy.getLastUpdate());
		Assert.assertEquals(original.getLastUpdatedBy(), copy.getLastUpdatedBy());
		Assert.assertEquals(original.getLocationCd(), copy.getLocationCd());
		Assert.assertEquals(original.getLocationName(), copy.getLocationName());
		Assert.assertEquals(original.getMaidenName(), copy.getMaidenName());
		Assert.assertEquals(original.getMailCode(), copy.getMailCode());
		Assert.assertEquals(original.getManagerId(), copy.getManagerId());
		Assert.assertEquals(original.getMetadataTypeId(), copy.getMetadataTypeId());
		Assert.assertEquals(original.getMiddleInit(), copy.getMiddleInit());
		Assert.assertEquals(original.getNickname(), copy.getNickname());
		Assert.assertEquals(original.getObjectState(), copy.getObjectState());
		Assert.assertEquals(original.getPasswordTheme(), copy.getPasswordTheme());
		
		
		if(isDeep) {
			//Assert.assertEquals(original.getPhone(), copy.getPhone());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getPhones()));
		}
		Assert.assertEquals(original.getPhoneExt(), copy.getPhoneExt());
		Assert.assertEquals(original.getPhoneNbr(), copy.getPhoneNbr());
		Assert.assertEquals(original.getPostalCd(), copy.getPostalCd());
		Assert.assertEquals(original.getPrefix(), copy.getPrefix());
		
		if(isDeep) {
			//Assert.assertEquals(original.getPrincipalList(), copy.getPrincipalList());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getPrincipalList()));
		}
		Assert.assertEquals(original.getRequestClientIP(), copy.getRequestClientIP());
		Assert.assertEquals(original.getRequestorDomain(), copy.getRequestorDomain());
		Assert.assertEquals(original.getRequestorLogin(), copy.getRequestorLogin());
		Assert.assertEquals(original.getSecondaryStatus(), copy.getSecondaryStatus());
		Assert.assertEquals(original.getSecurityDomain(), copy.getSecurityDomain());
		Assert.assertEquals(original.getSelected(), copy.getSelected());
		Assert.assertEquals(original.getSex(), copy.getSex());
		Assert.assertEquals(original.getShowInSearch(), copy.getShowInSearch());
		Assert.assertEquals(original.getStartDate(), copy.getStartDate());
		Assert.assertEquals(original.getState(), copy.getState());
		Assert.assertEquals(original.getStatus(), copy.getStatus());
		Assert.assertEquals(original.getStreetDirection(), copy.getStreetDirection());
		Assert.assertEquals(original.getSuffix(), copy.getSuffix());
		Assert.assertEquals(original.getSuite(), copy.getSuite());
		
		if(isDeep) {
			//Assert.assertEquals(original.getSupervisor(), copy.getSupervisor());
		} else {
			Assert.assertNull(copy.getSupervisor());
		}
		Assert.assertEquals(original.getTitle(), copy.getTitle());
		if(isDeep) {
			//Assert.assertEquals(original.getUserAttributes(), copy.getUserAttributes());
		} else {
			Assert.assertTrue(MapUtils.isEmpty(copy.getUserAttributes()));
		}
		Assert.assertEquals(original.getUserId(), copy.getUserId());
		
		
		if(isDeep) {
			//Assert.assertEquals(original.getUserNotes(), copy.getUserNotes());
		} else {
			Assert.assertTrue(CollectionUtils.isEmpty(copy.getUserNotes()));
		}
		Assert.assertEquals(original.getUserOwnerId(), copy.getUserOwnerId());
		Assert.assertEquals(original.getUserTypeInd(), copy.getUserTypeInd());
	}
}
