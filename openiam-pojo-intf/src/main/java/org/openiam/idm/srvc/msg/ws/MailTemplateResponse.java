package org.openiam.idm.srvc.msg.ws;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailTemplateDeliveryResponse", propOrder = {
        "mailTemplate"
})
public class MailTemplateResponse extends Response {

    private static final long serialVersionUID = 2233469507991704513L;

    private MailTemplateDto mailTemplate;

    public MailTemplateResponse() {
    }

    public MailTemplateResponse(ResponseStatus s) {
        super(s);
    }

    public MailTemplateDto getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplateDto mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

}
