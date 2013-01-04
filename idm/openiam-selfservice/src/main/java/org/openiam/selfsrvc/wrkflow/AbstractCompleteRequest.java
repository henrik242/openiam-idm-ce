package org.openiam.selfsrvc.wrkflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

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

    public void approve(ProvisionUser pUser, ProvisionRequest req, String approverUserId ) {


       String requestType = req.getRequestType();

        List<ApproverAssociation> apList = managedSysService.getApproverByRequestType(requestType, 1);

        for (ApproverAssociation ap : apList) {
            String typeOfUserToNotify = ap.getApproveNotificationUserType();
            if (typeOfUserToNotify == null || typeOfUserToNotify.length() == 0) {
                typeOfUserToNotify = "USER";
            }
            String notifyUserId = null;
            if (typeOfUserToNotify.equalsIgnoreCase("USER")) {
                notifyUserId = ap.getNotifyUserOnApprove();
            } else {
                if (typeOfUserToNotify.equalsIgnoreCase("SUPERVISOR")) {
                    Supervisor supVisor = pUser.getSupervisor();
                    if (supVisor != null) {
                        notifyUserId = supVisor.getSupervisor().getUserId();
                    } else {
                        notifyUserId = null;
                    }

                } else {
                    // target user
                    if (pUser.getEmailAddresses() != null) {
                        notifyUserId = pUser.getUserId();
                    } else {
                        notifyUserId = null;
                    }

                }
            }

            if (notifyUserId != null) {
                notifyRequestorApproval(req, approverUserId, pUser, notifyUserId);
            } else {
                log.info("Unable to determine userId to notify");
            }
        }


    }

    public void reject(ProvisionUser pUser, ProvisionRequest req, String approverUserId) {

        String requestType = req.getRequestType();
        String notifyEmail = null;


        List<ApproverAssociation> apList = managedSysService.getApproverByRequestType(requestType, 1);
        //String notifyUserId = ap.getNotifyUserOnReject();

        for (ApproverAssociation ap : apList) {
            String typeOfUserToNotify = ap.getRejectNotificationUserType();
            if (typeOfUserToNotify == null || typeOfUserToNotify.length() == 0) {
                typeOfUserToNotify = "USER";
            }
            String notifyUserId = null;
            if (typeOfUserToNotify.equalsIgnoreCase("USER")) {
                notifyUserId = ap.getNotifyUserOnReject();
            } else {
                if (typeOfUserToNotify.equalsIgnoreCase("SUPERVISOR")) {
                    Supervisor supVisor = pUser.getSupervisor();
                    if (supVisor != null) {
                        notifyUserId = supVisor.getSupervisor().getUserId();
                    } else {
                        notifyUserId = null;
                    }

                } else {
                    // target user
                    if (pUser.getEmailAddresses() != null) {
                        // user does not exist. We cant use their userId.
                        notifyUserId = null;
                        notifyEmail = pUser.getEmail();
                    } else {
                        notifyUserId = null;
                    }

                }
            }


            notifyRequestorReject(req, approverUserId, notifyUserId, notifyEmail);

        }
    }

    public User getNotificationUser(String notifyUserId) {

       return userManager.getUserWithDependent(notifyUserId,false).getUser();

    }

    abstract public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId );
    abstract public void rejectRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId);

    abstract public void notifyRequestorApproval(ProvisionRequest req, String approverUserId, User newUser, String notifyUserId);
    abstract public void notifyRequestorReject(ProvisionRequest req, String approverUserId, String notifyUserId, String notifyEmail);



}
