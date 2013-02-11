
/**
 * Manager.groovy
 * Returns the name of a person immediate supervisor. Return the DN so that it works with AD
 * User: suneetshah
 * Date: 2/9/13
 * Time: 12:37 PM
 */

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openiam.idm.srvc.user.ws.SupervisorListResponse
import org.openiam.base.ws.ResponseStatus
import org.openiam.idm.srvc.user.dto.Supervisor
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.auth.dto.Login
import org.openiam.provision.service.AbstractProvisioningService;
import org.openiam.idm.srvc.auth.login.LoginDataService;

def LoginDataService loginManager = context.getBean("loginManager")
def userMgr = context.getBean("userWS")

output = null;

// user is passed into the script as a bind variable in the service that calls this script
User u = user;

println( "TARGET_SYS_MANAGED_SYS_ID = " + managedSysId);

SupervisorListResponse supervisorResp = userMgr.getSupervisors(u.userId);
if (supervisorResp.getStatus() == ResponseStatus.SUCCESS) {
    List<Supervisor> supVisorList = supervisorResp.getSupervisorList();
    if (supVisorList != null && !supVisorList.isEmpty()) {
        Supervisor supervisor = supVisorList.get(0);

        println();

        List<Login> identityList = loginManager.getLoginByUser(supervisor.getSupervisor().userId);

        if ( identityList != null) {
            for (Login l : identityList) {

                if ( l.id.managedSysId.equalsIgnoreCase(managedSysId)) {
                    output = l.id.login;
                    return;
                }

            }

        }
    }
}

