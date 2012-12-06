package msgprovider
import org.openiam.idm.srvc.msg.service.NotificationMessageProvider
import org.openiam.idm.srvc.msg.service.Message
import org.openiam.idm.srvc.msg.service.MailTemplateParameters
import org.openiam.idm.srvc.msg.service.MailSenderUtils

class TestMailSenderNotification implements NotificationMessageProvider {
        private static ResourceBundle res = ResourceBundle.getBundle("securityconf");
        private static ResourceBundle DATASOURCE_PROPERTIES = ResourceBundle.getBundle("datasource");

        private static final String tmplBody = "<html>Dear [firstName] [lastName]: \n\n" +
                "<b>This is to notify you that the request summarized below has been approved.</b> \n\n" +
                "\n\n" +
                "Request ID: [requestId] \n" +
                "Request Type: <div style='color:red;'>[requestReason] </div> \n" +
                "Approver: [requester] \n" +
                "For: [targetUser] \n\n" +
                "A new user account has been created for [targetUser]\n" +
                "The login Id is: [identity]\n" +
                "The initial password is: [password]</html>";

        @Override
        public List<Message> build(Map<String, String> args) {
            if(args == null) {
                return Collections.EMPTY_LIST;
            }
            String subject = "Request APPROVED";
            String userId = args.get(MailTemplateParameters.USER_ID.value());

            String toAddress = args.get(MailTemplateParameters.TO.value());

            String ccAddress = args.get(MailTemplateParameters.CC.value());
            String bccAddress = args.get(MailTemplateParameters.BCC.value());

            if(toAddress == null
                    || "".equals(toAddress)) {
                return Collections.EMPTY_LIST;
            }
            List<Message> messageList = new LinkedList<Message>();
            Message message = new Message();

            message.addAttachments(res.getString("uploadDir") + File.separatorChar + "OpenIAM_and_BIRT_integration.docx");

            message.addTo(toAddress);
            message.setFrom(DATASOURCE_PROPERTIES.getString("mail.defaultSender"));
            if(ccAddress != null && !"".equals(ccAddress)) {
                message.addCc(ccAddress);
            }
            if(bccAddress != null && !"".equals(bccAddress)) {
                message.addBcc(bccAddress)
            }
            message.setSubject(subject);
            message.setBodyType(Message.BodyType.HTML_TEXT);

            String body = MailSenderUtils.parseBody(tmplBody, args);
            message.setBody(body);

            messageList.add(message);
            return messageList;
        }

}
