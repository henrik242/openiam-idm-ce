package org.openiam.webadmin.admin.sysmsg;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.dto.NotificationType;
import org.openiam.idm.srvc.msg.ws.MailTemplateWebService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

//import org.openiam.idm.srvc.audit.service.AuditLogUtil;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.msg.ws.SysMessageWebService;

public class SysMsgDetailController extends CancellableFormController {

    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");

    private static final Log log = LogFactory.getLog(SysMsgDetailController.class);
    private SysMessageWebService sysMessageService;
    private String redirectView;

    private MailTemplateWebService mailTemplateWebService;

    public SysMsgDetailController() {
        super();
    }

    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        //Map map = new HashMap();
        //List<MailTemplateDto> templateList = mailTemplateWebService.getAllTemplates().getMailTemplateList();
        //map.put("mailTemplates", templateList);
        return super.referenceData(request,command,errors);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        SysMsgCommand msgCmd = (SysMsgCommand) command;
        log.info("SysMsgDetailController:onSubmit called.");


        NotificationDto sysMessage = msgCmd.getMsg();
        String btn = request.getParameter("btn");
        if ("delete".equalsIgnoreCase(btn)) {
            if(sysMessage.getType() == NotificationType.SYSTEM) {
                return null;
            }
            sysMessageService.removeMessage(sysMessage.getMsgId());
            ModelAndView mav = new ModelAndView("/deleteconfirm");
            mav.addObject("msg", "Location has been successfully deleted.");
            return mav;
        } else if ("save".equalsIgnoreCase(btn)) {
            if (msgCmd.getTemplateMethod() == 0) {
                MultipartFile providerScriptFile = msgCmd.getProviderScriptFile();
                String providerScriptFileName = "";
                if (providerScriptFile != null && providerScriptFile.getSize() > 0) {
                    providerScriptFileName = providerScriptFile.getOriginalFilename();
                    String filePath = res.getString("scriptRoot") + "/msgprovider/" + providerScriptFileName;
                    File dest = new File(filePath);
                    try {
                        providerScriptFile.transferTo(dest);
                        sysMessage.setProviderScriptName(providerScriptFileName);
                        sysMessage.setMailTemplate(null);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                MailTemplateDto mailTemplateDto = mailTemplateWebService.getTemplateById(msgCmd.getSelectedTemplateId()).getMailTemplate();
                sysMessage.setMailTemplate(mailTemplateDto);
                sysMessage.setProviderScriptName(null);
            }

            if (StringUtils.isEmpty(sysMessage.getMsgId())) {
                    sysMessageService.addMessage(sysMessage);
                } else {
                    sysMessageService.updateMessage(sysMessage);
                }
        }

        ModelAndView mav = new ModelAndView(new RedirectView(redirectView, true));

        return mav;
    }

    private Login getPrimaryLogin(List<Login> principalList) {
        if (principalList == null) {
            return null;
        }
        for (Login lg : principalList) {
            if (lg.getId().getManagedSysId().equalsIgnoreCase("0")) {
                return lg;
            }
        }
        return null;
    }

    public String getRedirectView() {
        return redirectView;
    }

    public void setRedirectView(String redirectView) {
        this.redirectView = redirectView;
    }

    public SysMessageWebService getSysMessageService() {
        return sysMessageService;
    }

    public void setSysMessageService(SysMessageWebService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }

    public MailTemplateWebService getMailTemplateWebService() {
        return mailTemplateWebService;
    }

    public void setMailTemplateWebService(MailTemplateWebService mailTemplateWebService) {
        this.mailTemplateWebService = mailTemplateWebService;
    }
}
