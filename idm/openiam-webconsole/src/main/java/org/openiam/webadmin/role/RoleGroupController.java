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
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
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

public class RoleGroupController extends CancellableFormController {

	protected RoleDataWebService roleDataService;
    protected NavigatorDataWebService navigationDataService;
    protected GroupDataWebService groupManager;

	protected String roleTypeCategory;
	protected String redirectView;

	protected String menuGroup;


	private static final Log log = LogFactory.getLog(RoleGroupController.class);

	public RoleGroupController() {
		super();
	}

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {


        List<Group> rootGroupList = groupManager.getAllGroupsWithDependents(false).getGroupList();

        Map model = new HashMap();
        model.put("rootGroupList", rootGroupList);


        return model;
    }

    private void loadReferenceData(ModelAndView mav) {
        List<Group> rootGroupList = groupManager.getAllGroupsWithDependents(false).getGroupList();
        mav.addObject("rootGroupList", rootGroupList);
    }

    private void restoreMenu(HttpServletRequest request, String userId) {

        List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGroup, userId, "en").getMenuList();
        request.setAttribute("menuL3", level3MenuList);
    }
	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		

		RoleGroupCommand roleCommand = new RoleGroupCommand();

		HttpSession session =  request.getSession();
		String userId = (String)session.getAttribute("userId");
	  	
		String roleId = (String)session.getAttribute("roleid");
		String domainId = (String)session.getAttribute("domainid");


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


    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {

        String parentGroup = request.getParameter("parentGrp");
        String roleId = (String)request.getSession().getAttribute("roleid");
        String domainId = (String)request.getSession().getAttribute("domainid");

        List<Group> groupList =  prePopulateCurrentResourceSelection(parentGroup,domainId, roleId);

        if (groupList != null ) {
            controlModel = new HashMap();
            controlModel.put("groupList", groupList);
            controlModel.put("parentGrp", parentGroup);

        }
        return super.showForm(request, response, errors, controlModel);
    }



    private List<Group> prePopulateCurrentResourceSelection(String parentGroupId,
                                                     String domainId, String roleId) {

        List<Group> groupList = null;


        List<Group> fullGroupList = new ArrayList<Group>();
        if (parentGroupId != null && !parentGroupId.isEmpty()) {
            groupList = groupManager.getChildGroups(parentGroupId,false).getGroupList();
        }else {
            groupList= groupManager.getAllGroupsWithDependents(false).getGroupList();
        }

        Group[] roleGroupAry =  roleDataService.getGroupsInRole(domainId,roleId).getGroupAry();




        // for each role in the main list, check the userRole list to see if its there
        if (groupList != null) {
            for (Group grp : groupList) {
                boolean found = false;
                if (roleGroupAry != null) {
                    for (Group g : roleGroupAry ) {
                        if (grp.getGrpId().equalsIgnoreCase(g.getGrpId())) {
                            grp.setSelected(true);
                            fullGroupList.add(grp);
                            found = true;
                        }
                    }
                }
                if (!found) {
                    fullGroupList.add(grp);
                }
            }
        }
        if (!fullGroupList.isEmpty() ) {
            //roleCommand.setGroupList(fullGroupList);
            return fullGroupList;
        }

        return null;



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

        RoleGroupCommand roleCommand = (RoleGroupCommand)command;

        String userId = (String)request.getSession().getAttribute("userId");
		String domainId = (String)request.getSession().getAttribute("domainid");
		String login = (String)request.getSession().getAttribute("login");

        ModelAndView mav =  new ModelAndView(getSuccessView());
        List<Group> groupList =  prePopulateCurrentResourceSelection(roleCommand.getGroupId(), roleCommand.getDomainId(),
                roleCommand.getRoleId());

        mav.addObject("roleGroupCmd", roleCommand);
        mav.addObject("groupList", groupList);
        mav.addObject("roleid", roleCommand.getRoleId());
        mav.addObject("domainid", roleCommand.getDomainId());
        mav.addObject("parentGrp", roleCommand.getGroupId());

        loadReferenceData(mav);

        restoreMenu(request, userId);

        return mav;



		
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

    public GroupDataWebService getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }
}
