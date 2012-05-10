
if (user.email != null && user.email.length() > 0) {
	output=user.email
}else {

	def loginManager = context.getBean("loginManager")

	loginID=user.firstName + "." + user.lastName


	ctr = 1;
	origLoginID = loginID


	while ( loginManager.loginExists( "USR_SEC_DOMAIN", loginID, sysId )) {
		loginID=origLoginID + ctr
		ctr++
	}

	email = loginID + "@openiam.com" 


	output=email

}


