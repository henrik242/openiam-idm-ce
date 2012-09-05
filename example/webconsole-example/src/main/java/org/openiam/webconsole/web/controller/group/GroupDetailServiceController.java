package org.openiam.webconsole.web.controller.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;
import org.openiam.idm.srvc.grp.ws.GroupAttrMapResponse;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.grp.ws.GroupResponse;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.webconsole.config.ErrorCode;
import org.openiam.webconsole.web.constant.NotificationType;
import org.openiam.webconsole.web.controller.BaseServiceController;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.openiam.webconsole.web.model.ChoiceModel;
import org.openiam.webconsole.web.model.GroupDetailCommand;
import org.openiam.webconsole.web.model.GroupStatusModel;
import org.openiam.webconsole.web.model.UserSessionModel;
import org.openiam.webconsole.web.validator.GroupDetailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/05/12
 */
@Controller
public class GroupDetailServiceController extends BaseServiceController {
    @Autowired
    private GroupDataWebService groupManager;
    @Autowired
    private MetadataWebService metadataService;
    @Autowired
    private OrganizationDataService orgDataService;
    @Autowired
    private AuditHelper auditHelper;
    @Autowired
    private GroupDetailValidator groupDetailValidator;

    protected String groupTypeCategory = "GROUP_TYPE";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(groupDetailValidator);
    }

    @RequestMapping(value = "secure/group/edit", method = RequestMethod.GET)
    public String editGroupHandler(HttpSession session,
            UserSessionModel userSession, HttpServletRequest request,
            HttpServletResponse response, Model model,
            @RequestParam("groupId") String groupId) throws Exception {
        processingGroupEditForm(session, userSession, request, response, model,
                groupId, null);
        return "secure/group-detail";
    }

    @RequestMapping(value = "secure/group/new", method = RequestMethod.GET)
    public String createNewGroup(
            HttpSession session,
            UserSessionModel userSession,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            @RequestParam(value = "parentGroupId", required = false) String parentGroupId)
            throws Exception {
        processingGroupEditForm(session, userSession, request, response, model,
                null, parentGroupId);
        return "secure/group-detail";
    }

    @RequestMapping(value = "secure/group/delete/{groupId}", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<String> deleteGroup(HttpServletRequest request,
            HttpSession session, UserSessionModel userSession,
            @PathVariable String groupId) {
        CommonWebResponse<String> responseModel = new CommonWebResponse<String>();
        Response resp = groupManager.removeGroup(groupId);

        if (resp.getStatus() != ResponseStatus.SUCCESS) {
            responseModel.addNotification(NotificationType.error,
                    getMessage(request, ErrorCode.GENERIC_ERROR));
            return responseModel;
        }

        auditHelper.addLog("DELETE", userSession.getDomainId(),
                userSession.getLogin(), "WEBCONSOLE", userSession.getUserId(),
                "0", "GROUP", groupId, null, "SUCCESS", null, null, null, null,
                null, groupId, request.getRemoteHost());

        return responseModel;
    }

    @RequestMapping(value = "secure/group/save", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<String> saveGroup(HttpServletRequest request,
            HttpSession session, UserSessionModel userSession,
            @RequestBody @Valid GroupDetailCommand groupCommand) {
        CommonWebResponse<String> responseModel = new CommonWebResponse<String>();

        String grpId = null;

        Group group = groupCommand.getGroup();
        prepareObject(group);

        List<GroupAttribute> attributeList = groupCommand.getAttributeList();
        Map<String, GroupAttribute> attributeMap = convertAttrListToMap(
                attributeList, group);

        if (group.getGrpId() != null && group.getGrpId().length() > 0) {
            group.setLastUpdate(new Date(System.currentTimeMillis()));
            group.setLastUpdatedBy(userSession.getUserId());

            grpId = group.getGrpId();
            group.setAttributes(attributeMap);
            GroupResponse resp = groupManager.updateGroup(group);
            if (resp.getStatus() != ResponseStatus.SUCCESS) {
                responseModel.addNotification(NotificationType.error,
                        getMessage(request, ErrorCode.GENERIC_ERROR));
                return responseModel;
            }

            auditHelper.addLog("UPDATE", userSession.getDomainId(),
                    userSession.getLogin(), "WEBCONSOLE",
                    userSession.getUserId(), "0", "GROUP", group.getGrpId(),
                    null, "SUCCESS", null, null, null, null, null,
                    group.getGrpName(), request.getRemoteHost());
        } else {
            group.setGrpId(null);
            group.setCreatedBy(userSession.getUserId());
            group.setCreateDate(new Date(System.currentTimeMillis()));
            group.setAttributes(attributeMap);
            GroupResponse resp = groupManager.addGroup(group);

            if (resp.getStatus() != ResponseStatus.SUCCESS) {
                responseModel.addNotification(NotificationType.error,
                        getMessage(request, ErrorCode.GENERIC_ERROR));
                return responseModel;
            }
            grpId = resp.getGroup().getGrpId();
            auditHelper.addLog("CREATE", userSession.getDomainId(),
                    userSession.getLogin(), "WEBCONSOLE",
                    userSession.getUserId(), "0", "GROUP", grpId, null,
                    "SUCCESS", null, null, null, null, null,
                    group.getGrpName(), request.getRemoteHost());
        }

        return responseModel;
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

    private void processingGroupEditForm(HttpSession session,
            UserSessionModel userSession, HttpServletRequest request,
            HttpServletResponse response, Model model, String groupId,
            String parentGroupId) {
        Group group = null;
        GroupDetailCommand groupCommand = new GroupDetailCommand();
        List<GroupAttribute> attrList = new ArrayList<GroupAttribute>();

        groupCommand.setOrgList(orgDataService.getTopLevelOrganizations());
        groupCommand.setTypeList(metadataService.getTypesInCategory(
                groupTypeCategory).getMetadataTypeAry());

        if (groupId != null) {
            group = groupManager.getGroup(groupId).getGroup();
            GroupAttrMapResponse resp = groupManager.getAllAttributes(groupId);
            if (resp.getStatus() == ResponseStatus.SUCCESS) {
                attrMapToList(resp.getGroupAttrMap(), attrList);
            }
        } else {
            group = new Group();
            group.setParentGrpId(null);
        }
        if (parentGroupId != null && parentGroupId.length() > 0) {
            group.setParentGrpId(parentGroupId);
        }

        groupCommand.setGroup(group);
        groupCommand.setAttributeList(attrList);

        // get the list of child groups if any
        List<Group> childGroupList = groupManager.getChildGroups(
                group.getGrpId(), false).getGroupList();
        groupCommand.setChildGroup(childGroupList);

        List<GroupStatusModel> groupStatusList = Arrays.asList(
                new GroupStatusModel("ACTIVE"),
                new GroupStatusModel("DELETED"), new GroupStatusModel(
                        "PENDING_APPROVAL"));

        List<ChoiceModel> inheritFromParentList = Arrays.asList(
                new ChoiceModel("0", "NO"), new ChoiceModel("1", "YES"));

        model.addAttribute("groupStatusList", groupStatusList);
        model.addAttribute("inheritFromParentList", inheritFromParentList);
        model.addAttribute("groupModel", groupCommand);
    }

    private void attrMapToList(Map<String, GroupAttribute> attrMap,
            List<GroupAttribute> attrList) {
        if (attrMap == null || attrMap.isEmpty()) {
            return;
        }
        Set<String> keySet = attrMap.keySet();
        for (String s : keySet) {
            GroupAttribute attr = attrMap.get(s);
            if (attr != null) {
                attrList.add(attr);
            }
        }
    }

    Map<String, GroupAttribute> convertAttrListToMap(
            List<GroupAttribute> attributeList, Group group) {
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
                if (atr.getValue() != null && !atr.getValue().isEmpty()
                        && !"**ENTER NAME**".equalsIgnoreCase(atr.getName())) {
                    atr.setId(null);
                    atr.setGroupId(groupId);
                    attributeMap.put(atr.getName(), atr);
                }
            } else {
                if (atr.getName() == null || atr.getName().isEmpty()) {
                    groupManager.removeAttribute(atr);
                } else {
                    attributeMap.put(atr.getName(), atr);
                }
            }
        }
        return attributeMap;
    }
}
