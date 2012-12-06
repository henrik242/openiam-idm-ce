package msgprovider

import org.openiam.idm.srvc.msg.service.NotificationMessageProvider
import org.openiam.idm.srvc.msg.service.Message
import org.openiam.idm.srvc.msg.service.MailTemplateParameters
import org.openiam.idm.srvc.msg.service.MailSenderUtils

public class NewUserCreateToSupervisorNotification implements NotificationMessageProvider {

    private static final String tmplBody = "Dear [firstName] [lastName]: \n\nA new user account has been created for [name]\n\nThe login Id is: [identity] \nThe password is: [password]";
    private static ResourceBundle DATASOURCE_PROPERTIES = ResourceBundle.getBundle("datasource");
    @Override
    public List<Message> build(Map<String, String> args) {
        String subject = "New Account Created";
        String toAddress = args.get(MailTemplateParameters.TO.value());
        String ccAddress = args.get(MailTemplateParameters.CC.value());
        String bccAddress = args.get(MailTemplateParameters.BCC.value());
        if(args==null
                || toAddress == null
                || "".equals(toAddress)) {
            return Collections.EMPTY_LIST;
        }
        List<Message> messageList = new LinkedList<Message>();
        Message message = new Message();

        message.addTo(toAddress);
        message.setFrom(DATASOURCE_PROPERTIES.getString("mail.defaultSender"));
        if(ccAddress != null && !"".equals(ccAddress)) {
            message.addCc(ccAddress);
        }
        if(bccAddress != null && !"".equals(bccAddress)) {
            message.addBcc(bccAddress)
        }
        message.setSubject(subject);
        message.setBodyType(Message.BodyType.PLAIN_TEXT);

        String body = MailSenderUtils.parseBody(tmplBody, args);
        message.setBody(body);

        messageList.add(message);
        return messageList;
    }
}