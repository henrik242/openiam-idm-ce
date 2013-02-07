package org.openiam.spml2.spi.csv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.LookupRequestType;
import org.openiam.spml2.msg.LookupResponseType;
import org.openiam.spml2.msg.ModifyRequestType;
import org.openiam.spml2.msg.ModifyResponseType;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.password.ExpirePasswordRequestType;
import org.openiam.spml2.msg.password.ResetPasswordRequestType;
import org.openiam.spml2.msg.password.ResetPasswordResponseType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.password.ValidatePasswordRequestType;
import org.openiam.spml2.msg.password.ValidatePasswordResponseType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService", targetNamespace = "http://www.openiam.org/service/connector", portName = "CSVConnectorServicePort", serviceName = "CSVConnectorService")
public class CSVConnectorImpl extends AbstractSpml2Complete implements
		ConnectorService, ApplicationContextAware {

	private AddCSVCommand addCommand;
	private TestCSVCommand testCommand;
	private LookupCSVCommand lookupCommand;
	private ModifyCSVCommand modifyCommand;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	@WebMethod
	public ResponseType reconcileResource(
			@WebParam(name = "config", targetNamespace = "") ReconciliationConfig config) {
		// TODO Auto-generated method stub
		return null;
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
