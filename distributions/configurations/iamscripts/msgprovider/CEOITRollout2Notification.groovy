package msgprovider

import org.openiam.idm.srvc.msg.service.NotificationMessageProvider
import groovy.sql.Sql
import org.openiam.idm.srvc.msg.service.Message
import org.openiam.idm.srvc.msg.service.MailTemplateParameters


class CEOITRollout2Notification implements NotificationMessageProvider {

    private String DEFAULT_SUBJECT ="System Notification";
    private String DEFAULT_BODY = "<p class=\"MsoNormal\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:auto\"><span style=\"font-size:14.0pt;line-height:115%\" lang=\"EN-US\">Attn: <span style=\"color:red\">Sheriff Department Staff</span>,</span></p>\n" +
            "\n" +
            "<p class=\"MsoListParagraph\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:\n" +
            "auto;text-indent:-18.0pt;mso-list:l0 level1 lfo1\"><span style=\"font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:\n" +
            "Symbol;mso-bidi-font-family:Symbol\" lang=\"EN-US\"><span style=\"mso-list:Ignore\">·<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
            "</span></span></span><span style=\"font-size:14.0pt\" lang=\"EN-US\">The\n" +
            "County recently updated its <b><i>Information Technology (IT) Usage Policy</i></b><i>\n" +
            "</i>to reflect the changes in technology and enhance IT security measures. The\n" +
            "updated policy, which has been reviewed with executive management and<span style=\"color:navy\"> </span>labor representation, must by acknowledged by all\n" +
            "County employees and contractors who use Sheriff Department Systems. </span></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;\n" +
            "text-indent:36.0pt\"><b><span style=\"font-size:14.0pt;line-height:\n" +
            "115%;color:red\" lang=\"EN-US\">Human Resource directed that All IT users should complete their\n" +
            "acknowledgment process</span></b><b><span style=\"font-size:14.0pt;\n" +
            "line-height:115%;color:navy\" lang=\"EN-US\"> </span></b><b><span style=\"font-size:\n" +
            "14.0pt;line-height:115%;color:red\" lang=\"EN-US\">by the 01/30/10 deadline. </span></b></p>\n" +
            "\n" +
            "<p class=\"MsoListParagraph\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:\n" +
            "auto;text-indent:-18.0pt;mso-list:l0 level1 lfo1\"><span style=\"font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:\n" +
            "Symbol;mso-bidi-font-family:Symbol\" lang=\"EN-US\"><span style=\"mso-list:Ignore\">·<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
            "</span></span></span><span style=\"font-size:14.0pt\" lang=\"EN-US\">This\n" +
            "week you will get a <span style=\"color:red\">“welcome email” </span><span style=\"color:navy\">from</span><b><span style=\"color:red\"> <a href=\"mailto:OCid-support@ocgov.com\">OCid-support@ocgov.com</a> </span></b>containing\n" +
            "<b>step-by-step instructions</b> on how to acknowledge the Updated IT Usage\n" +
            "Policy online. &nbsp;<u>Do<span style=\"color:navy\"> </span>n<span style=\"color:navy\">o</span>t delete the email</u><span style=\"color:navy\">,</span>\n" +
            "rather<span style=\"color:navy\"> </span>follow the steps outlined in the email. <span style=\"color:navy\">T</span>he online process should take less than 5 minutes<span style=\"color:navy\"> t</span>o<span style=\"color:navy\"> </span>complete<span style=\"color:navy\">, </span>and support is available if you require\n" +
            "assistance.&nbsp; </span></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:auto\"><span style=\"font-size:14.0pt;line-height:115%\" lang=\"EN-US\">Please contact your\n" +
            "Agency-HR Representative with any policy questions. Additional information\n" +
            "about OCid is provided below.<span style=\"color:navy\"> </span></span></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\" style=\"mso-margin-top-alt:auto;mso-margin-bottom-alt:auto\"><b><span style=\"font-size:14.0pt;line-height:115%;color:#003366\" lang=\"EN-US\"><img src=\"\" alt=\"Description: cid:image001.jpg@01CA9CF1.94A9FC60\" border=\"0\" height=\"51\" width=\"132\">&nbsp;is\n" +
            "the County’s secure identity application for: policy compliance, staff\n" +
            "directory services and enterprise IT application access information.</span></b></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\"><b><u><span style=\"font-size:14.0pt;line-height:\n" +
            "115%;color:#003366\" lang=\"EN-US\">OCid provides:</span></u></b><span style=\"font-size:14.0pt;line-height:115%;color:#003366\" lang=\"EN-US\"></span></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\" style=\"margin-bottom:12.0pt\"><b><span style=\"font-size:14.0pt;line-height:115%;color:#003366\" lang=\"EN-US\">· Online acknowledgment\n" +
            "of IT Policy and other compliance documents.<br>\n" +
            "·&nbsp;Updating of your own phone numbers, building location, supervisor, etc.<br>\n" +
            "·&nbsp;Self-service Password -reset your master password whenever you want.<br>\n" +
            "·&nbsp;Self-service Password Recovery using challenge response questions.<br>\n" +
            "·&nbsp;Self-service secure updating of your emergency contact information.<br>\n" +
            "·&nbsp;Anonymous reporting of security breaches.</span></b></p>\n" +
            "\n" +
            "<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>";

    public Sql connect() {
        ResourceBundle resDS = ResourceBundle.getBundle("datasource");

        def db = resDS.getString("openiam.driver_url");
        def user = resDS.getString("openiam.username");
        def password = resDS.getString("openiam.password");
        def driver = resDS.getString("openiam.driver_classname");

        return Sql.newInstance(db, user, password, driver);
    }

    @Override
    public List<Message> build(Map<String, String> args) {
        if(args == null) {
            return Collections.EMPTY_LIST;
        }
        String userIds = args.get(MailTemplateParameters.USER_IDS.value());
        if(userIds == null || "".equals(userIds)) {
            return Collections.EMPTY_LIST;
        }
        ResourceBundle resDS = ResourceBundle.getBundle("datasource");
        def from = resDS.getString("mail.defaultSender");
        def String query = "select FIRST_NAME, LAST_NAME, EMAIL_ADDRESS from USERS where USER_ID in ("+userIds+")";

        def templateName = "CEOIT Rollout-2";
        def String getTemplateQuery = "select TMPL_SUBJECT, BODY from MAIL_TEMPLATE where TMPL_NAME='"+templateName+"'";

        def String subject = DEFAULT_SUBJECT;
        def String body = DEFAULT_BODY;
        def sql = connect();
        sql.eachRow(getTemplateQuery,[]) { a ->
            subject = a.TMPL_SUBJECT;
            body = a.BODY;
        };

        List<Message> messages = new LinkedList<Message>();

        def paramList = [];

        sql.eachRow(query,paramList) { a ->
            Message message = new Message();
            message.addTo(a.EMAIL_ADDRESS);
            message.setSubject(subject);
            message.setBody(body);
            message.setFrom(from);
            message.setBodyType(Message.BodyType.HTML_TEXT);
            messages.add(message);
        };
        return messages;
    }
}
