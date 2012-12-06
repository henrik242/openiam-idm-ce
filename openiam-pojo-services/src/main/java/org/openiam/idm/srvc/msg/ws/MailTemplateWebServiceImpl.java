package org.openiam.idm.srvc.msg.ws;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.msg.dto.MailTemplateDto;
import org.openiam.idm.srvc.msg.service.MailTemplateService;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "org.openiam.idm.srvc.msg.ws.MailTemplateWebService",
        targetNamespace = "urn:idm.openiam.org/srvc/msg/service",
        portName = "MailTemplatePort",
        serviceName = "MailTemplateWebService")
public class MailTemplateWebServiceImpl implements MailTemplateWebService {

    private MailTemplateService tmplService;

    @Override
    public MailTemplateResponse addTemplate(@WebParam(name = "template", targetNamespace = "") MailTemplateDto template) {
        MailTemplateResponse resp = new MailTemplateResponse(ResponseStatus.SUCCESS);
        MailTemplateDto newTmpl = tmplService.addTemplate(template);
        if (newTmpl.getTmplId() == null || newTmpl.getTmplId().isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
        }else {
            resp.setMailTemplate(newTmpl);
        }
        return resp;
    }

    @Override
    public Response removeTemplate(@WebParam(name = "tmplId", targetNamespace = "") String tmplId) {
        Response resp = new Response(ResponseStatus.SUCCESS);
        tmplService.removeTemplate(tmplId);
        return resp;
    }

    @Override
    public MailTemplateResponse updateTemplate(@WebParam(name = "template", targetNamespace = "") MailTemplateDto template) {
        MailTemplateResponse resp = new MailTemplateResponse(ResponseStatus.SUCCESS);
        MailTemplateDto mailTemplate = tmplService.updateTemplate(template);
        if (mailTemplate.getTmplId() == null || mailTemplate.getTmplId().isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
        }else {
            resp.setMailTemplate(mailTemplate);
        }
        return resp;
    }

    @Override
    public MailTemplateResponse getTemplateById(@WebParam(name = "id", targetNamespace = "") String id) {
        MailTemplateResponse resp = new MailTemplateResponse(ResponseStatus.SUCCESS);
        MailTemplateDto mailTemplateDto = tmplService.getTemplateById(id);
        if (mailTemplateDto.getTmplId() == null || mailTemplateDto.getTmplId().isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
        }else {
            resp.setMailTemplate(mailTemplateDto);
        }
        return resp;
    }

    @Override
    public MailTemplateListResponse getAllTemplates() {
        MailTemplateListResponse resp = new MailTemplateListResponse(ResponseStatus.SUCCESS);
        List<MailTemplateDto> allTemplates = tmplService.getAllTemplates();
        if (allTemplates == null || allTemplates.isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
        }else {
            resp.setMailTemplateList(allTemplates);
        }
        return resp;
    }

    public MailTemplateService getTmplService() {
        return tmplService;
    }

    public void setTmplService(MailTemplateService tmplService) {
        this.tmplService = tmplService;
    }
}
