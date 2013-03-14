
/**
 * Manager.groovy
 * Returns the name of a person immediate supervisor. Return the DN so that it works with AD
 */

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openiam.idm.srvc.user.ws.SupervisorListResponse
import org.openiam.base.ws.ResponseStatus
import org.openiam.idm.srvc.user.dto.Supervisor
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.auth.dto.Login;

def loginManager = context.getBean("loginManager")
def userMgr = context.getBean("userWS")

output = null;

// user is passed into the script as a bind variable in the service that calls this script
User u = user;

SupervisorListResponse supervisorResp = userMgr.getSupervisors(u.userId);
if (supervisorResp.getStatus() == ResponseStatus.SUCCESS) {
    List<Supervisor> supVisorList = supervisorResp.getSupervisorList();
    if (supVisorList != null && !supVisorList.isEmpty()) {
        Supervisor supervisor = supVisorList.get(0);

        Login l = loginManager.getByUserIdManagedSys(supervisor.getSupervisor().userId, TARGET_SYS_MANAGED_SYS_ID);

        // identity for the AD resource
        output = l.id.login;

    }
}


