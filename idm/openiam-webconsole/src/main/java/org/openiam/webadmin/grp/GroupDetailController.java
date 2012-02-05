package org.openiam.webadmin.grp;

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


import java.util.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;
import org.openiam.idm.srvc.grp.ws.GroupAttrMapResponse;
import org.openiam.idm.srvc.org.dto.OrganizationAttribute;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.webadmin.util.AuditHelper;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.grp.ws.GroupResponse;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;


public class GroupDetailController extends CancellableFormController {



	protected GroupDataWebService groupManager;
	protected MetadataWebService metadataService;
	protected OrganizationDataService orgDataService;
	protected String groupTypeCategory;
	protected String redirectView;
	protected AuditHelper auditHelper;
	
	
	private static final Log log = LogFactory.getLog(GroupDetailController.class);

	public GroupDetailController() {
		super();
	}

    @Override
       protected ModelAndView onCancel(Object command) throws Exception {
           return new ModelAndView(new RedirectView(getCancelView(),true));
       }


	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Group group = null;
		GroupDetailCommand groupCommand = new GroupDetailCommand();
        List<GroupAttribute> attrList = new ArrayList<GroupAttribute> ();
		
		groupCommand.setOrgList( orgDataService.getTopLevelOrganizations());
	  	groupCommand.setTypeList( metadataService.getTypesInCategory(groupTypeCategory).getMetadataTypeAry() );
			
		String groupId = request.getParameter("groupId");
		String parentGroupId = request.getParameter("parentGroupId");
        String mode = request.getParameter("mode");
		
		
		if ( groupId != null) {
			group = groupManager.getGroup(groupId).getGroup();
            GroupAttrMapResponse resp = groupManager.getAllAttributes(groupId);
            if ( resp.getStatus() == ResponseStatus.SUCCESS) {
               
                attrMapToList(resp.getGroupAttrMap(), attrList);
            }
            
		}else {
			group = new Group();
			group.setParentGrpId(null);
		}
		if (parentGroupId != null && parentGroupId.length() > 0) {
			group.setParentGrpId(parentGroupId);
		}

		
		groupCommand.setGroup(group);
        addNewRowToAttributeSet(attrList);
        groupCommand.setAttributeList(attrList);

		// get the list of child groups if any
		List<Group> childGroupList = groupManager.getChildGroups(group.getGrpId(),false).getGroupList();
		groupCommand.setChildGroup(childGroupList);

        if (mode != null && "1".equals(mode)) {
            request.setAttribute("mode","1");
        }
		
		return groupCommand;
	}


    private void addNewRowToAttributeSet(List<GroupAttribute> attrList) {


        if (attrList == null) {
            attrList = new ArrayList<GroupAttribute>();

        }

        GroupAttribute oa = new GroupAttribute();
        oa.setName("**ENTER NAME**");
        oa.setValue("");
        oa.setId("NEW");
        attrList.add(oa);

    }

    private void attrMapToList(Map<String, GroupAttribute> attrMap, List<GroupAttribute> attrList ) {

        if (attrMap == null ||  attrMap.isEmpty()) {
            return;
        }
        
        Set<String> keySet =  attrMap.keySet();
        for (String s : keySet) {
            GroupAttribute attr =  attrMap.get(s);
            if (attr != null) {
                attrList.add(attr);
            }

        }
        
        
    }

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

        String grpId = null;

		String userId = (String)request.getSession().getAttribute("userId");
		String domainId = (String)request.getSession().getAttribute("domainId");
		String login = (String)request.getSession().getAttribute("login");
		
		GroupDetailCommand groupCommand = (GroupDetailCommand)command;
		
		Group group = groupCommand.getGroup();
		prepareObject(group);
		
		String btn = request.getParameter("btn");

		if (btn != null && btn.equalsIgnoreCase("Delete")) {
			groupManager.removeGroup(group.getGrpId());

			auditHelper.addLog("DELETE", domainId,	login,
					"WEBCONSOLE", userId, "0", "GROUP", group.getGrpId(), 
					null,   "SUCCESS", null,  null, 
					null, null, null,
                    group.getGrpName(), request.getRemoteHost());
			
			ModelAndView mav = new ModelAndView("/deleteconfirm");
	        mav.addObject("msg", "Group has been successfully deleted.");
	        return mav;

		}
        
        List<GroupAttribute> attributeList = groupCommand.getAttributeList();
        Map<String, GroupAttribute> attributeMap = convertAttrListToMap(attributeList, group);
         

		if (group.getGrpId() != null && group.getGrpId().length() > 0) {
			group.setLastUpdate(new Date(System.currentTimeMillis()));
			group.setLastUpdatedBy(userId);

            grpId = group.getGrpId();
            group.setAttributes(attributeMap);

            groupManager.updateGroup(group);
			
			auditHelper.addLog("UPDATE", domainId,	login,
					"WEBCONSOLE", userId, "0", "GROUP", group.getGrpId(), 
					null,   "SUCCESS", null,  null, 
					null, null, null,
                    group.getGrpName(), request.getRemoteHost());
		
			
		}else {
			group.setGrpId(null);
			group.setCreatedBy(userId);
			group.setCreateDate(new Date(System.currentTimeMillis()));
            group.setAttributes(attributeMap);

            GroupResponse resp = groupManager.addGroup(group);


			if (resp.getStatus() == ResponseStatus.SUCCESS) {
				grpId = resp.getGroup().getGrpId();
			}

			auditHelper.addLog("CREATE", domainId,	login,
					"WEBCONSOLE", userId, "0", "GROUP", grpId, 
					null,   "SUCCESS", null,  null, 
					null, null, null,
                    group.getGrpName(), request.getRemoteHost());
			
		}
        String url =  redirectView + "&groupId=" + grpId;
		return new ModelAndView(new RedirectView(url, true));
		


		
	}

	private void prepareObject(Group group) {
		if (group.getMetadataTypeId().equals("")) {
			group.setMetadataTypeId(null);
		}
		if (group.getCompanyId().equals("")) {
			group.setCompanyId(null);
		}
		if (group.getParentGrpId().equals("")) {
			group.setParentGrpId(null);
		}
	}

    Map<String, GroupAttribute> convertAttrListToMap(List<GroupAttribute> attributeList, Group group) {
        Map<String, GroupAttribute> attributeMap = new HashMap<String, GroupAttribute>();
        
        if (attributeList == null || attributeList.isEmpty()) {
            return null;
        }
        
        String groupId = null;
       
        if (group.getGrpId() != null && !group.getGrpId().isEmpty()) {
            groupId = group.getGrpId();
        }
        
        for (GroupAttribute atr : attributeList) {
            // new attr added
            if (atr.getId() == null || "NEW".equalsIgnoreCase(atr.getId())) {
                if (atr.getValue() != null &&
                        !atr.getValue().isEmpty() &&
                        !"**ENTER NAME**".equalsIgnoreCase(atr.getName()) ) {
                    atr.setId(null);
                    atr.setGroupId(groupId);
                    attributeMap.put(atr.getName(), atr);
                }
            }else if (!"NEW".equalsIgnoreCase(atr.getId() )) {
                if (atr.getName() == null || atr.getName().isEmpty()){
                    groupManager.removeAttribute(atr);
                }else {

                    attributeMap.put(atr.getName(), atr);
                }
            }

        }
        
        return attributeMap;
    }


	public OrganizationDataService getOrgDataService() {
		return orgDataService;
	}

	public void setOrgDataService(OrganizationDataService orgDataService) {
		this.orgDataService = orgDataService;
	}

	public String getGroupTypeCategory() {
		return groupTypeCategory;
	}

	public void setGroupTypeCategory(String groupTypeCategory) {
		this.groupTypeCategory = groupTypeCategory;
	}

	public String getRedirectView() {
		return redirectView;
	}

	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}

	public GroupDataWebService getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupDataWebService groupManager) {
		this.groupManager = groupManager;
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


	

}
