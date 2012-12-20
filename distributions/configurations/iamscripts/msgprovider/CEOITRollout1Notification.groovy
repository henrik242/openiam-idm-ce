package msgprovider;

import groovy.sql.Sql;
import org.openiam.idm.srvc.msg.service.MailTemplateParameters;
import org.openiam.idm.srvc.msg.service.Message;
import org.openiam.idm.srvc.msg.service.NotificationMessageProvider;


public class CEOITRollout1Notification implements NotificationMessageProvider {

    private String DEFAULT_SUBJECT ="System Notification";
    private String DEFAULT_BODY = "As introduced by CEO/IT Management, here are your step-by-step instructions.\n" +
            "Please complete the following steps on or before: 3/18/2012\n" +
            "\n" +
            "1) Open a web browser, go to https://OCid.ocgov.com and login entering your assigned User Private Name (UPN) and Pass Phrase. Type the UPN exactly as shown using upper and lower case letters, the period and 3-digits. If you have lost or forgot your login credentials you can use the \"GET-UPN\" or \"Pass Phrase Reset\" self-service links on the OCid login page.\n" +
            "\n" +
            "2) When the \"Welcome\" screen appears, Click on the \"Edit Your Profile\" link (lower right side (5 items below Self-Service Center heading). Fill-in/update all fields (including non-required items) before clicking the \"Submit\" button. Do not change your legal name, rather edit the AKA field with any nickname you use. To assist you, simply click on the \"Select Supervisor\" and \"Select Work Site\" links and the system will fill-in the information for you.\n" +
            "\n" +
            "CEO/IT Users should complete ALL PROFILE FIELDS, not just those marked *required.  This includes the key areas listed below:\n" +
            "   1)Work information: Work site, floor/room, address, City, Zip\n" +
            "   2)Contact information: Listed selection,  Desk Phone, Alt Work Phone, non-County email\n" +
            "   3)Emergency information: Home Phone, Address, Emergency Name, relationship, full Home address\n" +
            "Note: ACS management has approved their staff to complete the Emergency Contact block (lower right above the \"Privacy Note\").\n" +
            "\n" +
            "3)  Supervisors - You should talk with any member of your staff who is not listed among your \"Direct Reports\" at the bottom of your Profile Screen, as that staff member has not selected you as their supervisor.\n" +
            "\n" +
            "4) Click on the \"Challenge Response\" link (on the right side under Self-Service). Select and answer three questions then click the Submit button. In the future if you used the Forgot Password link you would be prompted to answer these same questions.\n" +
            "\n" +
            "5) Click on the \"Logout\" link and you are done.\n" +
            "\n" +
            "If you have any questions you can click on the built-in FAQ link, or visit http://intra2k3.ocgov.com/ocid. If you need personal assistance simply click on the self-service \"Support\" link (lower right of the web page) or contact the CEO/IT Helpdesk.";

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
        def String mailTeplateName = "CEOIT Rollout-1";

        def String getTemplateQuery = "select TMPL_SUBJECT, BODY from MAIL_TEMPLATE where TMPL_NAME='"+mailTeplateName+"'";
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
            message.setBodyType(Message.BodyType.PLAIN_TEXT);
            messages.add(message);
        };
        return messages;
    }
}
