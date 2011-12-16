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

	private static final Log log = LogFactory.getLog(SysMessageDAO.class);
	static protected ResourceBundle notificationRes = ResourceBundle.getBundle("notification");

	public void sendToAllUsers() {
		log.info("sendToAllUsers was called, but is not implemented");
	}

	public void sendToGroup(String groupId) {
		log.info("sendToGroup was called, but is not implemented");
	}

	public void send(String from, String to, String subject, String msg) {
		System.out.println("To:" + to);
		System.out.println("From:" + from);
		System.out.println("Subject:" + subject);
		
		Message message = new Message();
		if (from != null && from.length()  > 0) {
			message.setFrom(from);
		}else {
			message.setFrom(defaultSender);
		}
		message.setTo(to);
		if (subjectPrefix != null) {
			subject = subjectPrefix + " " + subject;
		}
		if (optionalBccAddress != null) {
			message.setBcc(optionalBccAddress);
		}
		message.setSubject(subject);
		message.setBody(msg);
		try {
			mailSender.send(message);
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}

	public void sendWithCC(String from, String to,String cc, String subject, String msg) {
		Message message = new Message();
		if (from != null && from.length()  > 0) {
			message.setFrom(from);
		}else {
			message.setFrom(defaultSender);
		}
		message.setTo(to);
		message.setCc(cc);
		if (subjectPrefix != null) {
			subject = subjectPrefix + " " + subject;
		}
		if (optionalBccAddress != null) {
			message.setBcc(optionalBccAddress);
		}
		message.setSubject(subject);
		message.setBody(msg);
		try {
			mailSender.send(message);
		}catch(Exception e) {
			log.error(e);
		}
		
	}

	private  boolean isEmailValid(String email){
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  

		//Make the comparison case-insensitive.
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.msg.service.MailService#sendNotification(org.openiam.idm.srvc.msg.dto.NotificationRequest)
	 */
	public boolean sendNotification(NotificationRequest req) {
		log.debug("Send Notification called.");
		
		log.debug("sendNotification userId = " + req.getUserId());
		log.debug("sendNotification notificationType = " + req.getNotificationType());
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
		// for each notification, there will be entry in the property file
		String notificationDetl = notificationRes.getString(req.getNotificationType());
		int indx = notificationDetl.indexOf(";");
		String subject = notificationDetl.substring(0,indx);
		String emailScript = notificationDetl.substring(indx+1, notificationDetl.length());
		
		ScriptIntegration se = null;
		Map<String, Object> bindingMap = new HashMap<String, Object>();
		bindingMap.put("context", ac);	
		
		try {
			se = ScriptFactory.createModule(this.scriptEngine); 
		} catch(Exception e) {
			log.error(e);
			return false;
		}
		log.debug("Show email request object:" + req);
		
		bindingMap.put("user", usr);
		bindingMap.put("req", req);
		
		String emailBody = (String)se.execute(bindingMap, emailScript);
		
		send(null, usr.getEmail(), subject, emailBody);
		return true;
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
