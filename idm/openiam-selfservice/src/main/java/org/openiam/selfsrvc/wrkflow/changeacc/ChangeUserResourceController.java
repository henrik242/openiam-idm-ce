package org.openiam.selfsrvc.wrkflow.changeacc;


import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.selfsrvc.wrkflow.AbstractFormWorkflowController;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.provision.dto.UserResourceAssociation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class ChangeUserResourceController extends AbstractFormWorkflowController {


    public ChangeUserResourceController() {
        super();
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        return loadResourceInformation(request);

    }

    protected Map loadResourceInformation(HttpServletRequest request) {

        List<Resource> resourceList = resourceDataService.getResourcesByType("MANAGED_SYS");

        Map model = new HashMap();

        model.put("resourceList", resourceList);


        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserResourceCommand cmd = new ChangeUserResourceCommand();


        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");
        if (wrkFlowRequest != null) {

            String personId = wrkFlowRequest.getPersonId();
            cmd.setSelectedUser(userManager.getUserWithDependent(personId, false).getUser());

        }

        return cmd;


    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {


        ChangeUserResourceCommand identityCmd = (ChangeUserResourceCommand) command;

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");


        ProvisionRequest provReq = createRequest(wrkFlowRequest,identityCmd);

        provRequestService.addRequest(provReq);


        // implementation pending

        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String) request.getSession().getAttribute("login"), "CHANGE_APPLICATION_WORKFLOW",
                ProvisionRequest.CHANGE_APPLICATION_WORKFLOW, provReq.getRequestTitle(), null,
                null, null, (String) request.getSession().getAttribute("domain"), provReq.getRequestorId());

        auditLog.setRequestId(provReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditService.createLog(auditLog);

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("name", getUserName(provReq));
        return mav;


    }

    protected ProvisionRequest createRequest(WorkflowRequest wrkFlowRequest, ChangeUserResourceCommand cmd) {

        boolean addOperation = false;

        if ("ADD".equalsIgnoreCase(cmd.getOperation())) {
            addOperation = true;

        }

        ProvisionUser pUser = buildUserObject(wrkFlowRequest);

        if ( cmd.getResourceId() != null && !cmd.getResourceId().isEmpty()) {

            UserResourceAssociation ura = new UserResourceAssociation();
            ura.setResourceId(cmd.getResourceId());
            if (addOperation) {
                ura.setOperation(AttributeOperationEnum.ADD);
            }else {
                ura.setOperation(AttributeOperationEnum.DELETE);
            }
            Resource res = resourceDataService.getResource(cmd.getResourceId());

            ura.setManagedSystemId(res.getManagedSysId());
            ura.setResourceName(res.getName());

            List<UserResourceAssociation> uraList = new ArrayList<UserResourceAssociation>();
            uraList.add(ura);

            pUser.setUserResourceList(uraList);
        }



        ProvisionRequest pReq = buildRequest(wrkFlowRequest, pUser);

        wrkFlowRequest.getWorkflowResId();

        RequestApprover reqApprover = getApprover(wrkFlowRequest.getWorkflowResId(), pUser);
        pReq.getRequestApprovers().add(reqApprover);

        notifyApprover(pReq, pUser);

        return pReq;

    }


}


