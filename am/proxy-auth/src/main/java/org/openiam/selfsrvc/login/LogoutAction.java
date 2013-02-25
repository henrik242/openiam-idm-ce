
package org.openiam.selfsrvc.login;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.sun.mail.iap.Response;
import org.apache.struts.action.*;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.ws.IdmAuditLogWebDataService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.auth.ws.LoginResponse;
import org.openiam.selfsrvc.AppConfiguration;
import org.springframework.web.struts.DispatchActionSupport;
import org.apache.struts.action.ActionMessages;


public class LogoutAction extends DispatchActionSupport {

 
  protected AppConfiguration appConfiguration;
  protected AuthenticationService authenticate;
  protected LoginDataWebService loginManager;
  protected IdmAuditLogWebDataService auditService;

    ResourceBundle res = ResourceBundle.getBundle("datasource");
    String COOKIE_DOMAIN = res.getString("COOKIE_DOMAIN");

  public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

      boolean found = false;
      ActionErrors errors = new ActionErrors();
      Locale locale = getLocale(request);
      HttpSession session = request.getSession();

      String principalName = request.getParameter("userid");
      if (principalName == null) {
        principalName = (String)session.getAttribute("userid");
      }


      
      try {
    	  if (principalName != null) {

              // get the identity for this user.
              LoginResponse resp = loginManager.getLoginByManagedSys("USR_SEC_DOMAIN", principalName, "0");
              if (resp.getStatus() == ResponseStatus.FAILURE) {
                  resp = loginManager.getLoginByManagedSys("IDM", principalName, "0");
                  if (resp.getStatus() == ResponseStatus.SUCCESS) {
                      found = true;
                  }
              }else {
                  found = true;
              }
              if (found) {

                 Login lg = resp.getPrincipal();


                 authenticate.globalLogout(resp.getPrincipal().getUserId());

                 String nodeIP = null;

                try {
                    InetAddress addr = InetAddress.getLocalHost();


                    nodeIP = addr.getHostAddress();


                    IdmAuditLog log = getAuditLog("LOGOUT", lg.getId().getDomainId(),	lg.getId().getLogin(),
					"IAM_PROXY", lg.getUserId(), "0", "AUTHENTICATION",
                    lg.getUserId(), null, "SUCCESS", null,  null,null,
                    null,null, null, null, request.getRemoteHost(), lg.getId().getDomainId(),
                    lg.getId().getLogin(), nodeIP);

                    auditService.addLog(log);


                } catch (UnknownHostException e) {
                      e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }




                 session.invalidate(); // removes everything

		         appConfiguration = (AppConfiguration)getWebApplicationContext().getBean("appConfiguration");

		         request.setAttribute("logoUrl", appConfiguration.getLogoUrl());
		         request.setAttribute("title", appConfiguration.getTitle());

                  Cookie[] cookieAry =  request.getCookies();
                  if (cookieAry != null) {
                      for (int i=0; i < cookieAry.length; i++) {
                         Cookie c = cookieAry[i];

                          System.out.println("- cookie value: " + c.getName() + " " + c.getDomain() + " " + c.getPath() + " " + c.getValue());

                          if (c.getName().equalsIgnoreCase("IAM_PARAM")) {
                              c.setMaxAge(0);
                              c.setPath("/");
                              if ( COOKIE_DOMAIN != null && COOKIE_DOMAIN.length() > 0 ) {
                                  c.setDomain(COOKIE_DOMAIN);

                              }

                              response.addCookie(c);
                          }

                      }
                  }

              }
    	  }

          if (!found) {
              // show a page that says logout failed.Invalid identity
              return (mapping.findForward("logoutfailed"));
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

    public LoginDataWebService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataWebService loginManager) {
        this.loginManager = loginManager;
    }

    public IdmAuditLogWebDataService getAuditService() {
        return auditService;
    }

    public void setAuditService(IdmAuditLogWebDataService auditService) {
        this.auditService = auditService;
    }
}