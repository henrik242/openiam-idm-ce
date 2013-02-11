package org.openiam.idm.srvc.msg.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.msg.dto.MessageBodyType;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.ws.NotificationParam;
import org.openiam.idm.srvc.msg.ws.NotificationRequest;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.File;
import java.util.*;

@WebService(endpointInterface = "org.openiam.idm.srvc.msg.service.MailService",
        targetNamespace = "urn:idm.openiam.org/srvc/msg",
        portName = "EmailWebServicePort",
        serviceName = "EmailWebService")
public class MailServiceImpl implements MailService {

    private MailSender mailSender;
    private String defaultSender;
    private String subjectPrefix;
    private String optionalBccAddress;

    private String scriptEngine;
    private UserDataWebService userManager;
    private AuditHelper auditHelper;

    public static ApplicationContext ac;

    private NotificationService sysMessageService;

    private static final Log log = LogFactory.getLog(MailServiceImpl.class);

    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");

    @Override
    public void send(String from, String to, String subject, String msg, boolean isHtmlFormat) {
        sendWithCC(from, to, null, subject, msg, isHtmlFormat);
    }

    public void sendWithCC(String from, String to, String cc, String subject, String msg, boolean isHtmlFormat) {
        log.debug("To:" + to + ", From:" + from + ", Subject:" + subject);

        Message message = new Message();
        if (from != null && from.length() > 0) {
            message.setFrom(from);
        } else {
            message.setFrom(defaultSender);
        }
        message.addTo(to);
        if (cc != null && !cc.isEmpty()) {
            message.addCc(cc);
        }
        if (subjectPrefix != null) {
            subject = subjectPrefix + " " + subject;
        }
        if (optionalBccAddress != null && !optionalBccAddress.isEmpty()) {
            message.addBcc(optionalBccAddress);
        }
        message.setSubject(subject);
        message.setBody(msg);
        message.setBodyType(isHtmlFormat ? Message.BodyType.HTML_TEXT : Message.BodyType.PLAIN_TEXT);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Override
    public Response sendNotificationRequest(@WebParam(name = "req", targetNamespace = "") NotificationRequest req) {
        HashMap<String,String> params = new HashMap<String,String>();
        for(NotificationParam param : req.getParamList()) {
            params.put(param.getName(), param.getValue());
        }
        return this.sendNotification(req.getNotificationType(), params);
    }


    private Response sendNotification(final String notificationName, final HashMap<String,String> params) {
        log.info("Sending Notification: " + notificationName + ", Params:" + params);
        Response response = new Response(ResponseStatus.SUCCESS);
        if (StringUtils.isEmpty(notificationName)) {
            response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
            response.setErrorText("System Message Id must be set.");
            log.warn("Sending Notification Error: " + response);
            return response;
        }

        if (params.containsKey(MailTemplateParameters.USER_ID.value())) {
            String userid = params.get(MailTemplateParameters.USER_ID.value());
            User user = userManager.getUserWithDependent(userid, true).getUser();
            if (user != null) {
                if (!params.containsKey(MailTemplateParameters.TO.value())) {
                    if (StringUtils.isEmpty(user.getEmail()) || !MailSenderUtils.isEmailValid(user.getEmail())) {
                        log.warn("Email field is empty or invalid for user with id = " + userid + ". TO field of email is empty.");
                    } else {
                        params.put(MailTemplateParameters.TO.value(), user.getEmail());
                    }
                }
                if (!params.containsKey(MailTemplateParameters.FIRST_NAME.value())) {
                    params.put(MailTemplateParameters.FIRST_NAME.value(), user.getFirstName());
                }
                if (!params.containsKey(MailTemplateParameters.LAST_NAME.value())) {
                    params.put(MailTemplateParameters.LAST_NAME.value(), user.getLastName());
                }
            } else {
                log.warn("User with id = " + userid + " is not exists in the system.");
            }
        }

        NotificationDto sysMessageDto = sysMessageService.getNotificationByName(notificationName);
        if (sysMessageDto == null) {
            response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
            response.setErrorText("System Message with name = " + notificationName + " doesn't exist in the system.");
            log.warn("Sending Notification Error: " + response);
            return response;
        }
        String providerScript = sysMessageDto.getProviderScriptName();
        if (sysMessageDto.isUseProviderScript()) {
            try {
                Map<String, Object> bindingMap = new HashMap<String, Object>();

                ScriptIntegration se = ScriptFactory.createModule(scriptEngine);
                NotificationMessageProvider messageProvider = (NotificationMessageProvider) se.instantiateClass(bindingMap, "/msgprovider/" + providerScript);
                List<Message> messages = messageProvider.build(params);
                if (messages == null || messages.size() == 0) {
                    log.info("No one message was not built in " + providerScript + ".");
                    return response;
                }
                for (Message message : messages) {
                    try {
                        mailSender.send(message);
                    } catch (Exception e) {
                        log.error(e.toString());
                    }
                }
            } catch (Throwable ex) {
                response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
                response.setErrorText(ex.getMessage());
                response.setStatus(ResponseStatus.FAILURE);
                log.error("Sending Notification Error: " + response);
                return response;
            }

        } else if (sysMessageDto.getMailTemplate() != null) {
            String toAddress = params.get(MailTemplateParameters.TO.value());
            if (StringUtils.isEmpty(toAddress) || !MailSenderUtils.isEmailArrayValid(toAddress)) {
                response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
                response.setErrorText("Address field TO must be in parameters list.");
                log.warn("Sending Notification Error: " + response);
                return response;
            }
            String fromAddress = params.get(MailTemplateParameters.FROM.value());
            String bccAddress = params.get(MailTemplateParameters.BCC.value());
            String ccAddress = params.get(MailTemplateParameters.CC.value());

            Message message = new Message();
            if(toAddress.contains(",")) {
                for(String addr : toAddress.split(",")) {
                    message.addTo(addr.trim());
                }
            } else {
                message.addTo(toAddress);
            }
            if (StringUtils.isNotEmpty(fromAddress)) {
                message.setFrom(fromAddress);
            } else {
                message.setFrom(defaultSender);
            }
            if (StringUtils.isNotEmpty(bccAddress)) {
                message.addBcc(bccAddress);
            }
            if (StringUtils.isNotEmpty(ccAddress)) {
                message.addCc(ccAddress);
            }
            message.setBodyType(sysMessageDto.getMailTemplate().getType() == MessageBodyType.HTML ? Message.BodyType.HTML_TEXT : Message.BodyType.PLAIN_TEXT);
            message.setSubject(sysMessageDto.getMailTemplate().getSubject());
            if (StringUtils.isNotEmpty(sysMessageDto.getMailTemplate().getAttachmentFilePath())) {
                message.addAttachments(res.getString("uploadDir") + File.separatorChar + sysMessageDto.getMailTemplate().getAttachmentFilePath());
            }
            String body = MailSenderUtils.parseBody(sysMessageDto.getMailTemplate().getBody(), params);
            message.setBody(body);
            try {
                mailSender.send(message);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        log.info("Send Notification SUCCESS: " + notificationName);
        return response;
    }

    public String getDefaultSender() {
        return defaultSender;
    }

    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }

    public String getSubjectPrefix() {
        return subjectPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(String scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public UserDataWebService getUserManager() {
        return userManager;
    }

    public void setUserManager(UserDataWebService userManager) {
        this.userManager = userManager;
    }

    public AuditHelper getAuditHelper() {
        return auditHelper;
    }

    public void setAuditHelper(AuditHelper auditHelper) {
        this.auditHelper = auditHelper;
    }

    public String getOptionalBccAddress() {
        return optionalBccAddress;
    }

    public void setOptionalBccAddress(String optionalBccAddress) {
        this.optionalBccAddress = optionalBccAddress;
    }

    public NotificationService getSysMessageService() {
        return sysMessageService;
    }

    public void setSysMessageService(NotificationService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }
}
