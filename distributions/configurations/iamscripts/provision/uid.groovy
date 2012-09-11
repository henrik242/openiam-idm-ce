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


import org.springframework.context.support.ClassPathXmlApplicationContext
import java.util.*;

def loginManager = context.getBean("loginManager")

println("uid.groovy called.")


	loginId =  lg.id.login;
	
	origLoginID = loginId
	

	loginId = matchParam.keyField + "=" + loginId + ",ou=people," + matchParam.baseDn;


	ctr = 1;
	


	while ( loginManager.loginExists( "USR_SEC_DOMAIN", loginId, "101" )) {
		  strCtrSize = String.valueOf(ctr)
			loginId=  matchParam.keyField + "=" +  origLoginID + ctr + "," + matchParam.baseDn;
			ctr++
	}

		
	output = loginId
	