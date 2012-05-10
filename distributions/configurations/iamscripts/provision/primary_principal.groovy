import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

println("primary_principal.groovy called.");

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


