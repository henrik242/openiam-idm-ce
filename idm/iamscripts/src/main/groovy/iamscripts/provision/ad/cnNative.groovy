import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginId=user.firstName + "." + user.lastName

//loginId = "CN=" + loginId + ",ou=users,ou=openIam,dc=ocgov,dc=dev";


output=loginId


