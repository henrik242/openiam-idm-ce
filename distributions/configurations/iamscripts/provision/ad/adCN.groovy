import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginId=lg.getId().getLogin();


loginId = matchParam.keyField + "=" + loginId  + "," + matchParam.baseDn;

output=loginId


