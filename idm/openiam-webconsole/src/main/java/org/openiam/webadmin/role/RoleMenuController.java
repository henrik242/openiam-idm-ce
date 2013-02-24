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
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RoleMenuController extends CancellableFormController {

	protected RoleDataWebService roleDataService;
    protected ResourceDataService resourceDataService;
    protected NavigatorDataWebService navigationDataService;

	protected String roleTypeCategory;
	protected String redirectView;
    protected String menuGroup;


	private static final Log log = LogFactory.getLog(RoleMenuController.class);

	public RoleMenuController() {
		super();
	}

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map model = new HashMap();
        model.put("rootMenuList", getRootTree());

        return model;
    }

    private List<Menu> getRootTree() {
        List<Menu> menuList = new LinkedList<Menu>();

        List<Menu> rootMenuList =  navigationDataService.menuGroup("ROOT","en").getMenuList();

        for (Menu m : rootMenuList) {
            menuList.add(m);

            List<Menu> subMenu = navigationDataService.menuGroup(m.getId().getMenuId(),"en").getMenuList();
            if ( subMenu != null && !subMenu.isEmpty()) {

                for ( Menu sm : subMenu ) {
                    sm.setMenuName("-->" + sm.getMenuName() );
                    menuList.add(sm);

                }

            }

        }
        return menuList;


    }


    private void loadReferenceData(ModelAndView mav) {

        mav.addObject("rootMenuList", getRootTree());
    }


	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

        RoleMenuCommand roleCommand = new RoleMenuCommand();

        HttpSession session =  request.getSession();
        String userId = (String)session.getAttribute("userId");

        String roleId = (String)session.getAttribute("roleid");
        String domainId = (String)session.getAttribute("domainid");

        String parentMenu = request.getParameter("parentMenu");
        if (parentMenu != null) {

            roleCommand.setMenuId(parentMenu);

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







    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(),true));
    }


    private List<Menu> prePopulateCurrentResourceSelection(String parentMenuId,
                                                            String domainId, String roleId) {

        List<Menu> menuList = null;


        List<Menu> fullMenuList = new LinkedList<Menu>();

        if (parentMenuId != null && !parentMenuId.isEmpty()) {
            menuList =navigationDataService.menuGroup(parentMenuId, "en").getMenuList();
        }else {
            menuList= navigationDataService.menuGroup("ROOT", "en").getMenuList();
        }

        // get menus in role
        List<Menu> roleMenuList =  navigationDataService.getMenusByRole(roleId,domainId).getMenuList();

        // for each role in the main list, check the userRole list to see if its there
        if (menuList != null) {
            for (Menu menu : menuList) {
                boolean found = false;
                if (roleMenuList != null) {
                    for (Menu m : roleMenuList ) {
                        if (menu.getId().getMenuId().equalsIgnoreCase(m.getId().getMenuId())) {
                            menu.setSelected(true);
                            fullMenuList.add(menu);
                            found = true;
                        }
                    }
                }
                if (!found) {
                    fullMenuList.add(menu);
                }
            }
        }
        if (!fullMenuList.isEmpty() ) {

            return fullMenuList;
        }

        return null;



    }


    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        log.info("RoleResourceController - onSubmit called.");

        RoleMenuCommand roleCommand = (RoleMenuCommand)command;

        String userId = (String)request.getSession().getAttribute("userId");
        String domainId = (String)request.getSession().getAttribute("domainid");
        String login = (String)request.getSession().getAttribute("login");

        ModelAndView mav =  new ModelAndView(getSuccessView());
        List<Menu> menuList =  prePopulateCurrentResourceSelection(roleCommand.getMenuId(), roleCommand.getDomainId(),
                roleCommand.getRoleId());

        mav.addObject("roleMenuCmd", roleCommand);
        mav.addObject("menuList", menuList);
        mav.addObject("roleid", roleCommand.getRoleId());
        mav.addObject("domainid", roleCommand.getDomainId());


        mav.addObject("parentMenu", roleCommand.getMenuId());

        loadReferenceData(mav);

        restoreMenu(request, userId);

        return mav;




    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {

        String parentMenu = request.getParameter("parentMenu");
        String roleId = (String)request.getSession().getAttribute("roleid");
        String domainId = (String)request.getSession().getAttribute("domainid");

        List<Menu> menuList =  prePopulateCurrentResourceSelection(parentMenu,domainId, roleId);

        if (menuList != null ) {
            controlModel = new HashMap();
            controlModel.put("menuList", menuList);
            controlModel.put("parentMenu", parentMenu);

        }
        return super.showForm(request, response, errors, controlModel);
    }

    private void restoreMenu(HttpServletRequest request, String userId) {

        List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGroup, userId, "en").getMenuList();
        request.setAttribute("menuL3", level3MenuList);
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
