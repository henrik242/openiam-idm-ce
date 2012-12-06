package org.openiam.idm.srvc.msg.ws;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailTemplateDeliveryListResponse", propOrder = {
        "mailTemplateList"
})
public class MailTemplateListResponse extends Response {

    private static final long serialVersionUID = 3971523111562402476L;

    private List<MailTemplateDto> mailTemplateList;

    public MailTemplateListResponse() {
    }

    public MailTemplateListResponse(ResponseStatus s) {
        super(s);
    }

    public List<MailTemplateDto> getMailTemplateList() {
        return mailTemplateList;
    }

    public void setMailTemplateList(List<MailTemplateDto> mailTemplateList) {
        this.mailTemplateList = mailTemplateList;
    }
}
