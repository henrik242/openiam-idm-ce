package org.openiam.webconsole.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.Validator;

import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.openiam.webconsole.web.model.LoginModel;
import org.openiam.webconsole.web.validator.LoginValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    protected LoginServiceController(Validator validator) {
        super(validator);
    }

    @ModelAttribute("loginModel")
    public LoginModel getModel() {
        return new LoginModel();
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpSession session,
            @Valid LoginModel loginModel, BindingResult result,
            RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "index";
        }
        session.setAttribute("userId", loginModel.getSubject().getUserId());
        session.setAttribute("token", loginModel.getSubject().getSsoToken()
                .getToken());
        session.setAttribute("login", loginModel.getSubject().getPrincipal()
                .trim());
        session.setAttribute("domainId", loginModel.getDomainId());

        // get the menus that the user has permissions too
        List<Menu> menuList = navigationDataService.menuGroupByUser(rootMenu,
                loginModel.getSubject().getUserId(), "en").getMenuList();
        session.setAttribute("permissions", menuList);

        // load the objects that are needed in the primary application

        /*
         * ModelAndView mav = new ModelAndView(getSuccessView());
         * mav.addObject("loginCmd", command); mav.addObject("subject",
         * loginCmd.getSubject());
         */
        return "redirect:dashboard";
    }

    @RequestMapping(value = "validateLogin", method = RequestMethod.POST)
    public @ResponseBody
    CommonWebResponse<String> validateLogin(HttpServletRequest request,
            HttpSession session, @RequestBody @Valid LoginModel loginModel) {
        return new CommonWebResponse<String>();
    }

}
