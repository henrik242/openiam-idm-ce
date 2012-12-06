package org.openiam.idm.srvc.msg.service;

import org.mockito.Matchers;
import org.openiam.base.ws.PropertyMap;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.dto.MessageBodyType;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.dto.NotificationType;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class MailServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationService sysMessageService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private MailTemplateService mailTemplateService;

    private final static String tmplBody = "Dear [firstName] [lastName]: \n\n" +
            "This is to notify you that the request summarized below has been approved. \n\n" +
            "\n\n" +
            "Request ID: [requestId] \n" +
            "Request Type: [requestReason] \n" +
            "Approver: [requester] \n" +
            "For: [targetUser] \n\n" +
            "A new user account has been created for [targetUser]\n" +
            "The login Id is: [identity]\n" +
            "The initial password is: [password]";

    private final static String tmplHtmlBody = "<html><b>Dear</b> [firstName] [lastName]: \n\n" +
            "<div style='color:red;'>This is to notify you that the request summarized below has been approved.</div> \n\n" +
            "\n\n" +
            "Request ID: [requestId] \n" +
            "Request Type: [requestReason] \n" +
            "Approver: [requester] \n" +
            "For: [targetUser] \n\n" +
            "A new user account has been created for [targetUser]\n" +
            "The login Id is: [identity]\n" +
            "The initial password is: [password] </html>";

    private UserDataWebService userWS = mock(UserDataWebService.class);
    private User user;
    private MailTemplateDto mailTemplateDto;

    @BeforeMethod
    public void init() {
        user = new User();
        user.setEmail("vitaly.test@openiam.com");
        user.setFirstName("Vitaly");
        user.setLastName("Test");
        user = userDataService.addUser(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user);
        stub(userWS.getUserWithDependent( Matchers.<String>any(), eq(true))).toReturn(userResponse);
        ((MailServiceImpl) mailService).setUserManager(userWS);

        NotificationDto sysMessageDto = new NotificationDto();
/*
        sysMessageDto.setName("NEW_USER_EMAIL");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("NewUserCreateNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
        sysMessageDto = new SysMessageDto();
        sysMessageDto.setName("NEW_USER_EMAIL_SUPERVISOR");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("NewUserCreateToSupervisorNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
        sysMessageDto = new SysMessageDto();
        sysMessageDto.setName("REQUEST_APPROVED");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("RequestApproveNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
        sysMessageDto = new SysMessageDto();
        sysMessageDto.setName("REQUEST_REJECTED");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("RequestRejectNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
        sysMessageDto = new SysMessageDto();
        sysMessageDto.setName("NEW_PENDING_REQUEST");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("RequestNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
        sysMessageDto = new SysMessageDto();
        sysMessageDto.setName("REQUEST_PASSWORD_RESET");
        sysMessageDto.setType(NotificationType.SYSTEM);
        sysMessageDto.setProviderScriptName("RequestPasswordResetNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);
*/

        sysMessageDto = new NotificationDto();
        sysMessageDto.setName("TEST_SCRIPT_MSG");
        sysMessageDto.setType(NotificationType.CONFIGURABLE);
        sysMessageDto.setProviderScriptName("TestMailSenderNotification.groovy");
        sysMessageService.addNotification(sysMessageDto);

        sysMessageDto = new NotificationDto();
        sysMessageDto.setName("TEST_MANY_USERS_MAILS_SCRIPT_MSG");
        sysMessageDto.setType(NotificationType.CONFIGURABLE);
        sysMessageDto.setProviderScriptName("SendForManyUsersByIds.groovy");
        sysMessageService.addNotification(sysMessageDto);

        sysMessageDto = new NotificationDto();
        sysMessageDto.setName("TEST_TEMPLATE_MSG");
        sysMessageDto.setType(NotificationType.CONFIGURABLE);
        mailTemplateDto = new MailTemplateDto();
        mailTemplateDto.setAttachmentFilePath("OpenIAM_and_BIRT_integration.docx");
        mailTemplateDto.setName("TEST_TEMPLATE");
        mailTemplateDto.setType(MessageBodyType.PLAIN);
        mailTemplateDto.setSubject("Test Mail Template");
        mailTemplateDto.setBody(tmplBody);
        mailTemplateDto = mailTemplateService.addTemplate(mailTemplateDto);
        sysMessageDto.setMailTemplate(mailTemplateDto);
        sysMessageService.addNotification(sysMessageDto);

        sysMessageDto = new NotificationDto();
        sysMessageDto.setName("TEST_HTML_TEMPLATE_MSG");
        sysMessageDto.setType(NotificationType.CONFIGURABLE);
        mailTemplateDto = new MailTemplateDto();
        mailTemplateDto.setAttachmentFilePath("OpenIAM_and_BIRT_integration.docx");
        mailTemplateDto.setName("HTML_TEMPLATE");
        mailTemplateDto.setType(MessageBodyType.HTML);
        mailTemplateDto.setSubject("Test Mail with Html Template");
        mailTemplateDto.setBody(tmplHtmlBody);
        mailTemplateDto = mailTemplateService.addTemplate(mailTemplateDto);

        sysMessageDto.setMailTemplate(mailTemplateDto);
        sysMessageService.addNotification(sysMessageDto);
        // NEW_USER_EMAIL [NewUserCreateNotification.groovy]
    // NEW_USER_EMAIL_SUPERVISOR [NewUserCreateToSupervisorNotification.groovy]
    // REQUEST_APPROVED [RequestApproveNotification.groovy]
    // REQUEST_REJECTED [RequestRejectNotification.groovy],
    // NEW_PENDING_REQUEST [RequestNotification.groovy],
    // REQUEST_PASSWORD_RESET [RequestPasswordResetNotification]
    }

    //@Test
    public void testScriptNotification() {

        String TEST_SCRIPT_MSG = "TEST_SCRIPT_MSG";

        HashMap<String, String> mailParameters = new HashMap<String, String>();
        mailParameters.put(MailTemplateParameters.USER_ID.value(), user.getUserId());
        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), "1");
        mailParameters.put(MailTemplateParameters.REQUESTER.value(), "Requester");
        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), "REQUEST_APPROVED");
        mailParameters.put(MailTemplateParameters.IDENTITY.value(), "identity");
        mailParameters.put(MailTemplateParameters.PASSWORD.value(), "password");
        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), "Target User");

        mailService.sendNotification(TEST_SCRIPT_MSG, new PropertyMap(mailParameters));
    }

    //@Test
    public void testScriptWithManyNotifications() {

        String TEST_SCRIPT_MSG = "TEST_MANY_USERS_MAILS_SCRIPT_MSG";

        HashMap<String, String> mailParameters = new HashMap<String, String>();
        mailParameters.put(MailTemplateParameters.USER_ID.value(), user.getUserId());
        mailParameters.put(MailTemplateParameters.USER_IDS.value(), "'297e52f33b37a5ef013b380b17c00017','297e52f33b37a5ef013b382d225a0026'");
        mailService.sendNotification(TEST_SCRIPT_MSG, new PropertyMap(mailParameters));
    }

    //@Test
    public void testTemplateNotification() {
        String TEST_TEMPLATE_MSG = "TEST_TEMPLATE_MSG";

        HashMap<String, String> mailParameters = new HashMap<String, String>();

        mailParameters.put(MailTemplateParameters.USER_ID.value(), user.getUserId());
        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), "1");
        mailParameters.put(MailTemplateParameters.REQUESTER.value(), "Requester");
        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), "REQUEST_APPROVED");
        mailParameters.put(MailTemplateParameters.IDENTITY.value(), "identity");
        mailParameters.put(MailTemplateParameters.PASSWORD.value(), "password");
        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), "Target User");

        mailService.sendNotification(TEST_TEMPLATE_MSG, new PropertyMap(mailParameters));
    }

    //@Test
    public void testHtmlTemplateNotification() {
        String TEST_HTML_TEMPLATE_MSG = "TEST_HTML_TEMPLATE_MSG";

        HashMap<String, String> mailParameters = new HashMap<String, String>();
        mailParameters.put(MailTemplateParameters.USER_ID.value(), user.getUserId());
        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), "1");
        mailParameters.put(MailTemplateParameters.REQUESTER.value(), "Requester");
        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), "REQUEST_APPROVED");
        mailParameters.put(MailTemplateParameters.IDENTITY.value(), "identity");
        mailParameters.put(MailTemplateParameters.PASSWORD.value(), "password");
        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), "Target User");

        mailService.sendNotification(TEST_HTML_TEMPLATE_MSG, new PropertyMap(mailParameters));
    }

}
