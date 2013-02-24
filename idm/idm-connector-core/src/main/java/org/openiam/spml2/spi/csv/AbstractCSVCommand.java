package org.openiam.spml2.spi.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.spi.common.UserFields;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class AbstractCSVCommand implements ApplicationContextAware {
	protected static final Log log = LogFactory
			.getLog(AbstractCSVCommand.class);

	protected ManagedSystemDataService managedSysService;

	public void setPathToCSV(String pathToCSV) {
		this.pathToCSV = pathToCSV;
	}

	protected ResourceDataService resourceDataService;
	protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;
	private String pathToCSV;
	public static ApplicationContext ac;

	public void setManagedSysService(ManagedSystemDataService managedSysService) {
		this.managedSysService = managedSysService;
	}

	public void setResourceDataService(ResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}

	public void setManagedSysObjectMatchDao(
			ManagedSystemObjectMatchDAO managedSysObjectMatchDao) {
		this.managedSysObjectMatchDao = managedSysObjectMatchDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ac = ac;
	}

	protected List<CSVObject<ProvisionUser>> getUsersFromCSV(
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		return userParser.getObjectList(managedSys, attrMapList);
	}

	protected Map<String, String> getUserProvisionMap(
			CSVObject<ProvisionUser> obj, ManagedSys managedSys)
			throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		return userParser.convertToMap(attrMapList, obj);
	}

	protected void addUsersToCSV(String principal, ProvisionUser newUser,
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.addObjectToCSV(new CSVObject<ProvisionUser>(principal,
				newUser), managedSys, attrMapList);
	}

	protected void deleteUser(String principal, ProvisionUser newUser,
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.deleteObjectFromCSV(principal, managedSys, attrMapList);
	}

	protected void updateUser(CSVObject<ProvisionUser> newUser,
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.updateObjectFromCSV(newUser, managedSys, attrMapList);
	}

	protected boolean lookupObjectInCSV(String findValue,
			ManagedSys managedSys, List<ExtensibleObject> extOnjectList)
			throws Exception {
		List<CSVObject<ProvisionUser>> users = this.getUsersFromCSV(managedSys);
		List<ExtensibleAttribute> eAttr = new ArrayList<ExtensibleAttribute>(0);

		if (findValue.contains("=*")) {
			for (CSVObject<ProvisionUser> user : users) {
				ExtensibleObject extOnject = new ExtensibleObject();
				if (StringUtils.hasText(user.getPrincipal())) {
					List<ExtensibleAttribute> attrs = new ArrayList<ExtensibleAttribute>(
							0);
					Map<String, String> res = this.getUserProvisionMap(user,
							managedSys);
					for (String key : res.keySet())
						if (res.get(key) != null)
							attrs.add(new ExtensibleAttribute(key, user
									.getPrincipal()));
					extOnject.setAttributes(attrs);
					extOnjectList.add(extOnject);
				}
			}
			return true;
		}
		for (CSVObject<ProvisionUser> user : users) {
			ExtensibleObject extOnject = new ExtensibleObject();
			if (match(findValue, user, extOnject)) {
				Map<String, String> res = this.getUserProvisionMap(user,
						managedSys);
				for (String key : res.keySet())
					if (res.get(key) != null)
						eAttr.add(new ExtensibleAttribute(key, user
								.getPrincipal()));
				extOnject.setAttributes(eAttr);
				extOnjectList.add(extOnject);
				return true;
			}
		}
		return false;
	}

	protected boolean match(String findValue, CSVObject<ProvisionUser> user2,
			ExtensibleObject extOnject) {
		if (!StringUtils.hasText(findValue) || user2 == null) {
			return false;
		}
		if (findValue.equals(user2.getPrincipal())) {
			extOnject.setObjectId(user2.getPrincipal());
			return true;
		}
		return false;
	}
}
