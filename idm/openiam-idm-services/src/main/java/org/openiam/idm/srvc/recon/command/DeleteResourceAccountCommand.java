package org.openiam.idm.srvc.recon.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.connector.type.RemoteUserRequest;
import org.openiam.connector.type.UserRequest;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ConnectorAdapter;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.service.RemoteConnectorAdapter;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.PSOIdentifierType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 27.04.12
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class DeleteResourceAccountCommand implements ReconciliationCommand {
    private ProvisionService provisionService;
    private static final Log log = LogFactory.getLog(DeleteResourceAccountCommand.class);
    private ManagedSystemDataService managedSysService;
    private ConnectorDataService connectorService;
    private RemoteConnectorAdapter remoteConnectorAdapter;
    private MuleContext muleContext;
    private String managedSysId;
    private ConnectorAdapter connectorAdapter;

    public DeleteResourceAccountCommand(ProvisionService provisionService, ManagedSystemDataService managedSysService, ConnectorDataService connectorService, RemoteConnectorAdapter remoteConnectorAdapter, MuleContext muleContext, String managedSysId, ConnectorAdapter connectorAdapter) {
        this.provisionService = provisionService;
        this.managedSysService = managedSysService;
        this.connectorService = connectorService;
        this.remoteConnectorAdapter = remoteConnectorAdapter;
        this.muleContext = muleContext;
        this.managedSysId = managedSysId;
        this.connectorAdapter = connectorAdapter;
    }

    public boolean execute(Login login, User user, List<ExtensibleAttribute> attributes) {
        log.debug("Entering DeleteResourceAccountCommand");
        if(user == null) {
            ManagedSys mSys = managedSysService.getManagedSys(managedSysId);
            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

            if (connector.getConnectorInterface() != null &&
                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                log.debug("Calling delete with Remote connector");
                RemoteUserRequest request = new RemoteUserRequest();
                request.setUserIdentity(login.getId().getLogin());
                request.setTargetID(login.getId().getManagedSysId());
                request.setHostLoginId(mSys.getUserId());
                request.setHostLoginPassword(mSys.getDecryptPassword());
                request.setHostUrl(mSys.getHostUrl());
                request.setScriptHandler(mSys.getDeleteHandler());
                remoteConnectorAdapter.deleteRequest(mSys, request, connector, muleContext);
            } else {
                DeleteRequestType reqType = new DeleteRequestType();
                PSOIdentifierType idType = new PSOIdentifierType(login.getId().getLogin(), null, managedSysId);
                reqType.setPsoID(idType);
                log.debug("Calling delete local connector");
                connectorAdapter.deleteRequest(mSys, reqType, muleContext);
            }
            return true;
        }
        List<Login> principleList = user.getPrincipalList();
        for(Login l : principleList){
            System.out.println("Checking login");
            if(l.getId().equals(login.getId())){
                l.setOperation(AttributeOperationEnum.DELETE);
                System.out.println("Set to delete");
                break;
            }
        }

        ProvisionUser pUser = new ProvisionUser(user);
        pUser.setPrincipalList(principleList);

        provisionService.modifyUser(pUser);
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
