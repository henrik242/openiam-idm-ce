package org.openiam.webadmin.admin.mailtmpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.service.MailTemplateService;
import org.openiam.idm.srvc.msg.ws.MailTemplateWebService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MailTemplateListController extends SimpleFormController {
    private static final Log log = LogFactory.getLog(MailTemplateListController.class);
    private MailTemplateWebService mailTemplateWebService;


    public MailTemplateListController() {
        super();
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        if (!request.getParameterMap().containsKey("selectedTemplate.tmplId")) {
            List<MailTemplateDto> mailTemplateList = mailTemplateWebService.getAllTemplates().getMailTemplateList();
            MailTemplateListCommand mailTemplateListCommand = new MailTemplateListCommand();
            mailTemplateListCommand.setTemplateList(mailTemplateList);
            return mailTemplateListCommand;
        }
        return super.formBackingObject(request);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors)
            throws Exception {
        MailTemplateListCommand mailTemplateListCommand = (MailTemplateListCommand) command;
        MailTemplateCommand templateCommand = new MailTemplateCommand();
        if (request.getParameterMap().containsKey("edit_btn")) {
            templateCommand.setTemplate(mailTemplateWebService.getTemplateById(mailTemplateListCommand.getSelectedTemplate().getTmplId()).getMailTemplate());
            return new ModelAndView(getSuccessView(), "mailTmplCmd", templateCommand);
        } else if (request.getParameterMap().containsKey("add_btn")) {
            return new ModelAndView(getSuccessView(), "mailTmplCmd", templateCommand);
        }
        return null;
    }


    public MailTemplateWebService getMailTemplateWebService() {
        return mailTemplateWebService;
    }

    public void setMailTemplateWebService(MailTemplateWebService mailTemplateWebService) {
        this.mailTemplateWebService = mailTemplateWebService;
    }
}
