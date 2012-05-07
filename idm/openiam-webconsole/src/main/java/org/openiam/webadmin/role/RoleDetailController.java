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
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.role.ws.RoleResponse;
import org.openiam.webadmin.util.AuditHelper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class RoleDetailController extends CancellableFormController {

    protected RoleDataWebService roleDataService;
    protected MetadataWebService metadataService;
    protected String roleTypeCategory;
    protected String redirectView;
    protected AuditHelper auditHelper;
    protected NavigatorDataWebService navigationDataService;
    protected String menuGroup;

    private static final Log log = LogFactory.getLog(RoleDetailController.class);


    public RoleDetailController() {
        super();
    }


    private void addNewRowToAttributeList(List<RoleAttribute> attrList) {


        if (attrList == null) {
            attrList = new ArrayList<RoleAttribute>();

        }

        RoleAttribute ra = new RoleAttribute();
        ra.setName("**ENTER NAME**");
        ra.setValue("");
        ra.setRoleAttrId("NEW");
        attrList.add(ra);

    }

    private void attrSetToList(Set<RoleAttribute> attrSet, List<RoleAttribute> attrList) {

        if (attrSet == null || attrSet.isEmpty()) {
            return;
        }
        for (RoleAttribute attr : attrSet) {
            if (attr != null) {
                attrList.add(attr);
            }

        }


    }

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(), true));
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {

        log.info("RoleDetailController:formBackingObject called.");

        Role role = null;
        RoleDetailCommand roleCommand = new RoleDetailCommand();

        List<RoleAttribute> attrList = new ArrayList<RoleAttribute>();

        roleCommand.setTypeList(metadataService.getTypesInCategory(roleTypeCategory).getMetadataTypeAry());

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        String roleId = request.getParameter("roleid");
        String domainId = request.getParameter("domainid");
        String mode = request.getParameter("mode");

        if (mode != null && mode.equalsIgnoreCase("NEW")) {

            request.setAttribute("mode", mode);

            session.removeAttribute("roleid");
            session.setAttribute("domainid", domainId);
        } else {

            if (roleId != null && domainId != null) {
                session.setAttribute("roleid", roleId);
                session.setAttribute("domainid", domainId);
            } else {
                roleId = (String) session.getAttribute("roleid");
                domainId = (String) session.getAttribute("domainid");

            }
        }

        List<Menu> level3MenuList = navigationDataService.menuGroupByUser(menuGroup, userId, "en").getMenuList();
        request.setAttribute("menuL3", level3MenuList);


        if (roleId != null) {
            role = roleDataService.getRole(domainId, roleId).getRole();
            roleCommand.setMode("UPDATE");

            roleCommand.setRole(role);

            Set<RoleAttribute> roleAttrSet = role.getRoleAttributes();
            attrSetToList(roleAttrSet, attrList);


        } else if (mode != null && mode.equalsIgnoreCase("NEW")) {

            role = new Role();
            RoleId id = new RoleId();
            id.setServiceId(domainId);
            role.setId(id);
            roleCommand.setMode("NEW");
            roleCommand.setRole(role);
        }
        addNewRowToAttributeList(attrList);

        roleCommand.setAttributeList(attrList);

        request.setAttribute("menuGroup", "SECURITY_ROLE");


        return roleCommand;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {


        RoleDetailCommand roleCommand = (RoleDetailCommand) command;

        String userId = (String) request.getSession().getAttribute("userId");
        String domainId = (String) request.getSession().getAttribute("domainid");
        String login = (String) request.getSession().getAttribute("login");

        Role role = roleCommand.getRole();
        prepareObject(role);

        String btn = request.getParameter("btn");

        if (btn != null && btn.equalsIgnoreCase("Delete")) {
            Response resp = roleDataService.removeRole(role.getId().getServiceId(),
                    role.getId().getRoleId());

            request.getSession().removeAttribute("roleid");
            request.getSession().removeAttribute("domainid");

            auditHelper.addLog("DELETE", domainId, login,
                    "WEBCONSOLE", userId, "0", "ROLE", role.getId().getRoleId(),
                    null, "SUCCESS", null, null,
                    null, null, null,
                    role.getId().getServiceId() + "-" + role.getId().getRoleId(), request.getRemoteHost());

            return new ModelAndView(new RedirectView("rolelist.cnt", true));


        }

        List<RoleAttribute> attributeList = roleCommand.getAttributeList();
        Set<RoleAttribute> attrSet = convertAttrListToSet(attributeList, role);
        role.setRoleAttributes(attrSet);

        RoleResponse resp =  roleDataService.getRole(role.getId().getServiceId(), role.getId().getRoleId());

        if (resp.getStatus() == ResponseStatus.SUCCESS) {
        //if (roleDataService.getRole(role.getId().getServiceId(), role.getId().getRoleId()) != null) {
            // update - role exists
            roleDataService.updateRole(role);

            if (roleCommand.getMode().equalsIgnoreCase("NEW")) {
                auditHelper.addLog("CREATE", domainId, login,
                        "WEBCONSOLE", userId, "0", "ROLE", role.getId().getRoleId(),
                        null, "SUCCESS", null, null,
                        null, null, null,
                        role.getId().getServiceId() + "-" + role.getId().getRoleId(), request.getRemoteHost());
            } else {

                auditHelper.addLog("MODIFY", domainId, login,
                        "WEBCONSOLE", userId, "0", "ROLE", role.getId().getRoleId(),
                        null, "SUCCESS", null, null,
                        null, null, null,
                        role.getId().getServiceId() + "-" + role.getId().getRoleId(), request.getRemoteHost());
            }
        } else {
            // new
            role.setCreateDate(new Date(System.currentTimeMillis()));
            role.setCreatedBy(userId);
            roleDataService.addRole(role);


            request.getSession().setAttribute("roleid", role.getId().getRoleId());
            request.getSession().setAttribute("domainid", domainId);

            auditHelper.addLog("CREATE", domainId, login,
                    "WEBCONSOLE", userId, "0", "ROLE", role.getId().getRoleId(),
                    null, "SUCCESS", null, null,
                    null, null, null,
                    role.getId().getServiceId() + "-" + role.getId().getRoleId(), request.getRemoteHost());

        }



        String url = redirectView + "&menuid=ROLE_SUMMARY&menugrp=SECURITY_ROLE&objId=null";

        log.info("Redirecting to: " + url);

        return new ModelAndView(new RedirectView(url, true));

    }

    private void prepareObject(Role r) {
        if (r.getMetadataTypeId().equals("")) {
            r.setMetadataTypeId(null);
        }
        if (r.getStatus().equals("")) {
            r.setStatus(null);
        }

    }

    Set<RoleAttribute> convertAttrListToSet(List<RoleAttribute> attributeList, Role role) {
        Set<RoleAttribute> attributeSet = new HashSet<RoleAttribute>();

        if (attributeList == null || attributeList.isEmpty()) {
            return null;
        }


        for (RoleAttribute atr : attributeList) {
            // new attr added
            if (atr.getRoleAttrId() == null || "NEW".equalsIgnoreCase(atr.getRoleAttrId())) {
                if (atr.getValue() != null &&
                        !atr.getValue().isEmpty() &&
                        !"**ENTER NAME**".equalsIgnoreCase(atr.getName())) {
                    atr.setRoleAttrId(null);
                    if (role.getId() != null) {
                        atr.setRoleId(role.getId().getRoleId());
                        atr.setServiceId(role.getId().getServiceId());
                    }
                    attributeSet.add(atr);
                }
            } else if (!"NEW".equalsIgnoreCase(atr.getRoleAttrId())) {
                if (atr.getName() == null || atr.getName().isEmpty()) {
                    roleDataService.removeAttribute(atr);
                } else {

                    attributeSet.add(atr);

                }
            }

        }

        return attributeSet;
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


    public AuditHelper getAuditHelper() {
        return auditHelper;
    }


    public void setAuditHelper(AuditHelper auditHelper) {
        this.auditHelper = auditHelper;
    }


    public MetadataWebService getMetadataService() {
        return metadataService;
    }


    public void setMetadataService(MetadataWebService metadataService) {
        this.metadataService = metadataService;
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


    public String getMenuGroup() {
        return menuGroup;
    }


    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }


}
