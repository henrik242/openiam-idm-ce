package org.openiam.webadmin.admin.mailtmpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.ws.MailTemplateWebService;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class MailTemplateController extends SimpleFormController {
    private static final Log log = LogFactory.getLog(MailTemplateController.class);

    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");
    private MailTemplateWebService mailTemplateWebService;
    private String redirectView;

    public MailTemplateController() {
        super();
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return super.formBackingObject(request);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        MailTemplateCommand mailTmplCmd = (MailTemplateCommand) command;
        log.info("MailTemplateController:onSubmit called.");


        MailTemplateDto mailTemplate = mailTmplCmd.getTemplate();
        String btn = request.getParameter("btn");
        if ("delete".equalsIgnoreCase(btn)) {
            mailTemplateWebService.removeTemplate(mailTemplate.getTmplId());
            ModelAndView mav = new ModelAndView("/deleteconfirm");
            mav.addObject("msg", "Location has been successfully deleted.");
            return mav;
        } else if ("submit".equalsIgnoreCase(btn)) {
            MultipartFile attachedFile = mailTmplCmd.getAttachmentFile();
            String attachedFileFileName = "";

            if (attachedFile != null && attachedFile.getSize() > 0) {
                attachedFileFileName = attachedFile.getOriginalFilename().replaceAll(" ","_");
                String filePath = res.getString("uploadDir") + File.separatorChar + attachedFileFileName;
                mailTemplate.setAttachmentFilePath(attachedFileFileName);
                File dest = new File(filePath);
                try {
                    attachedFile.transferTo(dest);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (StringUtils.isEmpty(mailTemplate.getTmplId())) {
                mailTemplate = mailTemplateWebService.addTemplate(mailTemplate).getMailTemplate();
            } else {
                mailTemplate = mailTemplateWebService.updateTemplate(mailTemplate).getMailTemplate();
            }
        }

        ModelAndView mav = new ModelAndView(new RedirectView(redirectView, true));
        mav.addObject("mailTmplCmd", mailTmplCmd);

        return mav;
    }

    public MailTemplateWebService getMailTemplateWebService() {
        return mailTemplateWebService;
    }

    public void setMailTemplateWebService(MailTemplateWebService mailTemplateWebService) {
        this.mailTemplateWebService = mailTemplateWebService;
    }

    public String getRedirectView() {
        return redirectView;
    }

    public void setRedirectView(String redirectView) {
        this.redirectView = redirectView;
    }
}
