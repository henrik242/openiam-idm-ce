package org.openiam.idm.srvc.recon.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.type.ExtensibleAttribute;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 27.04.12
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class DeleteIdmAccountCommand implements ReconciliationCommand {
    private ProvisionService provisionService;
    private static final Log log = LogFactory.getLog(DeleteIdmAccountCommand.class);

    public DeleteIdmAccountCommand(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    public boolean execute(Login login, User user, List<ExtensibleAttribute> attributes) {
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
