package org.openiam.spml2.spi.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTML;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.CollectionUtils;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.recon.command.ReconciliationCommandFactory;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.idm.srvc.recon.dto.ReconciliationResponse;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.ReconciliationHTMLReport;
import org.openiam.spml2.msg.ReconciliationHTMLReportResults;
import org.openiam.spml2.msg.ReconciliationHTMLRow;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.spi.common.UserFields;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class AbstractCSVCommand implements ApplicationContextAware {
	protected static final Log log = LogFactory
			.getLog(AbstractCSVCommand.class);

	protected ManagedSystemDataService managedSysService;

	protected UserDataService userMgr;

	protected ResourceDataService resourceDataService;
	protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;
	protected String pathToCSV;
	public static ApplicationContext ac;

	public void setUserMgr(UserDataService userMgr) {
		this.userMgr = userMgr;
	}

	public void setPathToCSV(String pathToCSV) {
		this.pathToCSV = pathToCSV;
	}

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

	protected ResponseType reconcile(ReconciliationConfig config) {
		ResponseType response = new ResponseType();
		ReconciliationHTMLReport result = new ReconciliationHTMLReport();
		List<ReconciliationHTMLRow> report = new ArrayList<ReconciliationHTMLRow>();
		result.setReport(report);
		Resource res = resourceDataService.getResource(config.getResourceId());
		String managedSysId = res.getManagedSysId();
		ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

		Map<String, ReconciliationCommand> situations = new HashMap<String, ReconciliationCommand>();
		for (ReconciliationSituation situation : config.getSituationSet()) {
			situations.put(situation.getSituation().trim(),
					ReconciliationCommandFactory.createCommand(
							situation.getSituationResp(), situation,
							managedSysId));
			log.debug("Created Command for: " + situation.getSituation());
		}

		UserCSVParser parserIDM = new UserCSVParser(pathToCSV);
		UserCSVParser parserSource = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(mSys.getResourceId());
		List<CSVObject<ProvisionUser>> idmUsers;
		List<CSVObject<ProvisionUser>> sourceUsers;
		try {
			idmUsers = parserIDM.getObjectListFromIDMCSV(mSys, attrMapList);
			sourceUsers = parserSource.getObjectListFromSourceCSV(mSys,
					attrMapList);
			// Fill header
			StringBuilder header = new StringBuilder();
			List<String> hList = new ArrayList<String>(0);
			for (AttributeMap map : attrMapList) {
				hList.add(map.getAttributeName());
				header.append(map.getAttributeName());
				header.append(",");
			}
			header.deleteCharAt(header.length() - 1);
			report.add(new ReconciliationHTMLRow(header.toString()));
			log.info("Generate headred " + header.toString()); // -----------------------------------------------------------
			// First run from IDM search in Sourse
			String preffix = "IDM: ";
			report.add(new ReconciliationHTMLRow("Records from IDM: "
					+ idmUsers.size() + " items", hList.size() + 1));
			for (CSVObject<ProvisionUser> u : idmUsers) {
				log.info("User " + u.toString());
				if (u.getObject() == null || u.getPrincipal() == null) {
					log.warn("Skip USER" + u.toString()
							+ " key or objecy is NULL");
					if (u.getObject() != null) {
						report.add(new ReconciliationHTMLRow(preffix
								+ ReconciliationHTMLReportResults.BROKEN_CSV
										.getValue(), this.objectToString(hList,
								parserIDM.convertToMap(attrMapList, u))));
					}
					continue;
				}

				Login l = null;
				List<User> users = userMgr.search(parserIDM.userSearch(
						u.getPrincipal(), attrMapList));
				if (CollectionUtils.isEmpty(users)) {
					report.add(new ReconciliationHTMLRow(
							preffix
									+ ReconciliationHTMLReportResults.NOT_EXIST_IN_IDM_DB
											.getValue(), this.objectToString(
									hList,
									parserIDM.convertToMap(attrMapList, u))));
					continue;
				}
				if (users.size() > 1) {
					report.add(new ReconciliationHTMLRow(preffix
							+ ReconciliationHTMLReportResults.NOT_QNIQUE_KEY
									.getValue(), this.objectToString(hList,
							parserIDM.convertToMap(attrMapList, u))));
					continue;
				}
				List<Login> logins = users.get(0).getPrincipalList();
				if (logins != null) {
					for (Login login : logins) {
						if (login.getId().getDomainId()
								.equalsIgnoreCase(mSys.getDomainId())
								&& login.getId().getManagedSysId()
										.equalsIgnoreCase(managedSysId)) {
							l = login;
							break;
						}
					}
				}
				if (l == null) {
					if (users.get(0).getStatus().equals(UserStatusEnum.DELETED)) {
						for (CSVObject<ProvisionUser> sourceUser : sourceUsers) {
							if (u.getPrincipal().endsWith(
									sourceUser.getPrincipal())) {
								report.add(new ReconciliationHTMLRow(
										preffix
												+ ReconciliationHTMLReportResults.IDM_DELETED
														.getValue(),
										this.objectToString(hList, parserIDM
												.convertToMap(attrMapList, u))));
								break;
							}
						}
						continue;
					}
					report.add(new ReconciliationHTMLRow(preffix
							+ ReconciliationHTMLReportResults.LOGIN_NOT_FOUND
									.getValue(), this.objectToString(hList,
							parserIDM.convertToMap(attrMapList, u))));
					continue;
				} else {
					boolean isFind = false;
					for (CSVObject<ProvisionUser> sourceUser : sourceUsers) {
						if (u.getPrincipal()
								.trim()
								.equalsIgnoreCase(
										sourceUser.getPrincipal().trim())) {

							report.add(new ReconciliationHTMLRow(
									preffix
											+ ReconciliationHTMLReportResults.MATCH_FOUND
													.getValue(),
									this.objectToString(
											hList,
											matchFields(parserIDM.convertToMap(
													attrMapList, u), parserIDM
													.convertToMap(attrMapList,
															sourceUser)))));
							isFind = true;
							break;
						}
					}
					if (!isFind) {
						report.add(new ReconciliationHTMLRow(
								preffix
										+ ReconciliationHTMLReportResults.RESOURSE_DELETED
												.getValue(), this
										.objectToString(hList, parserIDM
												.convertToMap(attrMapList, u))));
					}
				}
			}
			// -----------------------------------------------
			// Second run from Sourse search in IDM
			report.add(new ReconciliationHTMLRow("Records from Remove CSV: "
					+ sourceUsers.size() + " items", hList.size() + 1));
			preffix = "Source: ";
			for (CSVObject<ProvisionUser> u : sourceUsers) {
				boolean isFind = false;
				if (u.getObject() == null || u.getPrincipal() == null) {
					log.warn("Skip USER" + u.toString()
							+ " key or object is NULL");
					if (u.getObject() != null) {

						report.add(new ReconciliationHTMLRow(preffix
								+ ReconciliationHTMLReportResults.BROKEN_CSV
										.getValue(), this.objectToString(hList,
								parserIDM.convertToMap(attrMapList, u))));

					}
					continue;
				}

				for (CSVObject<ProvisionUser> sourceUser : idmUsers) {
					if (u.getPrincipal().trim()
							.equalsIgnoreCase(sourceUser.getPrincipal().trim())) {
						isFind = true;
						report.add(new ReconciliationHTMLRow(preffix
								+ ReconciliationHTMLReportResults.MATCH_FOUND
										.getValue(), this.objectToString(
								hList,
								matchFields(parserIDM.convertToMap(attrMapList,
										u), parserIDM.convertToMap(attrMapList,
										sourceUser)))));
						break;
					}
				}
				if (!isFind) {
					report.add(new ReconciliationHTMLRow(
							preffix
									+ ReconciliationHTMLReportResults.NOT_EXIST_IN_IDM_DB
											.getValue(), this.objectToString(
									hList,
									parserIDM.convertToMap(attrMapList, u))));
				}
			}
			// -----------------------------------------------
		} catch (Exception e) {
			log.error(e);
			response.setStatus(StatusCodeType.FAILURE);
			response.setErrorMessage(e.getMessage() + e.getStackTrace());
			return response;
		}
		try {
			result.save(pathToCSV, mSys);
		} catch (IOException e) {
			log.error("can't save report");
			response.setStatus(StatusCodeType.FAILURE);
			response.setErrorMessage(e.getMessage() + e.getStackTrace());
		}
		return response;
	}

	private String objectToString(List<String> head, Map<String, String> obj) {
		StringBuilder stb = new StringBuilder();
		for (String h : head) {
			stb.append(obj.get(h.trim()) == null ? "" : obj.get(h));
			stb.append(",");
		}
		stb.deleteCharAt(stb.length() - 1);
		return stb.toString();
	}

	private Map<String, String> matchFields(Map<String, String> one,
			Map<String, String> two) {
		Map<String, String> res = new HashMap<String, String>(0);
		for (String field : one.keySet()) {

			if (one.get(field) == null && two.get(field) == null) {
				res.put(field, null);
				continue;
			}
			if (one.get(field) == null && two.get(field) != null) {
				res.put(field, two.get(field));
				continue;
			}
			if (one.get(field) != null && two.get(field) == null) {
				res.put(field, one.get(field));
				continue;
			}
			if (one.get(field) != null && two.get(field) != null) {

				res.put(field,
						one.get(field).equals(two.get(field)) ? one.get(field)
								: ("[" + one.get(field) + "][" + two.get(field))
										+ "]");
				continue;
			}
		}

		return res;
	}

	protected List<CSVObject<ProvisionUser>> getUsersFromCSV(
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		return userParser.getObjectListFromIDMCSV(managedSys, attrMapList);
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
		userParser.addObjectToIDMCSV(new CSVObject<ProvisionUser>(principal,
				newUser), managedSys, attrMapList);
	}

	protected void deleteUser(String principal, ProvisionUser newUser,
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.deleteObjectFromIDMCSV(principal, managedSys, attrMapList);
	}

	protected void updateUser(CSVObject<ProvisionUser> newUser,
			ManagedSys managedSys) throws Exception {
		UserCSVParser userParser = new UserCSVParser(pathToCSV);
		List<AttributeMap> attrMapList = managedSysService
				.getResourceAttributeMaps(managedSys.getResourceId());
		userParser.updateObjectFromIDMCSV(newUser, managedSys, attrMapList);
	}

	protected boolean lookupObjectInCSV(String findValue,
			ManagedSys managedSys, List<ExtensibleObject> extOnjectList)
			throws Exception {
		List<CSVObject<ProvisionUser>> users = this.getUsersFromCSV(managedSys);
		List<ExtensibleAttribute> eAttr = new ArrayList<ExtensibleAttribute>(0);

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
