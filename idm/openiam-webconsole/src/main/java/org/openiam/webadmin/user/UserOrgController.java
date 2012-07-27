package org.openiam.webadmin.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.role.ws.RoleListResponse;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserResponse;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


public class UserOrgController extends CancellableFormController {


    protected OrganizationDataService orgManager;
	protected NavigatorDataWebService navigationDataService;
	protected String redirectView;
	protected ProvisionService provRequestService;
	protected UserDataWebService userMgr;

	private static final Log log = LogFactory.getLog(UserOrgController.class);


	public UserOrgController() {
		super();
	}
	

	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true) );
		
	}

    @Override
      protected ModelAndView onCancel(Object command) throws Exception {
          return new ModelAndView(new RedirectView(getCancelView(),true));
     }

	
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		log.info("refernceData called.");
		
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
	
		return dataMap;
		

	}
	


	@Override
	protected Object formBackingObject(HttpServletRequest request)		throws Exception {
		
		
		
		UserOrgCommand orgCmd = new UserOrgCommand();
		User usr = null;
        String primaryAffiliation = null;
        
		HttpSession session =  request.getSession();
		String userId = (String)session.getAttribute("userId");
               
		
		String menuGrp = request.getParameter("menugrp");
		String personId = request.getParameter("personId");
		
		// get the level 3 menu
		List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGrp, userId, "en").getMenuList();
		request.setAttribute("menuL3", level3MenuList);	
		request.setAttribute("personId", personId);				

		orgCmd.setPerId(personId);

        if (personId != null) {
            usr =  userMgr.getUserWithDependent(personId,false).getUser();
            if (usr != null) {
                primaryAffiliation = usr.getCompanyId();
            }
            
        }
        

        List<Organization> fullList = new ArrayList<Organization>();
        List<Organization> orgList =  orgManager.getOrganizationList(null, "ACTIVE");

        if ( orgList != null) {
            for ( Organization o : orgList) {
                if (primaryAffiliation != null && o.getOrgId().equalsIgnoreCase(primaryAffiliation)) {
                    o.setSelected(true);
                    o.setOrganizationName(o.getOrganizationName() + "(Primary Affiliation)");
                }
            }
        }

        List<Organization> userAffiliations = orgManager.getOrganizationsForUser(personId);
        

        if (userAffiliations != null && !userAffiliations.isEmpty()) {
            for (Organization o : orgList) {
                boolean found = false;
                for (Organization userOrg : userAffiliations) {
                    if (    userOrg.getOrgId().equalsIgnoreCase(o.getOrgId())  ) {
                        userOrg.setSelected(true);
                        fullList.add(userOrg);
                        found = true;
                    }

                }
                if (!found) {
                    fullList.add(o);

                }

            }

        }else {
            // set the prmary affiliation if any
            if ( orgList != null) {

                fullList.addAll(orgList);
            }
        }

        orgCmd.setOrgList(fullList);

		return orgCmd;
		
	}

		
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {

        System.out.println("onSubmit for UserOrgController...");
	
		User usr = null;
        UserOrgCommand orgCmd = (UserOrgCommand)command;
        List<Organization> currentOrgList = null;
        String primaryAffiliation = null;

		HttpSession session = request.getSession();
		
		// get the current user object
		String personId = orgCmd.getPerId();

        String url =  redirectView + "&personId=" + personId + "&menugrp=QUERYUSER";



		UserResponse usrResp =  userMgr.getUserWithDependent(personId, true);
		if (usrResp.getStatus() == ResponseStatus.SUCCESS ) {
			usr = usrResp.getUser();
            if (usr != null) {
                primaryAffiliation = usr.getCompanyId();
            }
		}


		ProvisionUser pUser = new ProvisionUser(usr);


		
        currentOrgList = orgManager.getOrganizationsForUser(personId);

		List<Organization> newOrgList = orgCmd.getOrgList();
         List<Organization> provOrgList = new ArrayList<Organization>();
        if ( newOrgList != null) {
            for (Organization o : newOrgList) {
                if (o.getSelected()) {
                    if (primaryAffiliation != null && o.getOrgId().equalsIgnoreCase(primaryAffiliation))  {
                        // dont save the primary affiliation in this list. We have already have org associated with the user
                        continue;
                        
                    }else {
                        provOrgList.add(o);
                    }

                }
            }

        }



        pUser.setUserAffiliations(provOrgList);
		provRequestService.modifyUser(pUser);

		return new ModelAndView(new RedirectView(url, true));

	}


	private boolean isCurrentOrgInList(Organization curOrg, List<Organization> newOrgList) {
		if (newOrgList != null) {
			for ( Organization o : newOrgList) {
				if (o.getOrgId().equals(curOrg.getOrgId())) {
					System.out.println("-Affiliation found in currentList=" + curOrg.getOrgId());
					return true;
				}
			}
		}
		log.info("-Organization not found in currentList=" + curOrg.getOrgId());
		return false;
	}

	


	public NavigatorDataWebService getNavigationDataService() {
		return navigationDataService;
	}


	public void setNavigationDataService(
			NavigatorDataWebService navigationDataService) {
		this.navigationDataService = navigationDataService;
	}


	public String getRedirectView() {
		return redirectView;
	}


	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}


	public ProvisionService getProvRequestService() {
		return provRequestService;
	}


	public void setProvRequestService(ProvisionService provRequestService) {
		this.provRequestService = provRequestService;
	}


	public UserDataWebService getUserMgr() {
		return userMgr;
	}


	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }
}
