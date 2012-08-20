import org.springframework.context.support.ClassPathXmlApplicationContext

def loginManager = context.getBean("loginManager")

loginID=user.firstName + "." + user.lastName + "@ad.openiamdemo.info";



output=loginID


