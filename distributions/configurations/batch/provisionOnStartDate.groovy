// changes the status us users that have been inactive
// runs every night


System.out.println("inactiveUser.groovy is being executed.");


import org.openiam.idm.groovy.helper.ServiceHelper;


import org.openiam.idm.srvc.auth.ws.LoginDataWebService
import org.openiam.idm.srvc.msg.dto.NotificationRequest
import org.openiam.idm.srvc.msg.service.MailService
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.idm.srvc.user.ws.UserDataWebService
import org.openiam.provision.dto.ProvisionUser
import org.openiam.provision.service.ProvisionService
import org.openiam.idm.srvc.user.dto.UserSearch
import org.openiam.base.id.UUIDGen;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.msg.dto.NotificationParam;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;


def MailService mailService = ServiceHelper.emailService();
def UserDataWebService userManager = ServiceHelper.userService()
def ProvisionService provision = ServiceHelper.povisionService()
def LoginDataWebService loginManager = ServiceHelper.loginService()


println("ProvisionOnStartDate.groovy called.");

Calendar cal = Calendar.getInstance();
cal.setTime(new Date(System.currentTimeMillis()));

UserSearch search = new UserSearch();
search.startDate = cal.getTime();
search.status = UserStatusEnum.PENDING_START_DATE;

userList = userManager.search(search).userList;


if (userList != null ) {
	for ( user in userList) {
        
			 ProvisionUser pUser = new ProvisionUser(user);
			 pUser.userId = user.userId
			 pUser.status = UserStatusEnum.ACTIVE
			 pUser.secondaryStatus = null;
       pUser.setProvisionOnStartDate(false);

       pUser.lastUpdatedBy = "3000"
			 pUser.requestorLogin ="IDM_SERVER";
       pUser.requestorDomain = "IDM";

			 provision.modifyUser(pUser)
			 
			 // get the primary identity and its password to notify the user
			 Login userIdentity = loginManager.getPrimaryIdentity(user.userId).principal;
			 String decPassword = (String)loginManager.decryptPassword(userIdentity.getPassword()).responseValue;
			 
	    
			 NotificationRequest req = new NotificationRequest()
			 req.notificationType = "NEW_USER_EMAIL"
			 req.userId = user.userId
			 req.linkedRequestId = parentRequestId
    	 req.requestId =  UUIDGen.getUUID()
    	 
    	 req.getParamList().add(new NotificationParam("IDENTITY", userIdentity.id.login));
       req.getParamList().add(new NotificationParam("PSWD", decPassword));
            
    
			 mailService.sendNotification(req)	    
			 

		}		
	}

output=1