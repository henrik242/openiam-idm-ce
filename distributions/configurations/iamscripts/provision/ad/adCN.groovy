import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginId=user.firstName + "." + user.lastName

loginId = matchParam.keyField + "=" + loginId + ",CN=Users,DC=iamdev,DC=local";

output=loginId


