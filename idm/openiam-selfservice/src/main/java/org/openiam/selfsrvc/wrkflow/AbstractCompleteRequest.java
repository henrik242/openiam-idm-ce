package org.openiam.selfsrvc.wrkflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Base class to approve or reject a request
 * User: suneetshah
 * Date: 11/25/12
 * Time: 4:17 PM
 */
public abstract class AbstractCompleteRequest  {

    protected ProvisionService provisionService;
    protected MailService mailService;
    protected ManagedSystemDataService managedSysService;
    protected UserDataWebService userManager;
    protected LoginDataWebService loginManager;


    protected static final Log log = LogFactory.getLog(AbstractCompleteRequest.class);

    public void init(ApplicationContext ac) {

        mailService = (MailService)ac.getBean("mailServiceClient");
        managedSysService = (ManagedSystemDataService) ac.getBean("managedSysServiceClient");
        provisionService = (ProvisionService)ac.getBean("provisionServiceClient");
        userManager = (UserDataWebService)ac.getBean("userServiceClient");
        loginManager = (LoginDataWebService) ac.getBean("loginServiceClient");


    }

    abstract public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId );
    abstract public void rejectRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId);


}
