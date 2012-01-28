
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import org.openiam.idm.srvc.synch.service.WSOperationCommand;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class MyAdapter implements WSOperationCommand {

	protected ProvisionUser pUser = new ProvisionUser();
	public static ApplicationContext ac;	

	public List<LineObject> execute(SynchConfig config) {
	List<LineObject> rowObjectList = new ArrayList();
	
	 	println("CMY CSV");

        println("CUSTOM SYNCHRONIZATION COMPLETE^^^^^^^^");
		
		SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);
		return resp;
		
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ac = applicationContext;
	}
	
	
	public static ApplicationContext getAc() {
		return ac;
	}

	public static void setAc(ApplicationContext ac) {
		MyAdapter.ac = ac;
	}
   

}
