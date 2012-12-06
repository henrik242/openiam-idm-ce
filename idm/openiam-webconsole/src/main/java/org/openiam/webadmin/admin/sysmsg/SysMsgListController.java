package org.openiam.webadmin.admin.sysmsg;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.ws.MailTemplateWebService;
import org.openiam.idm.srvc.msg.ws.SysMessageWebService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


public class SysMsgListController extends SimpleFormController {

    private static final Log log = LogFactory.getLog(SysMsgListController.class);

    private SysMessageWebService sysMessageService;
    private MailTemplateWebService mailTemplateWebService;

    public SysMsgListController() {
        super();
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        if (!request.getParameterMap().containsKey("selectedMsg.msgId")) {
            List<NotificationDto> sysMessageList = sysMessageService.getAllMessages().getSysMessageList();
            SysMsgListCommand sysMsgListCommand = new SysMsgListCommand();
            sysMsgListCommand.setMsgList(sysMessageList);
            return sysMsgListCommand;
        }
        return super.formBackingObject(request);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors)
            throws Exception {
        SysMsgListCommand sysMsgListCommand = (SysMsgListCommand) command;
        SysMsgCommand sysMsgCommand = new SysMsgCommand();
        ModelAndView modelAndView = null;
        if (request.getParameterMap().containsKey("edit_btn")) {
            sysMsgCommand.setMsg(sysMessageService.getMessageById(sysMsgListCommand.getSelectedMsg().getMsgId()).getSysMessage());
            modelAndView = new ModelAndView(getSuccessView(), "sysMsgCmd", sysMsgCommand);
        } else if (request.getParameterMap().containsKey("add_btn")) {
            modelAndView = new ModelAndView(getSuccessView(), "sysMsgCmd", sysMsgCommand);
        }
        if(modelAndView != null) {
            List<MailTemplateDto> templateList = mailTemplateWebService.getAllTemplates().getMailTemplateList();
            modelAndView.addObject("mailTemplates", templateList);
        }
        return modelAndView;
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
