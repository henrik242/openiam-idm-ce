import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginId=user.firstName + "." + user.lastName

loginId = matchParam.keyField + "=" + loginId + ",CN=Users,DC=ocgov,DC=dev";

output=loginId


