package org.openiam.spml2.spi.csv;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mvel2.optimizers.impl.refl.nodes.ArrayLength;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.spml2.spi.common.UserFields;

public class UserCSVParser extends AbstractCSVParser<ProvisionUser, UserFields> {

	public UserCSVParser() {
		super();
	}

	public UserCSVParser(String path) {
		super(path);
	}

	@Override
	protected void putValueInDTO(ProvisionUser user, UserFields field,
			String objValue) {
		switch (field) {
		case birthdate:
			try {
				user.setBirthdate(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setBirthdate(null);
			}
			break;
		case companyId:
			user.setCompanyId(objValue);
			break;
		case companyOwnerId:
			user.setCompanyOwnerId(objValue);
			break;
		case createDate:
			user.setCreateDate(Date.valueOf(objValue));
			break;
		case createdBy:
			user.setCreatedBy(objValue);
			break;
		case deptCd:
			user.setDeptCd(objValue);
			break;
		case deptName:
			user.setDeptName(objValue);
			break;
		case employeeId:
			user.setEmployeeId(objValue);
			break;
		case employeeType:
			user.setEmployeeType(objValue);
			break;
		case firstName:
			user.setFirstName(objValue);
			break;
		case jobCode:
			user.setJobCode(objValue);
			break;
		case lastName:
			user.setLastName(objValue);
			break;
		case lastUpdate:
			try {
				user.setLastUpdate(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setLastUpdate(null);
			}
			break;
		case lastUpdatedBy:
			user.setLastUpdatedBy(objValue);
			break;
		case locationCd:
			user.setLocationCd(objValue);
			break;
		case locationName:
			user.setLocationName(objValue);
			break;
		case managerId:
			user.setManagerId(objValue);
			break;
		case metadataTypeId:
			user.setMetadataTypeId(objValue);
			break;
		case classification:
			user.setClassification(objValue);
			break;
		case middleInit:
			user.setMiddleInit(objValue);
			break;
		case prefix:
			user.setPrefix(objValue);
			break;
		case sex:
			user.setSex(objValue);
			break;
		case status:
			user.setStatus(Enum.valueOf(UserStatusEnum.class,
					objValue.toUpperCase()));
			break;
		case secondaryStatus:
			user.setSecondaryStatus(Enum.valueOf(UserStatusEnum.class,
					objValue.toUpperCase()));
			break;
		case suffix:
			user.setSuffix(objValue);
			break;
		case title:
			user.setTitle(objValue);
			break;
		case uid:
		case userId:
			user.setUserId(objValue);
			break;
		case userTypeInd:
			user.setUserTypeInd(objValue);
			break;
		case userNotes:
		case emailAddresses:
		case userAttributes:
			break;
		case division:
			user.setDivision(objValue);
			break;
		case costCenter:
			user.setCostCenter(objValue);
			break;
		case startDate:
			try {
				user.setStartDate(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setStartDate(null);
			}
			break;
		case lastDate:
			try {
				user.setLastDate(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setLastDate(null);
			}
			break;
		case mailCode:
			user.setMailCode(objValue);
			break;
		case nickname:
			user.setNickname(objValue);
			break;
		case maidenName:
			user.setMaidenName(objValue);
			break;
		case passwordTheme:
			user.setPasswordTheme(objValue);
			break;
		case country:
			user.setCountry(objValue);
			break;
		case bldgNum:
			user.setBldgNum(objValue);
			break;
		case streetDirection:
			user.setStreetDirection(objValue);
			break;
		case suite:
			user.setSuite(objValue);
			break;
		case address1:
			user.setAddress1(objValue);
			break;
		case address2:
			user.setAddress2(objValue);
			break;
		case address3:
			user.setAddress3(objValue);
			break;
		case address4:
			user.setAddress4(objValue);
			break;
		case address5:
			user.setAddress5(objValue);
			break;
		case address6:
			user.setAddress6(objValue);
			break;
		case address7:
			user.setAddress7(objValue);
			break;
		case city:
			user.setCity(objValue);
			break;
		case state:
			user.setState(objValue);
			break;
		case postalCd:
			user.setPostalCd(objValue);
			break;
		case mail:
		case email:
			user.setEmail(objValue);
			break;
		case showInSearch:
			try {
				user.setShowInSearch(Integer.valueOf(objValue));
			} catch (Exception e) {
				user.setShowInSearch(null);
			}
			break;
		case delAdmin:
			try {
				user.setDelAdmin(Integer.valueOf(objValue));
			} catch (Exception e) {
				user.setDelAdmin(null);
			}
			break;
		case areaCd:
			user.setAreaCd(objValue);
			break;
		case countryCd:
			user.setCountryCd(objValue);
			break;
		case phoneNbr:
			user.setPhoneNbr(objValue);
			break;
		case phoneExt:
			user.setPhoneExt(objValue);
			break;
		case principalList:
		case phones:
		case supervisor:
			break;
		case alternateContactId:
			user.setAlternateContactId(objValue);
			break;
		case securityDomain:
			user.setSecurityDomain(objValue);
			break;
		case userOwnerId:
			user.setUserOwnerId(objValue);
			break;
		case datePasswordChanged:
			try {
				user.setDatePasswordChanged(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setDatePasswordChanged(null);
			}
			break;
		case dateChallengeRespChanged:
			try {
				user.setDateChallengeRespChanged(Date.valueOf(objValue));
			} catch (Exception e) {
				user.setDateChallengeRespChanged(null);
			}
			break;
		case DEFAULT:
			break;
		default:
			break;

		}

	}

	@Override
	protected String putValueIntoString(ProvisionUser user, UserFields field) {
		String objValue = "";
		switch (field) {
		case birthdate:
			objValue = convertToString(user.getBirthdate());
			break;
		case companyId:
			objValue = convertToString(user.getCompanyId());
			break;
		case companyOwnerId:
			objValue = convertToString(user.getCompanyOwnerId());
			break;
		case createDate:
			objValue = convertToString(user.getCreateDate());
			break;
		case createdBy:
			objValue = convertToString(user.getCreatedBy());
			break;
		case deptCd:
			objValue = convertToString(user.getDeptCd());
			break;
		case deptName:
			objValue = convertToString(user.getDeptName());
			break;
		case employeeId:
			objValue = convertToString(user.getEmployeeId());
			break;
		case employeeType:
			objValue = convertToString(user.getEmployeeType());
			break;
		case firstName:
			objValue = convertToString(user.getFirstName());
			break;
		case jobCode:
			objValue = convertToString(user.getJobCode());
			break;
		case lastName:
			objValue = convertToString(user.getLastName());
			break;
		case lastUpdate:
			objValue = convertToString(user.getLastUpdate());
			break;
		case lastUpdatedBy:
			objValue = convertToString(user.getLastUpdatedBy());
			break;
		case locationCd:
			objValue = convertToString(user.getLocationCd());
			break;
		case locationName:
			objValue = convertToString(user.getLocationName());
			break;
		case managerId:
			objValue = convertToString(user.getManagerId());
			break;
		case metadataTypeId:
			objValue = convertToString(user.getMetadataTypeId());
			break;
		case classification:
			objValue = convertToString(user.getClassification());
			break;
		case middleInit:
			objValue = convertToString(user.getMiddleInit());
			break;
		case prefix:
			objValue = convertToString(user.getPrefix());
			break;
		case sex:
			objValue = convertToString(user.getSex());
			break;
		case status:
			objValue = convertToString(user.getStatus());
			break;
		case secondaryStatus:
			objValue = convertToString(user.getSecondaryStatus());
			break;
		case suffix:
			objValue = convertToString(user.getSuffix());
			break;
		case title:
			objValue = convertToString(user.getTitle());
			break;
		case uid:
		case userId:
			objValue = convertToString(user.getUserId());
			break;
		case userTypeInd:
			objValue = convertToString(user.getUserTypeInd());
			break;
		case division:
			objValue = convertToString(user.getDivision());
			break;
		case costCenter:
			objValue = convertToString(user.getCostCenter());
			break;
		case startDate:
			objValue = convertToString(user.getStartDate());
			break;
		case lastDate:
			objValue = convertToString(user.getLastDate());
			break;
		case mailCode:
			objValue = convertToString(user.getMailCode());
			break;
		case nickname:
			objValue = convertToString(user.getNickname());
			break;
		case maidenName:
			objValue = convertToString(user.getMaidenName());
			break;
		case passwordTheme:
			objValue = convertToString(user.getPasswordTheme());
			break;
		case country:
			objValue = convertToString(user.getCountry());
			break;
		case bldgNum:
			objValue = convertToString(user.getBldgNum());
			break;
		case streetDirection:
			objValue = convertToString(user.getStreetDirection());
			break;
		case suite:
			objValue = convertToString(user.getSuite());
			break;
		case address1:
			objValue = convertToString(user.getAddress1());
			break;
		case address2:
			objValue = convertToString(user.getAddress2());
			break;
		case address3:
			objValue = convertToString(user.getAddress3());
			break;
		case address4:
			objValue = convertToString(user.getAddress4());
			break;
		case address5:
			objValue = convertToString(user.getAddress5());
			break;
		case address6:
			objValue = convertToString(user.getAddress6());
			break;
		case address7:
			objValue = convertToString(user.getAddress7());
			break;
		case city:
			objValue = convertToString(user.getCity());
			break;
		case state:
			objValue = convertToString(user.getState());
			break;
		case postalCd:
			objValue = convertToString(user.getPostalCd());
			break;
		case mail:
		case email:
			objValue = convertToString(user.getEmail());
			break;
		case showInSearch:
			objValue = convertToString(user.getShowInSearch());
			break;
		case delAdmin:
			objValue = convertToString(user.getDelAdmin());
			break;
		case areaCd:
			objValue = convertToString(user.getAreaCd());
			break;
		case countryCd:
			objValue = convertToString(user.getCountryCd());
			break;
		case phoneNbr:
			objValue = convertToString(user.getPhoneNbr());
			break;
		case phoneExt:
			objValue = convertToString(user.getPhoneExt());
			break;
		case alternateContactId:
			objValue = convertToString(user.getAlternateContactId());
			break;
		case securityDomain:
			objValue = convertToString(user.getSecurityDomain());
			break;
		case userOwnerId:
			objValue = convertToString(user.getUserOwnerId());
			break;
		case datePasswordChanged:
			objValue = convertToString(user.getDatePasswordChanged());
			break;
		case dateChallengeRespChanged:
			objValue = convertToString(user.getDateChallengeRespChanged());
			break;
		case userAttributes:
		case principalList:
		case supervisor:
		case userNotes:
		case phones:
		case emailAddresses:
			break;
		case DEFAULT:
			objValue = convertToString("");
			break;
		default:
			break;
		}
		return objValue;
	}

	protected List<CSVObject<ProvisionUser>> getObjectList(
			ManagedSys managedSys, List<AttributeMap> attrMapList)
			throws Exception {
		return getObjectList(managedSys, attrMapList, ProvisionUser.class,
				UserFields.class);
	}

	public void addObjectToCSV(CSVObject<ProvisionUser> newObject,
			ManagedSys managedSys, List<AttributeMap> attrMapList)
			throws Exception {
		appendObjectToCSV(newObject, managedSys, attrMapList,
				ProvisionUser.class, UserFields.class, true);
	}

	public void updateCSV(List<CSVObject<ProvisionUser>> newObject,
			ManagedSys managedSys, List<AttributeMap> attrMapList,
			boolean append) throws Exception {
		updateCSV(newObject, managedSys, attrMapList, ProvisionUser.class,
				UserFields.class, append);
	}

	public void deleteObjectFromCSV(String principal, ManagedSys managedSys,
			List<AttributeMap> attrMapList) throws Exception {
		List<CSVObject<ProvisionUser>> users = this.getObjectList(managedSys,
				attrMapList);
		Iterator<CSVObject<ProvisionUser>> userIter = users.iterator();
		while (userIter.hasNext()) {
			CSVObject<ProvisionUser> user = userIter.next();
			if (principal != null) {
				if (principal.equals(user.getPrincipal())) {
					userIter.remove();
				}
			}
		}
		this.updateCSV(users, managedSys, attrMapList, false);
	}

	public void updateObjectFromCSV(CSVObject<ProvisionUser> newUser,
			ManagedSys managedSys, List<AttributeMap> attrMapList)
			throws Exception {
		List<CSVObject<ProvisionUser>> users = this.getObjectList(managedSys,
				attrMapList);
		List<CSVObject<ProvisionUser>> newUsers = new ArrayList<CSVObject<ProvisionUser>>(
				0);
		for (CSVObject<ProvisionUser> user : users) {
			if (newUser.getPrincipal().equals(user.getPrincipal())) {
				newUsers.add(newUser);
			} else {
				newUsers.add(user);
			}
		}
		this.updateCSV(newUsers, managedSys, attrMapList, false);
	}

	public Map<String, String> convertToMap(List<AttributeMap> attrMap,
			CSVObject<ProvisionUser> obj) {
		return super.convertToMap(attrMap, obj, UserFields.class);
	}
}
