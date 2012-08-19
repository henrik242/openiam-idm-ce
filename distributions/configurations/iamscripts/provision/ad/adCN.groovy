import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginId=user.firstName + "." + user.lastName


loginId = matchParam.keyField + "=" + loginId  + "," + matchParam.baseDn;

output=loginId


