
package org.openiam.selfsrvc.login;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.ws.IdmAuditLogWebDataService;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.selfsrvc.AppConfiguration;
import org.springframework.web.struts.DispatchActionSupport;
import org.apache.struts.action.ActionMessages;
import org.openiam.selfsrvc.helper.AuditHelper;


public class LogoutAction extends DispatchActionSupport {

 
  protected AppConfiguration appConfiguration;
  protected AuthenticationService authenticate;
  protected IdmAuditLogWebDataService auditService;


  public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
  	
      ActionErrors errors = new ActionErrors();
      Locale locale = getLocale(request);
      HttpSession session = request.getSession();

      String userId = request.getParameter("userId");
      if (userId == null) {
        userId = (String)session.getAttribute("userId");
      }

      String login = (String)request.getSession().getAttribute("login");
      String domain = (String) request.getSession().getAttribute("domainId");

      
      try {
    	  if (userId != null) {
    		  authenticate.globalLogout(userId);


                String nodeIP = null;

                try {
                    InetAddress addr = InetAddress.getLocalHost();


                    nodeIP = addr.getHostAddress();


                } catch (UnknownHostException e) {
                      e.printStackTrace();
                }

                IdmAuditLog log = getAuditLog("LOGOUT", domain,	login,
					"SELFSERVICE", userId, "0", "AUTHENTICATION",
                    userId, null, "SUCCESS", null,  null,null,
                 null,null, null, null, request.getRemoteHost(), login, domain, nodeIP);

               auditService.addLog(log);

    	  }
    	  
        session.invalidate(); // removes everything
        
		appConfiguration = (AppConfiguration)getWebApplicationContext().getBean("appConfiguration");

		request.setAttribute("logoUrl", appConfiguration.getLogoUrl());
		request.setAttribute("title", appConfiguration.getTitle());


        // remove time-sensitive token
        Cookie [] cookies = request.getCookies();
        if (cookies!= null) {
          for (int i = 0; i < cookies.length; i++){
            if (cookies[i].getName().equalsIgnoreCase("token")) {
               cookies[i].setMaxAge(0);
               response.addCookie(cookies[i]);
            }
          }
        }



      } catch (Exception e) {
          e.printStackTrace();
          errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.logout.failed"));

          saveErrors(request,errors);
          return (mapping.findForward("logoutfailed"));
       }
      
       return (mapping.findForward("logout"));
    }


     private IdmAuditLog getAuditLog(String action,String requestorDomainId, String requestorPrincipal,
			String srcSystem, String userId, String targetSystem, String objectType,
			String objectId, String objectName,
			String actionStatus, String linkedLogId, String attrName, String attrValue,
			String requestId, String reason, String sessionId,
            String reasonDetail,
            String requestIP, String targetPrincipal, String targetUserDomain, String nodeIP) {

		IdmAuditLog log = new IdmAuditLog();
		log.setObjectId(objectId);
		log.setObjectTypeId(objectType);
		log.setActionId(action);
		log.setActionStatus(actionStatus);
		log.setDomainId(requestorDomainId);
		log.setUserId(userId);
		log.setPrincipal(requestorPrincipal);
		log.setLinkedLogId(linkedLogId);
		log.setSrcSystemId(srcSystem);
		log.setTargetSystemId(targetSystem);
		log.setCustomAttrname1(attrName);
		log.setCustomAttrvalue1(attrValue);
		log.setRequestId(requestId);
		log.setReason(reason);
		log.setSessionId(sessionId);
        log.setReasonDetail(reasonDetail);
        log.setHost(requestIP);
        log.setCustomAttrname3("TARGET_IDENTITY");
        log.setCustomAttrvalue3(targetPrincipal);
        log.setCustomAttrname4("TARGET_DOMAIN");
        log.setCustomAttrvalue4(targetUserDomain);
        log.setNodeIP(nodeIP);

        return log;

	}



public AuthenticationService getAuthenticate() {
	return authenticate;
}

public void setAuthenticate(AuthenticationService authenticate) {
	this.authenticate = authenticate;
}

    public IdmAuditLogWebDataService getAuditService() {
        return auditService;
    }

    public void setAuditService(IdmAuditLogWebDataService auditService) {
        this.auditService = auditService;
    }
}