package org.openiam.selfsrvc.hire;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/newhire")
public class NewHireExtController {

    private static final Log LOG = LogFactory.getLog(NewHireExtController.class);
    public static final String USER_CATEGORY_TYPE = "USER_TYPE";

    @Autowired
    private MetadataWebService metadataServiceClient;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/seltype")
    public String getConfirmForm(Model model, HttpServletRequest request) {
        LOG.info("New hire select type of user view form called.");

        MetadataType[] typeAry = metadataServiceClient.getTypesInCategory(USER_CATEGORY_TYPE).getMetadataTypeAry();
        model.addAttribute("metadataTypeAry", typeAry);
        model.addAttribute("newHireCommand", new NewHireExtCommand());

        return "/priv/newhire/selusertype";
    }

}
