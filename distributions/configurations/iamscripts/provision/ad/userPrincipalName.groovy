import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginID=user.firstName + "." + user.lastName + "@ocgov.com";



output=loginID


