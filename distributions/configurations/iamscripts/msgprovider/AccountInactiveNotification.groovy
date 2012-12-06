package msgprovider

import org.openiam.idm.srvc.msg.service.NotificationMessageProvider
import org.openiam.idm.srvc.msg.service.Message
import org.openiam.idm.srvc.msg.service.MailTemplateParameters
import org.openiam.idm.srvc.msg.service.MailSenderUtils;

public class AccountInactiveNotification implements NotificationMessageProvider {

    private static final String tmplBody = "Dear [firstName] [lastName]: \n\n Your account status has been changed due to inactivity. \n\n Please contact the security office to have your account reactivated.";
    private static ResourceBundle DATASOURCE_PROPERTIES = ResourceBundle.getBundle("datasource");

    @Override
    public List<Message> build(final Map<String, String> args) {
        String subject = "Account status changed to inactive";
        String toAddress = args.get(MailTemplateParameters.TO.value());
        String ccAddress = args.get(MailTemplateParameters.CC.value());
        String bccAddress = args.get(MailTemplateParameters.BCC.value());

        if(args==null
                || toAddress == null
                || "".equals(toAddress)) {
           return Collections.EMPTY_LIST;
        }
        List<Message> notificationMessageList = new LinkedList<Message>();
        Message notificationMessage = new Message();
        notificationMessage.addTo(toAddress);
        notificationMessage.setFrom(DATASOURCE_PROPERTIES.getString("mail.defaultSender"));
        if(ccAddress != null && !"".equals(ccAddress)) {
            notificationMessage.addCc(ccAddress);
        }
        if(bccAddress != null && !"".equals(bccAddress)) {
            notificationMessage.addBcc(bccAddress)
        }
        notificationMessage.setSubject(subject);
        notificationMessage.setBodyType(Message.BodyType.PLAIN_TEXT);

        String body = MailSenderUtils.parseBody(tmplBody, args);
        notificationMessage.setBody(body);

        notificationMessageList.add(notificationMessage);
        return notificationMessageList;
    }

}