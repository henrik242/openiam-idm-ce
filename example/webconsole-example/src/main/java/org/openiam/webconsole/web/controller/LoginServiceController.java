package org.openiam.webconsole.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.openiam.webconsole.web.model.LoginModel;
import org.openiam.webconsole.web.model.UserSessionModel;
import org.openiam.webconsole.web.validator.LoginValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/03/12
 */
@Controller
public class LoginServiceController extends BaseServiceController {

    @Autowired
    protected NavigatorDataWebService navigationDataService;
    @Autowired
    private LoginValidator loginValidator;

    private static final String rootMenu = "IDM";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(loginValidator);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getIndexPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        return getLoginPage(session, request, response, model);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpSession session,
            @Valid LoginModel loginModel, BindingResult result,
            RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "login";
        }
        UserSessionModel userSession = new UserSessionModel();
        userSession.setUserId(loginModel.getSubject().getUserId());
        userSession.setToken(loginModel.getSubject().getSsoToken().getToken());
        userSession.setLogin(loginModel.getSubject().getPrincipal().trim());
        userSession.setDomainId(loginModel.getDomainId());

        session.setAttribute(CommonWebConstant.userSession.name(), userSession);
        // get the menus that the user has permissions too
        List<Menu> menuList = navigationDataService.menuGroupByUser(rootMenu,
                loginModel.getSubject().getUserId(), "en").getMenuList();
        session.setAttribute("permissions", menuList);

        return "redirect:secure/dashboard";
    }

    @RequestMapping(value = "validateLogin", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<String> validateLogin(HttpServletRequest request,
            HttpSession session, @RequestBody @Valid LoginModel loginModel) {
        return new CommonWebResponse<String>();
    }

}
