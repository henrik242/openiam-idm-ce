package org.openiam.idm.srvc.msg.ws;

import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "urn:idm.openiam.org/srvc/msg/service", name = "MailTemplateWebService")
public interface MailTemplateWebService {
    @WebMethod
    public MailTemplateResponse addTemplate(
            @WebParam(name = "template", targetNamespace = "")
            MailTemplateDto template);

    @WebMethod
    public Response removeTemplate(
            @WebParam(name = "tmplId", targetNamespace = "")
            String tmplId);

    @WebMethod
    public MailTemplateResponse updateTemplate(
            @WebParam(name = "template", targetNamespace = "")
            MailTemplateDto template);

    @WebMethod
    public MailTemplateResponse getTemplateById(
            @WebParam(name = "id", targetNamespace = "")
            java.lang.String id);


    @WebMethod
    public MailTemplateListResponse getAllTemplates();

}
