package msgprovider

import org.openiam.idm.srvc.msg.service.NotificationMessageProvider
import org.openiam.idm.srvc.msg.service.Message
import org.openiam.idm.srvc.msg.service.MailTemplateParameters
import groovy.sql.Sql


public class BulkNotificationByUserIds implements NotificationMessageProvider {
    private static ResourceBundle DATASOURCE_PROPERTIES = ResourceBundle.getBundle("datasource");

    public Sql connect() {
        ResourceBundle resDS = ResourceBundle.getBundle("datasource");

        def db = resDS.getString("openiam.driver_url")
        def user = resDS.getString("openiam.username")
        def password = resDS.getString("openiam.password")
        def driver = resDS.getString("openiam.driver_classname")

        return Sql.newInstance(db, user, password, driver);
    }

    @Override
    public List<Message> build(Map<String, String> args) {
        if(args == null) {
            return Collections.EMPTY_LIST;
        }
        String subject = "System Notification";
        String userIds = args.get(MailTemplateParameters.USER_IDS.value());
        if(userIds == null || "".equals(userIds)) {
            return Collections.EMPTY_LIST;
        }

        def String query = "select FIRST_NAME, LAST_NAME, EMAIL_ADDRESS from USERS where USER_ID in ("+userIds+")";
        List<Message> messages = new LinkedList<Message>();
        def sql = connect();
        def paramList = [];
        sql.eachRow(query,paramList) { a ->
            Message message = new Message();
            message.addTo(a.EMAIL_ADDRESS);
            message.setSubject(subject);
            message.setBody("Dear "+a.FIRST_NAME +" "+a.LAST_NAME + ": " +" Your user account was modified by administrator.");
            message.setFrom(DATASOURCE_PROPERTIES.getString("mail.defaultSender"))
            message.setBodyType(Message.BodyType.PLAIN_TEXT);
            messages.add(message);
        };
        return messages;
    }
}
