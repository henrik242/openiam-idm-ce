package org.openiam.webadmin.user;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.dto.SearchAudit;
import org.openiam.idm.srvc.audit.ws.IdmAuditLogListResponse;
import org.openiam.idm.srvc.audit.ws.IdmAuditLogWebDataService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.auth.dto.Login;




public class UserHistoryController extends AbstractController {


	private static final Log log = LogFactory.getLog(UserHistoryController.class);
	private String successView;
	protected NavigatorDataWebService navigationDataService;	
	protected IdmAuditLogWebDataService auditService;
	protected Integer numberOfDays;

    protected UserDataWebService userMgr;
	protected LoginDataWebService loginManager;

	
	
	public UserHistoryController() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        System.out.println("UserHistoryController calle.d");

		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		String menuGrp = request.getParameter("menugrp");
		String personId = request.getParameter("personId");
		String userId = (String)request.getSession().getAttribute("userId");		
		
		UserGroupCommand userGroupCmd = new UserGroupCommand();
		

		List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGrp, userId, "en").getMenuList();
		request.setAttribute("menuL3", level3MenuList);	
		request.setAttribute("personId", personId);		
		
		//SearchAudit search = new SearchAudit();
		//search.setUserId(personId);
		// figure out a date to start the log
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.add(Calendar.DATE, (numberOfDays.intValue() * -1));
		//search.setStartDate(cal.getTime());

        Login lg =   loginManager.getPrimaryIdentity(personId).getPrincipal();
        if ( lg != null) {

            IdmAuditLogListResponse logListResp = auditService.eventsAboutUser(lg.getId().getLogin(), cal.getTime());
            if (logListResp != null && logListResp.getStatus() != ResponseStatus.FAILURE) {
                List<IdmAuditLog> logList = logListResp.getLogList();
                mav.addObject("auditLog", logList);
            }
        } else {

            SearchAudit search = new SearchAudit();
		    search.setUserId(personId);
		    search.setStartDate(cal.getTime());

             IdmAuditLogListResponse logListResp = auditService.search(search);
            if (logListResp != null && logListResp.getStatus() != ResponseStatus.FAILURE) {
                List<IdmAuditLog> logList = logListResp.getLogList();
                mav.addObject("auditLog", logList);
            }

        }
		
		mav.addObject("numOfDays", numberOfDays.intValue());
		
		return mav;
	}





	public String getSuccessView() {
		return successView;
	}


	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public NavigatorDataWebService getNavigationDataService() {
		return navigationDataService;
	}

	public void setNavigationDataService(
			NavigatorDataWebService navigationDataService) {
		this.navigationDataService = navigationDataService;
	}



	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public IdmAuditLogWebDataService getAuditService() {
		return auditService;
	}

	public void setAuditService(IdmAuditLogWebDataService auditService) {
		this.auditService = auditService;
	}


    public UserDataWebService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataWebService userMgr) {
        this.userMgr = userMgr;
    }

    public LoginDataWebService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataWebService loginManager) {
        this.loginManager = loginManager;
    }
}
