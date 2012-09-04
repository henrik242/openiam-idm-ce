package org.openiam.webconsole.web.controller.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;
import org.openiam.idm.srvc.grp.ws.GroupAttrMapResponse;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.webconsole.web.controller.BaseServiceController;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.openiam.webconsole.web.model.ChoiceModel;
import org.openiam.webconsole.web.model.GroupDetailCommand;
import org.openiam.webconsole.web.model.GroupStatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    protected String groupTypeCategory = "GROUP_TYPE";

    @Autowired
    protected GroupDetailServiceController(Validator validator) {
        super(validator);
    }

    @RequestMapping(value = "group/edit", method = RequestMethod.GET)
    public String editGroupHandler(HttpSession session,
            HttpServletRequest request, HttpServletResponse response,
            Model model, @RequestParam("groupId") String groupId)
            throws Exception {
        processingGroupEditForm(session, request, response, model, groupId,
                null);
        return "group-detail";
    }

    @RequestMapping(value = "group/new", method = RequestMethod.GET)
    public String createNewGroup(
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            @RequestParam(value = "parentGroupId", required = false) String parentGroupId)
            throws Exception {
        processingGroupEditForm(session, request, response, model, null,
                parentGroupId);
        return "group-detail";
    }

    @RequestMapping(value = "group/save", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<String> validateLogin(HttpServletRequest request,
            HttpSession session, @RequestBody GroupDetailCommand groupCommand) {
        CommonWebResponse<String> responseModel = new CommonWebResponse<String>();
        return responseModel;
    }

    private void processingGroupEditForm(HttpSession session,
            HttpServletRequest request, HttpServletResponse response,
            Model model, String groupId, String parentGroupId) {
        Group group = null;
        GroupDetailCommand groupCommand = new GroupDetailCommand();
        List<GroupAttribute> attrList = new ArrayList<GroupAttribute>();

        groupCommand.setOrgList(orgDataService.getTopLevelOrganizations());
        groupCommand.setTypeList(metadataService.getTypesInCategory(
                groupTypeCategory).getMetadataTypeAry());

        // String mode = request.getParameter("mode");

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
        addNewRowToAttributeSet(attrList);
        groupCommand.setAttributeList(attrList);

        // get the list of child groups if any
        List<Group> childGroupList = groupManager.getChildGroups(
                group.getGrpId(), false).getGroupList();
        groupCommand.setChildGroup(childGroupList);

        // if (mode != null && "1".equals(mode)) {
        // request.setAttribute("mode", "1");
        // }
        List<GroupStatusModel> groupStatusList = Arrays.asList(
                new GroupStatusModel("ACTIVE"),
                new GroupStatusModel("DELETED"), new GroupStatusModel(
                        "PENDING_APPROVAL"));

        List<ChoiceModel> inheritFromParentList = Arrays.asList(
                new ChoiceModel("0", "NO"), new ChoiceModel("1", "YES"));

        model.addAttribute("groupStatusList", groupStatusList);
        model.addAttribute("inheritFromParentList", inheritFromParentList);
        model.addAttribute("groupModel", groupCommand);
        // return groupCommand;
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
}
