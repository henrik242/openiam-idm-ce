import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

// DEFAULT SECURITY DOMAIN
def secDomain = "USR_SEC_DOMAIN";

ctr = 1;

loginID=user.firstName + "." + user.lastName

if (securityDomain != null) {
	secDomain = securityDomain;	
}


if (loginID.length() > 17) {
	loginID = loginID.substring(0,17);
	
	// add logic to ensure uniqueness
	
	if (managedSysId != null) {
		
		origLoginID = loginId;
		
		while ( loginManager.loginExists( secDomain, loginID, managedSysId )) {
		  strCtrSize = String.valueOf(ctr)
			loginId=   origLoginID + ctr;
			ctr++
		}

	}


}


output=loginID


