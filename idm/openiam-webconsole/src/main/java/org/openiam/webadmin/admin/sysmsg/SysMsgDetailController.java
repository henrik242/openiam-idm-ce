package org.openiam.webadmin.admin.sysmsg;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.msg.dto.NotificationConfig;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

//import org.openiam.idm.srvc.audit.service.AuditLogUtil;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.msg.ws.SysMessageWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.openiam.idm.srvc.user.ws.UserDataWebService;


public class SysMsgDetailController extends SimpleFormController {


	private static final Log log = LogFactory.getLog(SysMsgDetailController.class);
	private SysMessageWebService sysMessageService; 
 	protected String redirectView;
	
 	protected MailService mailService;
 	protected String emailAddress;
	private UserDataWebService userMgr;
	protected LoginDataWebService loginManager;
	
	
	public SysMsgDetailController() {
		super();
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true) );
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {


		SysMsgCommand msgCmd = new SysMsgCommand();

		String msgId = request.getParameter("msgId");
		if (msgId == null) {
			return msgCmd;
		}
		
		NotificationConfig sysMessage = sysMessageService.getMessageById(msgId).getSysMessage();
		
		msgCmd.setMsg(sysMessage);
	
		return msgCmd;

}

	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.info("OrgPolicyDetailController:onSubmit called.");
		
		SysMsgCommand msgCmd = (SysMsgCommand)command;
		NotificationConfig sysMessage = msgCmd.getMsg();

		String btn = request.getParameter("btn");
		if (btn != null && btn.equalsIgnoreCase("Delete")) {
			sysMessageService.removeMessage(sysMessage.getNotificationConfigId());
			ModelAndView mav = new ModelAndView("/deleteconfirm");
	        mav.addObject("msg", "Location has been successfully deleted.");
	        return mav;
		}else if (btn != null && btn.equalsIgnoreCase("Send Msg")) {
			//sendMsg(msgCmd);
		}else {	
			if (sysMessage.getNotificationConfigId() == null || sysMessage.getNotificationConfigId().length()  == 0) {
				sysMessage.setNotificationConfigId(null);
				sysMessageService.addMessage(sysMessage);
			}else {
				sysMessageService.updateMessage(sysMessage);
			}
		}
		
		ModelAndView mav = new ModelAndView(new RedirectView(redirectView, true));
		mav.addObject("sysMsgCmd", msgCmd);
		
		return mav;
	}


		

	
	private Login getPrimaryLogin(List<Login> principalList) {
		if (principalList == null) {
			return null;
		}
		for (Login lg : principalList) {
			if (lg.getId().getManagedSysId().equalsIgnoreCase("0")) {
				return lg;
			}
		}
		return null;
	}
	

    

	public String getRedirectView() {
		return redirectView;
	}

	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}

	public SysMessageWebService getSysMessageService() {
		return sysMessageService;
	}

	public void setSysMessageService(SysMessageWebService sysMessageService) {
		this.sysMessageService = sysMessageService;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public LoginDataWebService getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}



	public UserDataWebService getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}




	



	

}
