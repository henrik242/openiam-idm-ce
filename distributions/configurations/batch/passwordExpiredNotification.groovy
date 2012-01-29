// Sends a notification to users whose passwords HAVE ALREADY EXPIRED
// runs every night

System.out.println("passwordExpiredNotification.groovy");

import org.openiam.idm.srvc.msg.dto.NotificationRequest
import org.openiam.idm.srvc.msg.dto.NotificationParam

import java.util.Date
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.msg.dto.NotificationRequest
import org.openiam.idm.groovy.helper.ServiceHelper;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.base.id.UUIDGen;
import groovy.sql.*
import java.util.ResourceBundle
import org.openiam.idm.srvc.user.dto.User;



def MailService mailSrv = ServiceHelper.emailService();
ResourceBundle res = ResourceBundle.getBundle("datasource");



def db=res.getString("openiam.driver_url")  				//'jdbc:mysql://localhost:3306/gilt'
def u=res.getString("openiam.username")   					//'demouser'
def password=res.getString("openiam.password")			//'demouser'
def driver= res.getString("openiam.driver_classname")  //'com.mysql.jdbc.Driver'

String str = "select DISTINCT LOGIN, USER_ID, EXPIRATION_DATE from USER_PSWD_EXPIRED_YESTERDAY_VW " ;


def sql = Sql.newInstance(db,u, password, driver)
sql.eachRow(str) { result ->

    String userId = result.USER_ID
    String login = result.LOGIN

    println("owner id = >" + userId)


    NotificationRequest req = new NotificationRequest()

    req.notificationType = "PASSWORD_EXPIRED"
    req.userId = userId
    req.linkedRequestId = parentRequestId
    req.requestId =  UUIDGen.getUUID()


    mailSrv.sendNotification(req)

}

output=1

