package org.openiam.selfsrvc.wrkflow.terminate;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.selfsrvc.wrkflow.AbstractWorkflowRequest;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;

/**
 * Controller to terminate a user.
 * CE Version - simulate workflow only. Use EE version for real workflow integration
 *
 * @author suneet
 */
public class TerminateUserController extends AbstractWorkflowRequest {

    private static final Log log = LogFactory.getLog(TerminateUserController.class);
    private String view;
    private String viewErr;



    public TerminateUserController() {
        super();
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest req,
                                                 HttpServletResponse res) throws Exception {


        log.debug("ViewUserController: handleRequestInternal called.");

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) req.getSession().getAttribute("wrkflowRequest");

        if (wrkFlowRequest.getPersonId() == null || wrkFlowRequest.getPersonId().isEmpty()) {
            ModelAndView mav = new ModelAndView(viewErr);
            return mav;
        }

        ProvisionRequest provReq = createRequest(wrkFlowRequest);

        provRequestService.addRequest(provReq);



        ModelAndView mav = new ModelAndView(view);
        mav.addObject("name", getUserName(provReq) );

        return mav;

    }


    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getViewErr() {
        return viewErr;
    }

    public void setViewErr(String viewErr) {
        this.viewErr = viewErr;
    }
}
