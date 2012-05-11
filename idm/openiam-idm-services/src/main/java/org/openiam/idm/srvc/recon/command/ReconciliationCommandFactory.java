package org.openiam.idm.srvc.recon.command;

import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.provision.service.ConnectorAdapter;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.service.RemoteConnectorAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 27.04.12
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class ReconciliationCommandFactory implements ApplicationContextAware, MuleContextAware{

    private static MuleContext muleContext;
    private static ApplicationContext applicationContext;

    public static ReconciliationCommand createCommand(String name, ReconciliationSituation config, String managedSysId) {
        ReconciliationCommand reconCommand = null;
        if(name.equalsIgnoreCase("NOTHING")){
            reconCommand = new DoNothingCommand();
        } else if(name.equalsIgnoreCase("DEL_RES_ACCOUNT")){
            reconCommand = new DeleteResourceAccountCommand((ProvisionService) applicationContext.getBean("defaultProvision"), (ManagedSystemDataService)applicationContext.getBean("managedSysService"), (ConnectorDataService)applicationContext.getBean("connectorService"), (RemoteConnectorAdapter)applicationContext.getBean("remoteConnectorAdapter"), muleContext, managedSysId, (ConnectorAdapter)applicationContext.getBean("connectorAdapter"));
        } else if(name.equalsIgnoreCase("DEL_IDM_ACCOUNT")){
            reconCommand = new DeleteIdmAccountCommand((ProvisionService) applicationContext.getBean("defaultProvision"));
        } else if(name.equalsIgnoreCase("DEL_IDM_USER")){
            reconCommand = new DeleteIdmUserCommand((ProvisionService) applicationContext.getBean("defaultProvision"));
        } else if(name.equalsIgnoreCase("DEL_IDM_USER-NOT_TARGET")){
            reconCommand = new DeleteIdmUserExcludeTargetCommand((ProvisionService) applicationContext.getBean("defaultProvision"));
        } else if(name.equalsIgnoreCase("CREATE_RES_ACCOUNT")){
            reconCommand = new CreateResourceAccountCommand((ProvisionService) applicationContext.getBean("defaultProvision"));
        } else if(name.equalsIgnoreCase("CREATE_IDM_ACCOUNT")){
            reconCommand = new CreateIdmAccountCommand((ProvisionService) applicationContext.getBean("defaultProvision"), config);
        } else if(name.equalsIgnoreCase("UPD_IDM_USER")){
            reconCommand = new UpdateIdmUserCommand((ProvisionService) applicationContext.getBean("defaultProvision"), config);
        }
        return reconCommand;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ReconciliationCommandFactory.applicationContext = applicationContext;
    }

    public void setMuleContext(MuleContext muleContext) {
        ReconciliationCommandFactory.muleContext = muleContext;
    }
}
