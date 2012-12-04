package org.openiam.selfsrvc.wrkflow.terminate;

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
 * Class process either an approval or rejection when a request is made to terminate a user.
 * User: suneetshah
 */
public class CompleteTerminateRequest extends AbstractCompleteRequest {

    public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId ) {


        pUser.setStatus(UserStatusEnum.TERMINATE);
        ProvisionUserResponse resp =  provisionService.modifyUser(pUser);

        approve(resp.getUser(),req, approverUserId);

    }

    public void rejectRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId) {


        reject(pUser,req,approverUserId);


    }

    public void notifyRequestorApproval(ProvisionRequest req, String approverUserId, User newUser, String notifyUserId) {

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
        // send a message to this user
        request.setUserId(notifyUserId);
        request.setNotificationType("TERMINATE_REQUEST_APPROVED");

        request.getParamList().add(new NotificationParam("REQUEST_ID", req.getRequestId()));

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestTitle()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));


        mailService.sendNotification(request);
    }

    public void notifyRequestorReject(ProvisionRequest req, String approverUserId, String notifyUserId, String notifyEmail) {

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

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestTitle()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));

        mailService.sendNotification(request);


    }

}
