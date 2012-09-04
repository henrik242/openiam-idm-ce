package org.openiam.webconsole.web.controller.group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.webconsole.web.controller.BaseServiceController;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GroupServiceController extends BaseServiceController {
    @Autowired
    private GroupDataWebService groupManager;

    @Autowired
    protected GroupServiceController(Validator validator) {
        super(validator);
    }

    @RequestMapping(value = "group/list", method = RequestMethod.GET)
    public String getDashboard(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {

        model.addAttribute("searchResult", groupManager.getAllGroups()
                .getGroupList());
        return "group-list";
    }

    @RequestMapping(value = "group/apply-filter", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<List<Group>> validateLogin(HttpServletRequest request,
            HttpSession session, @RequestBody GroupSearch search) {
        CommonWebResponse<List<Group>> responseModel = new CommonWebResponse<List<Group>>();
        search.setGrpName(search.getGrpName() + "%");
        responseModel.setValue(groupManager.search(search).getGroupList());
        return responseModel;
    }
}
