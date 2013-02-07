package org.openiam.spml2.spi.csv;

import java.util.ArrayList;
import java.util.List;

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

	protected List<ProvisionUser> getUsersFromCSV(ManagedSys managedSys)
			throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		return userParser.getObjectList(managedSys, attrMapList);
	}

	protected void addUsersToCSV(ProvisionUser newUser, ManagedSys managedSys)
			throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.addObjectToCSV(newUser, managedSys, attrMapList);
	}

	protected void deleteUser(ProvisionUser newUser, ManagedSys managedSys)
			throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.deleteObjectFromCSV(newUser, managedSys, attrMapList);
	}

	protected void updateUser(ProvisionUser newUser, ManagedSys managedSys)
			throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.updateObjectFromCSV(newUser, managedSys, attrMapList);
	}

	protected boolean lookupObjectInCSV(String findValue,
			ManagedSys managedSys, ExtensibleObject extOnject) throws Exception {
		List<ProvisionUser> users = this.getUsersFromCSV(managedSys);
		for (ProvisionUser user : users) {
			if (match(findValue, user, extOnject)) {
				List<ExtensibleAttribute> eAttr = new ArrayList<ExtensibleAttribute>(
						0);
				eAttr.add(new ExtensibleAttribute("isFind", "true"));
				extOnject.setAttributes(eAttr);
				return true;
			}
		}
		return false;
	}

	protected boolean match(String findValue, ProvisionUser user2,
			ExtensibleObject extOnject) {
		if (!StringUtils.hasText(findValue) || user2 == null) {
			return false;
		}
		if (findValue.equals(user2.getFirstName() + " " + user2.getLastName())) {
			extOnject.setObjectId(user2.getUserId());
			return true;
		}
		return false;
	}
}
