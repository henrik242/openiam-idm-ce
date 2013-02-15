package org.openiam.spml2.spi.csv;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.recon.command.ReconciliationCommandFactory;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.LookupRequestType;
import org.openiam.spml2.msg.LookupResponseType;
import org.openiam.spml2.msg.ModifyRequestType;
import org.openiam.spml2.msg.ModifyResponseType;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.msg.password.ExpirePasswordRequestType;
import org.openiam.spml2.msg.password.ResetPasswordRequestType;
import org.openiam.spml2.msg.password.ResetPasswordResponseType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.password.ValidatePasswordRequestType;
import org.openiam.spml2.msg.password.ValidatePasswordResponseType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.spml2.spi.ldap.LdapConnectorImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService", targetNamespace = "http://www.openiam.org/service/connector", portName = "CSVConnectorServicePort", serviceName = "CSVConnectorService")
public class CSVConnectorImpl extends AbstractSpml2Complete implements
		ConnectorService, ApplicationContextAware {
	private static final Log log = LogFactory.getLog(CSVConnectorImpl.class);

	private AddCSVCommand addCommand;
	private TestCSVCommand testCommand;
	private LookupCSVCommand lookupCommand;
	private ModifyCSVCommand modifyCommand;

	private ResourceDataService resourceDataService;
	private ManagedSystemDataService managedSysService;

	public void setResourceDataService(ResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}

	public void setManagedSysService(ManagedSystemDataService managedSysService) {
		this.managedSysService = managedSysService;
	}

	public void setLoginManager(LoginDataService loginManager) {
		this.loginManager = loginManager;
	}

	private LoginDataService loginManager;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	@WebMethod
	public ResponseType reconcileResource(
			@WebParam(name = "config", targetNamespace = "") ReconciliationConfig config) {
		log.debug("reconcile resource called in LDAPConnector");

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

		ResponseType response = new ResponseType();
		response.setStatus(StatusCodeType.SUCCESS);

		LookupRequestType request = new LookupRequestType();
		ManagedSystemObjectMatch[] matchObjAry = managedSysService
				.managedSysObjectParam(managedSysId, "USER");
		if (matchObjAry.length == 0) {
			log.error("No match object found for this managed sys");
			response.setStatus(StatusCodeType.FAILURE);
			return response;
		}
		String keyField = matchObjAry[0].getKeyField();
		String searchString = keyField + "=*";
		PSOIdentifierType idType = new PSOIdentifierType(searchString, null,
				managedSysId);
		request.setPsoID(idType);

		LookupResponseType responseType = lookup(request);

		if (responseType.getStatus() == StatusCodeType.FAILURE) {
			response.setStatus(StatusCodeType.FAILURE);
			return response;
		}

		if (responseType.getAny() != null && responseType.getAny().size() != 0) {
			for (ExtensibleObject obj : responseType.getAny()) {
				log.debug("Reconcile Found User");
				String principal = null;
				String searchPrincipal = null;
				for (ExtensibleAttribute attr : obj.getAttributes()) {
					if (attr.getName().equalsIgnoreCase(keyField)) {
						principal = attr.getValue();
						break;
					}
				}
				if (principal != null) {
					log.debug("reconcile principle found");

					Login login = loginManager.getLoginByManagedSys(
							mSys.getDomainId(), principal, managedSysId);
					if (login == null) {
						log.debug("Situation: IDM Not Found");
						DeleteRequestType delete = new DeleteRequestType();
						idType = new PSOIdentifierType(searchPrincipal, null,
								managedSysId);
						delete.setPsoID(idType);
						delete(delete);
						Login l = new Login();
						LoginId id = new LoginId();
						id.setDomainId(mSys.getDomainId());
						id.setLogin(principal);
						id.setManagedSysId(managedSysId);
						l.setId(id);
						ReconciliationCommand command = situations
								.get("IDM Not Found");
						if (command != null) {
							log.debug("Call command for IDM Not Found");
							command.execute(l, null, obj.getAttributes());
						}
					}
				}
			}
		}
		return response; // To change body of implemented methods use File |
							// Settings | File Templates.
	}

	@Override
	@WebMethod
	public ResponseType testConnection(
			@WebParam(name = "managedSys", targetNamespace = "") ManagedSys managedSys) {
		return testCommand.test(managedSys);
	}

	public void setTestCommand(TestCSVCommand testCommand) {
		this.testCommand = testCommand;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/add")
	public AddResponseType add(
			@WebParam(name = "reqType", targetNamespace = "") AddRequestType reqType) {
		return addCommand.add(reqType);
	}

	public void setLookupCommand(LookupCSVCommand lookupCommand) {
		this.lookupCommand = lookupCommand;
	}

	public void setModifyCommand(ModifyCSVCommand modifyCommand) {
		this.modifyCommand = modifyCommand;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/modify")
	public ModifyResponseType modify(
			@WebParam(name = "reqType", targetNamespace = "") ModifyRequestType reqType) {
		return modifyCommand.modify(reqType);
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/delete")
	public ResponseType delete(
			@WebParam(name = "reqType", targetNamespace = "") DeleteRequestType reqType) {
		return modifyCommand.delete(reqType);
	}

	public void setAddCommand(AddCSVCommand addCommand) {
		this.addCommand = addCommand;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/lookup")
	public LookupResponseType lookup(
			@WebParam(name = "reqType", targetNamespace = "") LookupRequestType reqType) {
		return lookupCommand.lookup(reqType);
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/setPassword")
	public ResponseType setPassword(
			@WebParam(name = "request", targetNamespace = "") SetPasswordRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/expirePassword")
	public ResponseType expirePassword(
			@WebParam(name = "request", targetNamespace = "") ExpirePasswordRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/resetPassword")
	public ResetPasswordResponseType resetPassword(
			@WebParam(name = "request", targetNamespace = "") ResetPasswordRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/validatePassword")
	public ValidatePasswordResponseType validatePassword(
			@WebParam(name = "request", targetNamespace = "") ValidatePasswordRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/suspend")
	public ResponseType suspend(
			@WebParam(name = "request", targetNamespace = "") SuspendRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(action = "http://www.openiam.org/service/connector/ConnectorService/resume")
	public ResponseType resume(
			@WebParam(name = "request", targetNamespace = "") ResumeRequestType request) {
		// TODO Auto-generated method stub
		return null;
	}

}
