
/**
 * Manager.groovy
 * Returns the name of a person immediate super-visor
 * User: suneetshah
 * Date: 2/9/13
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openiam.idm.srvc.user.ws.SupervisorListResponse
import org.openiam.base.ws.ResponseStatus
import org.openiam.idm.srvc.user.dto.Supervisor
import org.openiam.idm.srvc.user.dto.User;


def userMgr = context.getBean("userWS")

output = null;

// user is passed into the script as a bind variable in the service that calls this script
User u = user;

SupervisorListResponse supervisorResp = userMgr.getSupervisors(u.userId);
if (supervisorResp.getStatus() == ResponseStatus.SUCCESS) {
    List<Supervisor> supVisorList = supervisorResp.getSupervisorList();
    if (supVisorList != null && !supVisorList.isEmpty()) {
        Supervisor supervisor = supVisorList.get(0);

        output = supervisor.getSupervisor().getFirstName() + " " + supervisor.getSupervisor().getLastName()

    }
}
