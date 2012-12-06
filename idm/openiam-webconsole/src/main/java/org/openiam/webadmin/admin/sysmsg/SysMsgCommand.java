package org.openiam.webadmin.admin.sysmsg;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Command object for the SysMsgDetailController
 * @author suneet
 *
 */
public class SysMsgCommand implements Serializable {

    private static final long serialVersionUID = -2564887130992994285L;
    private NotificationDto msg = new NotificationDto();
    private MultipartFile providerScriptFile;
    private String selectedTemplateId = "";
    private Integer templateMethod;
    private List<MailTemplateDto> mailTemplates = Collections.EMPTY_LIST;

	public SysMsgCommand() {
    }

	public NotificationDto getMsg() {
		return msg;
	}

	public void setMsg(NotificationDto msg) {
		this.msg = msg;
        if(msg.getMsgId() != null) {
            if(msg.getMailTemplate() != null){
                selectedTemplateId = msg.getMailTemplate().getTmplId();
                templateMethod = 1;
            } else if(StringUtils.isNotEmpty(msg.getProviderScriptName())) {
                templateMethod = 0;
            }
        }
	}

    public MultipartFile getProviderScriptFile() {
        return providerScriptFile;
    }

    public void setProviderScriptFile(MultipartFile providerScriptFile) {
        this.providerScriptFile = providerScriptFile;
    }

    public String getSelectedTemplateId() {
        return selectedTemplateId;
    }

    public void setSelectedTemplateId(String selectedTemplateId) {
        this.selectedTemplateId = selectedTemplateId;
    }

    public Integer getTemplateMethod() {
        return templateMethod;
    }

    public void setTemplateMethod(Integer templateMethod) {
        this.templateMethod = templateMethod;
    }

    public List<MailTemplateDto> getMailTemplates() {
        return mailTemplates;
    }

    public void setMailTemplates(List<MailTemplateDto> mailTemplates) {
        this.mailTemplates = mailTemplates;
    }
}
