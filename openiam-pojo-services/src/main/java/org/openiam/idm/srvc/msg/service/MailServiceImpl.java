package org.openiam.idm.srvc.msg.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebService(endpointInterface = "org.openiam.idm.srvc.msg.service.MailService",
		targetNamespace = "urn:idm.openiam.org/srvc/msg",
		portName = "EmailWebServicePort",
		serviceName = "EmailWebService")
public class MailServiceImpl implements MailService, ApplicationContextAware {

	private MailSender mailSender;
	private String defaultSender;
	private String subjectPrefix;
	private String optionalBccAddress;

	protected String scriptEngine;
	protected UserDataWebService userManager;
	protected AuditHelper auditHelper;

	public static ApplicationContext ac;

	private static final Log log = LogFactory.getLog(MailServiceImpl.class);
	private static final int SUBJECT_IDX = 0;
	private static final int SCRIPT_IDX = 1;

	static protected ResourceBundle notificationRes = ResourceBundle.getBundle("notification");

	public void sendToAllUsers() {
		log.warn("sendToAllUsers was called, but is not implemented");
	}

	public void sendToGroup(String groupId) {
		log.warn("sendToGroup was called, but is not implemented");
	}

	public void send(String from, String to, String subject, String msg) {
		sendWithCC(from, to, null, subject, msg);
	}

	public void sendWithCC(String from, String to, String cc, String subject, String msg) {
		log.debug("To:" + to + ", From:" + from + ", Subject:" + subject);

		Message message = new Message();
		if (from != null && from.length()  > 0) {
			message.setFrom(from);
		} else {
			message.setFrom(defaultSender);
		}
		message.setTo(to);
		if (cc != null && !cc.isEmpty()) {
			message.setCc(cc);
		}
		if (subjectPrefix != null) {
			subject = subjectPrefix + " " + subject;
		}
		if (optionalBccAddress != null && !optionalBccAddress.isEmpty()) {
			message.setBcc(optionalBccAddress);
		}
		message.setSubject(subject);
		message.setBody(msg);
		try {
			mailSender.send(message);
		}catch(Exception e) {
			log.error(e.toString());
		}
	}

	private  boolean isEmailValid(String email){
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean sendNotification(NotificationRequest req) {
		if (req == null) {
			return false;
		}
		log.debug("Send Notification called with notificationType = " + req.getNotificationType());

		if (req.getUserId() != null) {
			return sendEmailForUser(req);
		} else if (req.getTo() != null) {
			return sendCustomEmail(req);
		}
		return false;
	}

	private boolean sendCustomEmail(NotificationRequest req) {
		log.debug("sendNotification to = " + req.getTo());

		String[] emailDetails = fetchEmailDetails(req.getNotificationType());
		if (emailDetails == null) {
			return false;
		}

		Map<String, Object> bindingMap = new HashMap<String, Object>();
		bindingMap.put("context", ac);
		bindingMap.put("req", req);

		String emailBody = createEmailBody(bindingMap, emailDetails[SCRIPT_IDX]);
		if (emailBody != null) {
			sendWithCC(null, req.getTo(), req.getCc(), emailDetails[SUBJECT_IDX], emailBody);
			return true;
		}
		return false;
	}

	private boolean sendEmailForUser(NotificationRequest req) {
		log.debug("sendNotification userId = " + req.getUserId());
		// get the user object
		if (req.getUserId() == null) {
			return false;
		}
		User usr = userManager.getUserWithDependent(req.getUserId(), true).getUser();
		if (usr == null) {
			return false;
		}
		log.debug("Email address=" + usr.getEmail());

		if (usr.getEmail() == null || usr.getEmail().length() == 0) {
			log.error("Send notfication failed. Email was null for userId=" + usr.getUserId());
			return false;
		}

		if (!isEmailValid(usr.getEmail())) {
			log.error("Send notfication failed. Email was is not valid for userId=" + usr.getUserId() +
					" - " + usr.getEmail());
			return false;
		}
		String[] emailDetails = fetchEmailDetails(req.getNotificationType());
		if (emailDetails == null) {
			return false;
		}

		Map<String, Object> bindingMap = new HashMap<String, Object>();
		bindingMap.put("context", ac);
		bindingMap.put("user", usr);
		bindingMap.put("req", req);

		String emailBody = createEmailBody(bindingMap, emailDetails[SCRIPT_IDX]);
		if (emailBody != null) {
			send(null, usr.getEmail(), emailDetails[SUBJECT_IDX], emailBody);
			return true;
		}
		return false;
	}

	private String createEmailBody(Map<String, Object> bindingMap, String emailScript) {
		try {
			ScriptIntegration se = ScriptFactory.createModule(this.scriptEngine);
			return (String) se.execute(bindingMap, emailScript);
		} catch(Exception e) {
			log.error("createEmailBody():" + e.toString());
			return null;
		}
	}

	private String[] fetchEmailDetails(String notificationType) {
		// for each notification, there will be entry in the property file
		String notificationDetl = notificationRes.getString(notificationType);
		String[] details = notificationDetl.split(";", 2);
		if (details.length < 2) {
			log.warn("Mail not sent, invalid notificationType: " + notificationType);
			return null;
		}
		return details;
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

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException  {
		ac = applicationContext;
	}

	public UserDataWebService getUserManager() {
		return userManager;
	}

	public void setUserManager(UserDataWebService userManager) {
		this.userManager = userManager;
	}

	public static ResourceBundle getNotificationRes() {
		return notificationRes;
	}

	public static void setNotificationRes(ResourceBundle notificationRes) {
		MailServiceImpl.notificationRes = notificationRes;
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
}
