// changes the status us users that have been inactive
// runs every night

import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.idm.srvc.msg.dto.NotificationRequest
import java.util.Date;
import java.util.Calendar;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.msg.dto.NotificationRequest
import org.openiam.idm.groovy.helper.ServiceHelper;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.provision.service.ProvisionService;
import org.openiam.provision.dto.ProvisionUser
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;




def MailService mailService = ServiceHelper.emailService();
def LoginDataWebService loginManager = ServiceHelper.loginService()
def UserDataWebService userManager = ServiceHelper.userService()
def ProvisionService provision = ServiceHelper.povisionService()

println("Inactiveuser.groovy called.");


final int INACTIVE_DAYS = 90;

def boolean isIdmAccountActive(Login lg,LoginDataWebService loginManager, int days) {

	Login idmLg = loginManager.getLoginByManagedSys("IDM", lg.id.login, "0").principal;
	
	if ( idmLg == null) {
		return false;
	}
	
	if (idmLg.lastLogin == null) {
		return false;
	}
	
	// check the date
	long lastLoginMilis = idmLg.lastLogin.getTime();
	long curTimeMillis = System.currentTimeMillis();
	Date startDate = new Date(curTimeMillis);
	
	Calendar c = Calendar.getInstance();
  c.setTime(startDate);
  c.add(Calendar.DAY_OF_YEAR, (-1 * days));
  
  long startTimeMilis = c.getTimeInMillis();
  
  
  if (lastLoginMilis < startTimeMilis) {
  	
  	println("last login is before startTime");
  	
  	return false;
  
  }
  
  println("IDM found and is still active");
  
  return true; 
            
		
}

// get users that have been inactive for 90 day
// if you need all users over 90, then pass in 90,0

loginList = loginManager.getInactiveUsers(INACTIVE_DAYS,0).principalList


if (loginList != null ) {
	for ( lg in loginList) {
		
		println("Processing User id=" + lg.userId + " " + lg.id)
		
		if (	"USR_SEC_DOMAIN".equalsIgnoreCase(lg.id.domainId) && 
					!isIdmAccountActive( lg, loginManager, INACTIVE_DAYS ) ) {
						
		
			user = userManager.getUserWithDependent(lg.userId, true).user		
	
			
	
			if (user.status == null || (user.status != UserStatusEnum.INACTIVE &&
				 user.status != UserStatusEnum.TERMINATE && user.status != UserStatusEnum.LEAVE )) {
				
			 
				 ProvisionUser pUser = new ProvisionUser(user);
				 pUser.userId = user.userId
				 pUser.status = UserStatusEnum.INACTIVE
				 pUser.secondaryStatus = null
				 pUser.lastUpdatedBy = "3000"
	
				 pUser.requestorLogin ="IDM_SERVER";
	       pUser.requestorDomain = "IDM";
	      
				 provision.modifyUser(pUser)
				 
		    
				 NotificationRequest req = new NotificationRequest()
				 req.notificationType = "ACCOUNT_INACTIVE"
				 req.userId = user.userId
				 mailService.sendNotification(req)	    
			}
		}else {
			println("Skipping: " + lg.userId + " " + lg.id);
		}
	}
}

output=1