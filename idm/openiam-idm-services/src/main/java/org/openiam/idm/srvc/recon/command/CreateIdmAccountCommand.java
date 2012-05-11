package org.openiam.idm.srvc.recon.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.recon.service.PopulationScript;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.LookupUserResponse;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 27.04.12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class CreateIdmAccountCommand implements ReconciliationCommand {
    private ProvisionService provisionService;
    private ReconciliationSituation config;
    private static final Log log = LogFactory.getLog(CreateIdmAccountCommand.class);
    private static String scriptEngine = "org.openiam.script.GroovyScriptEngineIntegration";

    public CreateIdmAccountCommand(ProvisionService provisionService, ReconciliationSituation config) {
        this.provisionService = provisionService;
        this.config = config;
    }

    public boolean execute(Login login, User user, List<ExtensibleAttribute> attributes) {
        log.debug("Entering CreateIdmAccountCommand");
        if(attributes == null){
            log.debug("Can't create IDM user without attributes");
        } else {
            Map<String, String> line = new HashMap<String, String>();
            for(ExtensibleAttribute attr: attributes){
                line.put(attr.getName(), attr.getValue());
            }
            try {
                ScriptIntegration se = ScriptFactory.createModule(scriptEngine);
                PopulationScript script = (PopulationScript)se.instantiateClass(null, config.getScript());
                ProvisionUser pUser = new ProvisionUser();
                int retval = script.execute(line, pUser);
                if(retval == 0){
                    log.debug("Population successful for user: " + login.getId());
                    if(login != null) {
                        List<Login> pList = new ArrayList<Login>();
                        pList.add(login);
                        login.getId().setManagedSysId("0");
                        pUser.setPrincipalList(pList);
                    }
                    provisionService.addUser(pUser);
                    //provisionService.modifyUser(pUser);
                }else{
                    log.debug("Couldn't populate ProvisionUser. User not added");
                    return false;
                }
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
