package org.openiam.idm.srvc.recon.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.type.ExtensibleAttribute;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 27.04.12
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class DeleteIdmUserCommand implements ReconciliationCommand {
    private ProvisionService provisionService;
    private static final Log log = LogFactory.getLog(DeleteIdmUserCommand.class);

    public DeleteIdmUserCommand(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    public boolean execute(Login login, User user, List<ExtensibleAttribute> attributes) {
        log.debug("Entering DeleteIdmUserCommand");
        log.debug("Delete  user :" + login.getUserId());

        ProvisionUser pUser = new ProvisionUser();
        pUser.setUserId(login.getUserId());
        pUser.setNotifyTargetSystems(true);
        provisionService.deleteByUserId( pUser, UserStatusEnum.DELETED,"3000");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
