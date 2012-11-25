package org.openiam.selfsrvc.wrkflow.selfreg;

import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.ws.LoginResponse;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.msg.dto.NotificationParam;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.selfsrvc.wrkflow.AbstractCompleteRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class that completes a request for self registration
 * Created with IntelliJ IDEA.
 * User: suneetshah
 * Date: 11/25/12
 * Time: 4:18 PM
 */
public class CompleteSelfRegistrationRequest extends AbstractCompleteRequest {

    public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId ) {


        pUser.getUser().setStatus(UserStatusEnum.ACTIVE);
        pUser.getUser().setUserId(null);
        pUser.setStatus(UserStatusEnum.ACTIVE);
        ProvisionUserResponse resp = provisionService.addUser(pUser);

        User newUser = resp.getUser();

        log.info("New User userId = " + newUser.getUserId());

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
                        notifyUserId = newUser.getUserId();
                    } else {
                        notifyUserId = null;
                    }

                }
            }

            if (notifyUserId != null) {
                notifyRequestorApproval(req, approverUserId, newUser, notifyUserId);
            } else {
                log.info("Unable to determine userId to notify");
            }
        }


    }

    public void rejectRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId) {

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

    private void notifyRequestorApproval(ProvisionRequest req, String approverUserId, User newUser, String notifyUserId) {

        // requestor information
        String userId = req.getRequestorId();
        String identity = null;
        String password = null;


        User approver = userManager.getUserWithDependent(approverUserId, false).getUser();

        // get the target user
        String targetUserName = null;
        Set<RequestUser> reqUserSet = req.getRequestUsers();
        if (reqUserSet != null && !reqUserSet.isEmpty()) {
            Iterator<RequestUser> userIt = reqUserSet.iterator();
            if (userIt.hasNext()) {
                RequestUser targetUser = userIt.next();
                targetUserName = targetUser.getFirstName() + " " + targetUser.getLastName();
            }
        }

        LoginResponse lgResponse = loginManager.getPrimaryIdentity(newUser.getUserId());
        if (lgResponse.getStatus() == ResponseStatus.SUCCESS) {
            Login l = lgResponse.getPrincipal();
            identity = l.getId().getLogin();
            password = (String) loginManager.decryptPassword(l.getPassword()).getResponseValue();
        }


        NotificationRequest request = new NotificationRequest();
        // send a message to this user
        request.setUserId(notifyUserId);
        request.setNotificationType("REQUEST_APPROVED");

        request.getParamList().add(new NotificationParam("REQUEST_ID", req.getRequestId()));

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestReason()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));
        request.getParamList().add(new NotificationParam("IDENTITY", identity));
        request.getParamList().add(new NotificationParam("PSWD", password));


        mailService.sendNotification(request);
    }

    private void notifyRequestorReject(ProvisionRequest req, String approverUserId, String notifyUserId, String notifyEmail) {

        System.out.println("notifyRequestorReject() called");

        String userId = req.getRequestorId();

        User approver = userManager.getUserWithDependent(approverUserId, false).getUser();

        // get the target user
        String targetUserName = null;
        Set<RequestUser> reqUserSet = req.getRequestUsers();
        if (reqUserSet != null && !reqUserSet.isEmpty()) {
            Iterator<RequestUser> userIt = reqUserSet.iterator();
            if (userIt.hasNext()) {
                RequestUser targetUser = userIt.next();
                targetUserName = targetUser.getFirstName() + " " + targetUser.getLastName();

            }

        }

        NotificationRequest request = new NotificationRequest();
        request.setUserId(notifyUserId);
        request.setNotificationType("REQUEST_REJECTED");
        request.setTo(notifyEmail);

        request.getParamList().add(new NotificationParam("REQUEST_ID", req.getRequestId()));

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestReason()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));

        System.out.println("Sending notification for that request was rejected " + req.getRequestId());

        mailService.sendNotification(request);


    }
}
