/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 *
 */
package org.openiam.provision.dto;

import com.thoughtworks.xstream.XStream;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.res.dto.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * ProvisionUser is the user object used by the provisioning service.
 * 
 * @author suneet
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvisionUser", propOrder = { "memberOfGroups", "requestId",
		"sessionId", "memberOfRoles", "userResourceList", "userAffiliations",
		"srcSystemId", "provisionModel", 
		"notifyTargetSystems", "emailCredentialsToNewUsers",
		"emailCredentialsToSupervisor", "provisionOnStartDate",
		"addInitialPasswordToHistory", "passwordPolicy", "password",
		"skipPreprocessor", "skipPostProcessor" })
public class ProvisionUser extends org.openiam.idm.srvc.user.dto.User {
	/**
     *
     */
	private static final long serialVersionUID = 6441635701870724194L;
	// protected List<Login> principalList;
	protected List<Group> memberOfGroups;
	protected List<Role> memberOfRoles;
	protected List<Organization> userAffiliations;

	protected List<UserResourceAssociation> userResourceList;

	public ProvisionModelEnum provisionModel;

	boolean emailCredentialsToNewUsers = false;
	boolean emailCredentialsToSupervisor = false;
	boolean addInitialPasswordToHistory = false;

	// default behaviour - you dont have to wait till the start date to
	// provision a user
	// if this is set to true, the system will wait till the start date to
	// provision the user
	boolean provisionOnStartDate = false;

	protected String requestId;
	protected String sessionId;

	// flags to skip over the service level pre and post processors
	boolean skipPreprocessor = false;
	boolean skipPostProcessor = false;

	/*
	 * ID of the system where this request came from. If this value is set, then
	 * in the modify operation, that resource will not be updated.
	 */
	protected String srcSystemId;
	/*
	 * Flag that indicates if target systems should be updated or not
	 */
	protected boolean notifyTargetSystems = true;

	protected Policy passwordPolicy = null;

	protected String password = null;

	public ProvisionUser() {

	}

	public ProvisionUser(User user) {
		birthdate = user.getBirthdate();
		companyId = user.getCompanyId();
		companyOwnerId = user.getCompanyOwnerId();
		createDate = user.getCreateDate();
		createdBy = user.getCreatedBy();
		deptCd = user.getDeptCd();
		deptName = user.getDeptName();
		employeeId = user.getEmployeeId();
		employeeType = user.getEmployeeType();

		firstName = user.getFirstName();
		jobCode = user.getJobCode();
		lastName = user.getLastName();
		lastUpdate = user.getLastUpdate();
		this.lastUpdatedBy = user.getLastUpdatedBy();
		this.locationCd = user.getLocationCd();
		this.locationName = user.getLocationName();
		this.managerId = user.getManagerId();
		this.metadataTypeId = user.getMetadataTypeId();
		this.classification = user.getClassification();
		this.middleInit = user.getMiddleInit();
		this.prefix = user.getPrefix();
		this.sex = user.getSex();
		this.status = user.getStatus();
		this.secondaryStatus = user.getSecondaryStatus();
		this.suffix = user.getSuffix();
		this.title = user.getTitle();
		this.userId = user.getUserId();
		this.userTypeInd = user.getUserTypeInd();
		this.division = user.getDivision();
		this.mailCode = user.getMailCode();
		this.costCenter = user.getCostCenter();
		this.startDate = user.getStartDate();
		this.lastDate = user.getLastDate();
		this.nickname = user.getNickname();
		this.maidenName = user.getMaidenName();
		this.passwordTheme = user.getPasswordTheme();
		this.country = user.getCountry();
		this.bldgNum = user.getBldgNum();
		this.streetDirection = user.getStreetDirection();
		this.address1 = user.getAddress1();
		this.address2 = user.getAddress2();
		this.address3 = user.getAddress3();
		this.address4 = user.getAddress4();
		this.address5 = user.getAddress5();
		this.address6 = user.getAddress6();
		this.address7 = user.getAddress7();
		this.city = user.getCity();
		this.state = user.getState();
		this.postalCd = user.getPostalCd();
		this.email = user.getEmail();
		this.areaCd = user.getAreaCd();
		this.countryCd = user.getCountryCd();
		this.phoneNbr = user.getPhoneNbr();
		this.phoneExt = user.getPhoneExt();
		this.showInSearch = user.getShowInSearch();
		this.delAdmin = user.getDelAdmin();
		this.alternateContactId = user.getAlternateContactId();

		this.createdBy = user.getCreatedBy();
		this.startDate = user.getStartDate();
		this.lastDate = user.getLastDate();

		this.userOwnerId = user.getUserOwnerId();
		this.dateChallengeRespChanged = user.getDateChallengeRespChanged();
		this.datePasswordChanged = user.getDatePasswordChanged();

		userNotes = user.getUserNotes();
		userAttributes = user.getUserAttributes();
		phones = user.getPhones();
		addresses = user.getAddresses();
		// set the email address in a hibernate friendly manner

	}

	/**
	 * Extract a user object from the ProvisionUser object
	 * 
	 * @return
	 */
	public User getUser() {
		User user = new User();

		user.setBirthdate(birthdate);
		user.setCompanyId(companyId);
		user.setCompanyOwnerId(companyOwnerId);
		user.setCreateDate(createDate);
		user.setCreatedBy(createdBy);
		user.setDeptCd(deptCd);
		user.setDeptName(deptName);
		user.setEmployeeId(employeeId);
		user.setEmployeeType(employeeType);

		user.setFirstName(firstName);
		user.setJobCode(jobCode);
		user.setLastName(lastName);
		user.setLastUpdate(lastUpdate);
		user.setLastUpdatedBy(lastUpdatedBy);
		user.setLocationCd(locationCd);
		user.setLocationName(locationName);
		user.setManagerId(managerId);
		user.setMetadataTypeId(metadataTypeId);
		user.setClassification(classification);
		user.setMiddleInit(middleInit);
		user.setPrefix(prefix);
		user.setSex(sex);
		user.setStatus(status);
		user.setSecondaryStatus(secondaryStatus);
		user.setSuffix(suffix);
		user.setTitle(title);
		user.setUserId(userId);
		user.setUserTypeInd(userTypeInd);
		user.setDivision(division);
		user.setMailCode(mailCode);
		user.setCostCenter(costCenter);
		user.setStartDate(startDate);
		user.setLastDate(lastDate);
		user.setNickname(nickname);
		user.setMaidenName(maidenName);
		user.setPasswordTheme(passwordTheme);
		user.setCountry(country);
		user.setBldgNum(bldgNum);
		user.setStreetDirection(streetDirection);
		user.setAddress1(address1);
		user.setAddress2(address2);
		user.setAddress3(address3);
		user.setAddress4(address4);
		user.setAddress5(address5);
		user.setAddress6(address6);
		user.setAddress7(address3);
		user.setCity(city);
		user.setState(state);
		user.setPostalCd(postalCd);
		user.setEmail(email);
		user.setAreaCd(areaCd);
		user.setCountryCd(countryCd);
		user.setPhoneNbr(phoneNbr);
		user.setPhoneExt(phoneExt);

		user.setUserNotes(userNotes);
		user.setUserAttributes(userAttributes);
		user.setPhones(phones);
		user.setAddresses(addresses);
		user.setEmailAddresses(emailAddresses);
		user.setAlternateContactId(alternateContactId);
		user.setShowInSearch(showInSearch);
		user.setDelAdmin(delAdmin);

		user.setUserOwnerId(userOwnerId);
		user.setDateChallengeRespChanged(dateChallengeRespChanged);
		user.setDatePasswordChanged(datePasswordChanged);

		return user;
	}

	public Login getPrimaryPrincipal(String managedSysId) {
		if (principalList == null) {
			return null;
		}
		for (Login l : principalList) {
			if (l.getId().getManagedSysId().equals(managedSysId)) {
				return l;
			}
		}
		return null;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSrcSystemId() {
		return srcSystemId;
	}

	public void setSrcSystemId(String srcSystemId) {
		this.srcSystemId = srcSystemId;
	}

	public List<Group> getMemberOfGroups() {
		return memberOfGroups;
	}

	public void setMemberOfGroups(List<Group> memberOfGroups) {
		this.memberOfGroups = memberOfGroups;
	}

	public List<Role> getMemberOfRoles() {
		return memberOfRoles;
	}

	public List<Role> getActiveMemberOfRoles() {
		List<Role> activeRoleList = new ArrayList<Role>();
		if (memberOfRoles != null) {
			for (Role r : memberOfRoles) {
				if (r.getOperation() != AttributeOperationEnum.DELETE) {
					activeRoleList.add(r);
				}
			}
			return activeRoleList;
		}
		return null;
	}

	/**
	 * Its possible for the user to send service request which is missing most
	 * of the values that a User already has This can cause the provisioning
	 * scripts to fail
	 * 
	 * @param origUser
	 */
	public void updateMissingUserAttributes(User user) {

		if (birthdate == null) {
			birthdate = user.getBirthdate();
		}
		if (companyId == null) {
			companyId = user.getCompanyId();
		}
		if (companyOwnerId == null) {
			companyOwnerId = user.getCompanyOwnerId();
		}
		if (createDate == null) {
			createDate = user.getCreateDate();
		}
		if (createdBy == null) {
			createdBy = user.getCreatedBy();
		}
		if (deptCd == null) {
			deptCd = user.getDeptCd();
		}
		if (deptName == null) {
			deptName = user.getDeptName();
		}
		if (employeeId == null) {
			employeeId = user.getEmployeeId();
		}
		if (employeeType == null) {
			employeeType = user.getEmployeeType();
		}

		if (firstName == null) {
			firstName = user.getFirstName();
		}
		if (jobCode == null) {
			jobCode = user.getJobCode();
		}
		if (lastName == null) {
			lastName = user.getLastName();
		}
		if (lastUpdate == null) {
			lastUpdate = user.getLastUpdate();
		}
		if (lastUpdatedBy == null) {
			lastUpdatedBy = user.getLastUpdatedBy();
		}
		if (locationCd == null) {
			locationCd = user.getLocationCd();
		}
		if (locationName == null) {
			locationName = user.getLocationName();
		}
		if (managerId == null) {
			managerId = user.getManagerId();
		}
		if (metadataTypeId == null) {
			metadataTypeId = user.getMetadataTypeId();
		}
		if (classification == null) {
			classification = user.getClassification();
		}
		if (middleInit == null) {
			middleInit = user.getMiddleInit();
		}
		if (prefix == null) {
			prefix = user.getPrefix();
		}
		if (sex == null) {
			sex = user.getSex();
		}
		if (status == null) {
			status = user.getStatus();
		}
		if (secondaryStatus == null) {
			secondaryStatus = user.getSecondaryStatus();
		}
		if (suffix == null) {
			suffix = user.getSuffix();
		}
		if (title == null) {
			title = user.getTitle();
		}
		if (userTypeInd == null) {
			userTypeInd = user.getUserTypeInd();
		}
		if (division == null) {
			division = user.getDivision();
		}
		if (mailCode == null) {
			mailCode = user.getMailCode();
		}
		if (costCenter == null) {
			costCenter = user.getCostCenter();
		}
		if (startDate == null) {
			startDate = user.getStartDate();
		}
		if (lastDate == null) {
			lastDate = user.getLastDate();
		}
		if (nickname == null) {
			nickname = user.getNickname();
		}
		if (maidenName == null) {
			maidenName = user.getMaidenName();
		}
		if (passwordTheme == null) {
			passwordTheme = user.getPasswordTheme();
		}
		if (country == null) {
			country = user.getCountry();
		}
		if (bldgNum == null) {
			bldgNum = user.getBldgNum();
		}
		if (streetDirection == null) {
			streetDirection = user.getStreetDirection();
		}
		if (address1 == null) {
			address1 = user.getAddress1();
		}
		if (address2 == null) {
			address2 = user.getAddress2();
		}
		if (address3 == null) {
			address3 = user.getAddress3();
		}
		if (address4 == null) {
			address4 = user.getAddress4();
		}
		if (address5 == null) {
			address5 = user.getAddress5();
		}
		if (address6 == null) {
			address6 = user.getAddress6();
		}
		if (address7 == null) {
			address7 = user.getAddress7();
		}
		if (city == null) {
			city = user.getCity();
		}
		if (state == null) {
			state = user.getState();
		}
		if (postalCd == null) {
			postalCd = user.getPostalCd();
		}
		if (email == null) {
			email = user.getEmail();
		}
		if (areaCd == null) {
			areaCd = user.getAreaCd();
		}
		if (countryCd == null) {
			countryCd = user.getCountryCd();
		}
		if (phoneNbr == null) {
			phoneNbr = user.getPhoneNbr();
		}
		if (phoneExt == null) {
			phoneExt = user.getPhoneExt();
		}
		if (showInSearch == null) {
			showInSearch = user.getShowInSearch();
		}
		if (delAdmin == null) {
			delAdmin = user.getDelAdmin();
		}
		if (alternateContactId == null) {
			alternateContactId = user.getAlternateContactId();
		}

		if (createdBy == null) {
			createdBy = user.getCreatedBy();
		}
		if (startDate == null) {
			startDate = user.getStartDate();
		}
		if (lastDate == null) {
			lastDate = user.getLastDate();
		}

		if (userOwnerId == null) {
			userOwnerId = user.getUserOwnerId();
		}
		if (dateChallengeRespChanged == null) {
			dateChallengeRespChanged = user.getDateChallengeRespChanged();
		}
		if (datePasswordChanged == null) {
			datePasswordChanged = user.getDatePasswordChanged();
		}

	}

	public void setMemberOfRoles(List<Role> memberOfRoles) {
		this.memberOfRoles = memberOfRoles;
	}

	public ProvisionModelEnum getProvisionModel() {
		return provisionModel;
	}

	public void setProvisionModel(ProvisionModelEnum provisionModel) {
		this.provisionModel = provisionModel;
	}


	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isNotifyTargetSystems() {
		return notifyTargetSystems;
	}

	public void setNotifyTargetSystems(boolean notifyTargetSystems) {
		this.notifyTargetSystems = notifyTargetSystems;
	}

	@Override
	public String toString() {
		return "ProvisionUser{" + "memberOfGroups=" + memberOfGroups
				+ ", memberOfRoles=" + memberOfRoles + ", userAffiliations="
				+ userAffiliations + ", userResourceList=" + userResourceList
				+ ", provisionModel=" + provisionModel + ", emailCredentialsToNewUsers="
				+ emailCredentialsToNewUsers
				+ ", emailCredentialsToSupervisor="
				+ emailCredentialsToSupervisor
				+ ", addInitialPasswordToHistory="
				+ addInitialPasswordToHistory + ", provisionOnStartDate="
				+ provisionOnStartDate + ", requestId='" + requestId + '\''
				+ ", sessionId='" + sessionId + '\'' + ", skipPreprocessor="
				+ skipPreprocessor + ", skipPostProcessor=" + skipPostProcessor
				+ ", srcSystemId='" + srcSystemId + '\''
				+ ", notifyTargetSystems=" + notifyTargetSystems
				+ ", passwordPolicy=" + passwordPolicy + ", password='"
				+ password + '\'' + '}';
	}

	public boolean isEmailCredentialsToNewUsers() {
		return emailCredentialsToNewUsers;
	}

	public void setEmailCredentialsToNewUsers(boolean emailCredentialsToNewUsers) {
		this.emailCredentialsToNewUsers = emailCredentialsToNewUsers;
	}

	public boolean isEmailCredentialsToSupervisor() {
		return emailCredentialsToSupervisor;
	}

	public void setEmailCredentialsToSupervisor(
			boolean emailCredentialsToSupervisor) {
		this.emailCredentialsToSupervisor = emailCredentialsToSupervisor;
	}

	public List<Organization> getUserAffiliations() {
		return userAffiliations;
	}

	public void setUserAffiliations(List<Organization> userAffiliations) {
		this.userAffiliations = userAffiliations;
	}

	public boolean isProvisionOnStartDate() {
		return provisionOnStartDate;
	}

	public void setProvisionOnStartDate(boolean provisionOnStartDate) {
		this.provisionOnStartDate = provisionOnStartDate;
	}

	public boolean isAddInitialPasswordToHistory() {
		return addInitialPasswordToHistory;
	}

	public void setAddInitialPasswordToHistory(
			boolean addInitialPasswordToHistory) {
		this.addInitialPasswordToHistory = addInitialPasswordToHistory;
	}

	public List<UserResourceAssociation> getUserResourceList() {
		return userResourceList;
	}

	public void setUserResourceList(
			List<UserResourceAssociation> userResourceList) {
		this.userResourceList = userResourceList;
	}

	public Policy getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(Policy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSkipPreprocessor() {
		return skipPreprocessor;
	}

	public void setSkipPreprocessor(boolean skipPreprocessor) {
		this.skipPreprocessor = skipPreprocessor;
	}

	public boolean isSkipPostProcessor() {
		return skipPostProcessor;
	}

	public void setSkipPostProcessor(boolean skipPostProcessor) {
		this.skipPostProcessor = skipPostProcessor;
	}

	public String toXML() {
		XStream xstream = new XStream();
		return xstream.toXML(this);
	}
}
