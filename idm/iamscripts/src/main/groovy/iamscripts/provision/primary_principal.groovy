import org.springframework.context.support.ClassPathXmlApplicationContext

/*
Objects that are passed to the script:
sysId - DefaultManagedSysId
user - New user object that has been submitted to the provisioning service
org - Organization object		
context - Spring application context. Allows you to look up any spring bean
targetSystemIdentityStatus
targetSystemIdentity
targetSystemAttributes = attributes at the target system
*/


def loginManager = context.getBean("loginManager")


if (user.userAttributes.get("userid") != null && user.userAttributes.get("userid").value != null && 
	user.userAttributes.get("userid").value.length() > 0 ) {
	loginID = user.userAttributes.get("userid").value
}else {
	
	loginID=user.firstName + "." + user.lastName

}

ctr = 1;
origLoginID = loginID


while ( loginManager.loginExists( "USR_SEC_DOMAIN", loginID, sysId )) {
  strCtrSize = String.valueOf(ctr)
	loginID=origLoginID + ctr
	ctr++
}


output=loginID


