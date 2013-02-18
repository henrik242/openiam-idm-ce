package org.openiam.webadmin.role;

/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the Lesser GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceRole;
import org.openiam.idm.srvc.res.dto.ResourceRoleId;
import org.openiam.idm.srvc.res.dto.ResourceType;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleResourceController extends CancellableFormController {

	protected RoleDataWebService roleDataService;
    protected ResourceDataService resourceDataService;
    protected NavigatorDataWebService navigationDataService;

	protected String roleTypeCategory;
	protected String redirectView;

	protected String menuGroup;

	
	private static final Log log = LogFactory.getLog(RoleResourceController.class);

	public RoleResourceController() {
		super();
	}

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        List<ResourceType> resTypeList = resourceDataService.getAllResourceTypes();

        Map model = new HashMap();
        model.put("resTypeList", resTypeList);

        return model;
    }

    private void loadReferenceData(ModelAndView mav) {
        List<ResourceType> resTypeList = resourceDataService.getAllResourceTypes();
        mav.addObject("resTypeList", resTypeList);
    }

    private void restoreMenu(HttpServletRequest request, String userId) {

        List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGroup, userId, "en").getMenuList();
        request.setAttribute("menuL3", level3MenuList);
    }
	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		

		RoleResourceCommand roleCommand = new RoleResourceCommand();

		HttpSession session =  request.getSession();
		String userId = (String)session.getAttribute("userId");
	  	
		String roleId = (String)session.getAttribute("roleid");
		String domainId = (String)session.getAttribute("domainid");

        String type = request.getParameter("resType");
        if (type != null && !type.isEmpty()) {
            roleCommand.setResourceTypeId(type);
            prePopulateCurrentResourceSelection(roleCommand,domainId,roleId);

        }
        String mode = request.getParameter("mode");
        if (mode != null && "1".equals(mode)) {
            request.setAttribute("msg","The role has been successfully modified");
        }





        restoreMenu(request, userId);

		if (roleId != null) {		
			// used by the ui add/remove role and resource associations
			roleCommand.setDomainId(domainId);
			roleCommand.setRoleId(roleId);
		}
	
		request.setAttribute("menuGroup", "SECURITY_ROLE");


        return roleCommand;
	}


    private void prePopulateCurrentResourceSelection(RoleResourceCommand roleCommand,
                                                     String domainId, String roleId) {

        String typeId = roleCommand.getResourceTypeId();
        if (typeId == null || typeId.isEmpty()) {
            return;

        }

        List<Resource> fullResList = new ArrayList<Resource>();
        List<Resource> resList =   resourceDataService.getResourcesByType(typeId);
            //resourceDataService.getAllResources();

        List<Resource> roleResourceList =  resourceDataService.getResourcesForRole(domainId, roleId);


        // for each role in the main list, check the userRole list to see if its there
        for (Resource res : resList) {
            boolean found = false;
            if (roleResourceList != null) {
                for (Resource r : roleResourceList ) {
                    if (res.getResourceId().equalsIgnoreCase(r.getResourceId())) {
                        res.setSelected(true);
                        fullResList.add(res);
                        found = true;
                    }
                }
            }
            if (!found) {
                fullResList.add(res);
            }
        }
        roleCommand.setResourceList(fullResList);
    }



    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(),true));
    }



	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.info("RoleResourceController - onSubmit called.");
		
		RoleResourceCommand roleCommand = (RoleResourceCommand)command;

        String userId = (String)request.getSession().getAttribute("userId");
		String domainId = (String)request.getSession().getAttribute("domainid");
		String login = (String)request.getSession().getAttribute("login");

        ModelAndView mav =  new ModelAndView(getSuccessView());
        prePopulateCurrentResourceSelection(roleCommand, roleCommand.getDomainId(),
                roleCommand.getRoleId());

        mav.addObject("roleResCmd", roleCommand);
        loadReferenceData(mav);

        restoreMenu(request, userId);

        return mav;


            // current resource-role list
		/*List<Resource> curRoleResList = resourceDataService.getResourcesForRole(roleCommand.getDomainId(), roleCommand.getRoleId());
		List<Resource> newRoleResList = roleCommand.getResourceList();
	
		//
		log.info("comparing new resource selection with current selecton.");
		
		if (newRoleResList != null) {
			for (Resource res  : newRoleResList) {
				log.info("Checking resource id=" + res.getResourceId());
				Resource curRes = getCurrentResource(res, curRoleResList);
				if (curRes == null && res.getSelected()) {
					// link role and resource
					log.info("Adding resource (1) " + res.getResourceId() + " to role=" + roleCommand.getRoleId());
					
					resourceDataService.addResourceRole(getResourceRole(res, 
							roleCommand.getRoleId() ,roleCommand.getDomainId()));

                    auditHelper.addLog("MODIFY", domainId,	login,
                                    "WEBCONSOLE", userId, "0", "ROLE", roleCommand.getRoleId(),
                                    null,   "SUCCESS", null,  "ADD RESOURCE",
                                    res.getResourceId(), null, null,
                                    roleCommand.getRoleId() + "-" + roleCommand.getDomainId(), request.getRemoteHost());


				}else {
						log.info("Check if resource should be removed");
						log.info("Current Res=" + curRes);
						log.info("Res Selected=" + res.getSelected());
						   if (!res.getSelected() && curRes != null ) {
							   log.info("attemptng to remove association to resource: " + res.getResourceId());
							// remove the association
							ResourceRole r = getResourceRole(curRes, 
									roleCommand.getRoleId() ,roleCommand.getDomainId());
									
							if (r != null) {
								log.info("removing resource " + res.getResourceId() + " to role=" + roleCommand.getRoleId());
								
								resourceDataService.removeResourceRole(r.getId());

                                auditHelper.addLog("MODIFY", domainId,	login,
                                    "WEBCONSOLE", userId, "0", "ROLE", roleCommand.getRoleId(),
                                    null,   "SUCCESS", null,  "REMOVE RESOURCE",
                                    r.getId().getResourceId(), null, null,
                                    roleCommand.getRoleId() + "-" + roleCommand.getDomainId(), request.getRemoteHost());
							    }
						}
					
				}
			}
		}

        prePopulateCurrentResourceSelection(roleCommand, roleCommand.getDomainId(), roleCommand.getRoleId());

        // pass on a message that we have saved the details
        successUrl = successUrl + "&mode=1";
        return new ModelAndView(successUrl);

       */
		
	}
	
	private ResourceRole getResourceRole(Resource res, String roleId, String domainId) {
		ResourceRole rr = new ResourceRole();
		ResourceRoleId id = new ResourceRoleId();
		id.setDomainId(domainId);
		id.setRoleId(roleId);
		id.setResourceId(res.getResourceId());
		id.setPrivilegeId("na");
		rr.setId(id);
		return rr;
	}
	
	private Resource getCurrentResource(Resource newRes, List<Resource> curRoleResList) {
		if (curRoleResList == null ) {
			return null;
		}
		for (Resource curRes : curRoleResList) {
			if (curRes.getResourceId().equalsIgnoreCase(newRes.getResourceId())) {
				return curRes;
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



	public String getRoleTypeCategory() {
		return roleTypeCategory;
	}



	public void setRoleTypeCategory(String roleTypeCategory) {
		this.roleTypeCategory = roleTypeCategory;
	}


	public RoleDataWebService getRoleDataService() {
		return roleDataService;
	}


	public void setRoleDataService(RoleDataWebService roleDataService) {
		this.roleDataService = roleDataService;
	}


	public ResourceDataService getResourceDataService() {
		return resourceDataService;
	}


	public void setResourceDataService(ResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}


	public NavigatorDataWebService getNavigationDataService() {
		return navigationDataService;
	}



	public void setNavigationDataService(
			NavigatorDataWebService navigationDataService) {
		this.navigationDataService = navigationDataService;
	}



	public String getMenuGroup() {
		return menuGroup;
	}



	public void setMenuGroup(String menuGroup) {
		this.menuGroup = menuGroup;
	}
}
