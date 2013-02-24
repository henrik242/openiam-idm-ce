/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
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


package org.openiam.idm.srvc.menu.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.dto.Permission;
import org.openiam.idm.srvc.menu.service.NavigatorDataService;
import org.openiam.idm.srvc.role.ws.RoleListResponse;
import org.openiam.idm.srvc.role.dto.Role;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * @author suneet
 *
 */
@WebService(endpointInterface = "org.openiam.idm.srvc.menu.ws.NavigatorDataWebService", 
		targetNamespace = "urn:idm.openiam.org/srvc/menu/service",
		portName = "NavigationWebServicePort",
		serviceName = "NavigationWebService")
public class NavigatorDataWebServiceImpl implements NavigatorDataWebService {

	NavigatorDataService navigatorDataService;
	private static final Log log = LogFactory.getLog(NavigatorDataWebServiceImpl.class);
	
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#addMenu(org.openiam.idm.srvc.menu.dto.Menu)
	 */
	public MenuResponse addMenu(Menu data) {
		MenuResponse resp = new MenuResponse(ResponseStatus.SUCCESS);
		navigatorDataService.addMenu(data); 
		if (data.getId().getMenuId() != null) {
			resp.setMenu(data);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;

	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#getAllMenuOptionIDs(java.lang.String, java.lang.String)
	 */
	public Response getAllMenuOptionIDs(String parentMenuGroupId, String languageCd) {
		Response resp = new Response(ResponseStatus.SUCCESS);
		String str = navigatorDataService.getAllMenuOptionIDs(parentMenuGroupId, languageCd);
		if (str != null && str.length() > 0) {
			resp.setResponseValue(str);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;
	}



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#getMenu(java.lang.String, java.lang.String)
	 */
	public MenuResponse getMenu(String menuId, String languageCd) {
		MenuResponse resp = new MenuResponse(ResponseStatus.SUCCESS);
		Menu menu = navigatorDataService.getMenu(menuId, languageCd); 
		if (menu != null) {
			resp.setMenu(menu);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#menuGroup(java.lang.String, java.lang.String)
	 */
	public MenuListResponse menuGroup(String menuGroupId, String langCd) {
		MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);
		List<Menu> menuList = navigatorDataService.menuGroup(menuGroupId, langCd); 
		if (menuList != null) {
			resp.setMenuList(menuList);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#menuGroupByUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public MenuListResponse menuGroupByUser(String menuGroupId, String userId,	String languageCd) {
		MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);
		List<Menu> menuList = navigatorDataService.menuGroupByUser(menuGroupId, userId, languageCd); 
		if (menuList != null) {
			resp.setMenuList(menuList);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#menuGroupSelectedByUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public MenuListResponse menuGroupSelectedByUser(String menuGroupId, String userId,String languageCd) {
		MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);
		List<Menu> menuList = navigatorDataService.menuGroupSelectedByUser(menuGroupId, userId, languageCd); 
		log.info("menuGroupSelectedByUser: menuList=" + menuList);
		if (menuList != null) {
			resp.setMenuList(menuList);
		}else {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#removeMenu(java.lang.String, boolean)
	 */
	public Response removeMenu(String menuId, boolean deleteChildren) {
		Response resp = new Response(ResponseStatus.SUCCESS);
		int retval = navigatorDataService.removeMenu(menuId, deleteChildren);
		if (retval < 1) {
			resp.setStatus(ResponseStatus.FAILURE);
		}
		return resp;

	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.menu.ws.NavigatorDataWebService#updateMenu(org.openiam.idm.srvc.menu.dto.Menu)
	 */
	public Response updateMenu(Menu data) {
		Response resp = new Response(ResponseStatus.SUCCESS);
		navigatorDataService.updateMenu(data);
		return resp;
	}

	public NavigatorDataService getNavigatorDataService() {
		return navigatorDataService;
	}

	public void setNavigatorDataService(NavigatorDataService navigatorDataService) {
		this.navigatorDataService = navigatorDataService;
	}


    @Override
    public PermissionResponse addPermission(@WebParam(name = "menuId", targetNamespace = "") String menuId, @WebParam(name = "roleId", targetNamespace = "") String roleId, @WebParam(name = "serviceId", targetNamespace = "") String serviceId) {
        PermissionResponse resp = new PermissionResponse(ResponseStatus.SUCCESS);

        Permission p = navigatorDataService.addPermission(menuId,roleId,serviceId);
        if (p == null) {
            resp.setStatus(ResponseStatus.FAILURE);

        }else {
            resp.setPermission(p);
        }
        return resp;
    }

    @Override
    public PermissionResponse updatePermission(@WebParam(name = "permission", targetNamespace = "") Permission permission) {
        PermissionResponse resp = new PermissionResponse(ResponseStatus.SUCCESS);

        Permission p = navigatorDataService.updatePermission(permission);
        if (p == null) {
            resp.setStatus(ResponseStatus.FAILURE);

        }else {
            resp.setPermission(p);
        }
        return resp;

    }

    @Override
    public PermissionResponse getPermission(@WebParam(name = "menuId", targetNamespace = "") String menuId, @WebParam(name = "roleId", targetNamespace = "") String roleId, @WebParam(name = "serviceId", targetNamespace = "") String serviceId) {
        PermissionResponse resp = new PermissionResponse(ResponseStatus.SUCCESS);

        Permission p = navigatorDataService.getPermission(menuId, roleId, serviceId);
        if (p == null) {
            resp.setStatus(ResponseStatus.FAILURE);

        }else {
            resp.setPermission(p);
        }
        return resp;
    }

    @Override
    public PermissionListResponse getAllPermissions() {
        PermissionListResponse resp = new PermissionListResponse(ResponseStatus.SUCCESS);

        List<Permission> p = navigatorDataService.getAllPermissions();
        if (p == null) {
            resp.setStatus(ResponseStatus.FAILURE);

        }else {
            resp.setPermissionList(p);
        }
        return resp;
    }

    @Override
    public void removePermission(@WebParam(name = "menuId", targetNamespace = "") String menuId, @WebParam(name = "roleId", targetNamespace = "") String roleId, @WebParam(name = "serviceId", targetNamespace = "") String serviceId) {
       navigatorDataService.removePermission(menuId, roleId, serviceId);
    }

    @Override
    public int removeAllPermissions() {
        return navigatorDataService.removeAllPermissions();
    }

    @Override
    public RoleListResponse getRolesByMenu(@WebParam(name = "menuId", targetNamespace = "") String menuId) {
        RoleListResponse resp = new RoleListResponse(ResponseStatus.SUCCESS);

        List<Role> roleList = navigatorDataService.getRolesByMenu(menuId);

        if (roleList != null) {
            resp.setRoleList(roleList);
        }else {
            resp.setStatus(ResponseStatus.FAILURE);
        }
        return resp;

    }

    @Override
    public MenuListResponse getMenusByRole(@WebParam(name = "roleId", targetNamespace = "") String roleId, @WebParam(name = "serviceId", targetNamespace = "") String serviceId) {

        MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);

        List<Menu> menuList = navigatorDataService.getMenusByRole(roleId,serviceId);

        if (menuList != null) {
            resp.setMenuList(menuList);
        }else {
            resp.setStatus(ResponseStatus.FAILURE);
        }
        return resp;
    }

    @Override
    public MenuListResponse getMenusByUser(@WebParam(name = "menuGroup", targetNamespace = "") String menuGroup, @WebParam(name = "roleId", targetNamespace = "") String roleId, @WebParam(name = "userId", targetNamespace = "") String userId) {
        MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);

        List<Menu> menuList = navigatorDataService.getMenusByUser(menuGroup,roleId,userId);

        if (menuList != null) {
            resp.setMenuList(menuList);
        }else {
            resp.setStatus(ResponseStatus.FAILURE);
        }
        return resp;
    }

    @Override
    public MenuListResponse getMenuFamily(@WebParam(name = "menuId", targetNamespace = "") String menuId, @WebParam(name = "languageCd", targetNamespace = "") String languageCd) {

        MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);

        List<Menu> menuList = navigatorDataService.getMenuFamily(menuId,languageCd);

        if (menuList != null) {
            resp.setMenuList(menuList);
        }else {
            resp.setStatus(ResponseStatus.FAILURE);
        }
        return resp;

    }

    @Override
    public MenuListResponse getMenuTree(@WebParam(name = "menuId", targetNamespace = "") String menuId, @WebParam(name = "languageCd", targetNamespace = "") String languageCd) {
        MenuListResponse resp = new MenuListResponse(ResponseStatus.SUCCESS);

        List<Menu> menuList = navigatorDataService.getMenuTree(menuId,languageCd);

        if (menuList != null) {
            resp.setMenuList(menuList);
        }else {
            resp.setStatus(ResponseStatus.FAILURE);
        }
        return resp;
    }
}
