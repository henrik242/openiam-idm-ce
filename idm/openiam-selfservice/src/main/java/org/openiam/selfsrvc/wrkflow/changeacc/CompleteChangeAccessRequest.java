package org.openiam.selfsrvc.wrkflow.changeacc;

import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.ws.LoginResponse;
import org.openiam.idm.srvc.msg.dto.NotificationParam;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.selfsrvc.wrkflow.AbstractCompleteRequest;

import java.util.Iterator;
import java.util.Set;

/**
 * Class that completes a request for self registration
 * Created with IntelliJ IDEA.
 * User: suneetshah
 * Date: 11/25/12
 * Time: 4:18 PM
 */
public class CompleteChangeAccessRequest extends AbstractCompleteRequest {

    public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId ) {

        ProvisionUserResponse resp = provisionService.modifyUser(pUser);

        approve(resp.getUser(),req, approverUserId);

    }

    public void rejectRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId) {

        reject(pUser,req, approverUserId);

    }

    public void notifyRequestorApproval(ProvisionRequest req, String approverUserId, User newUser, String notifyUserId) {

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

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestTitle()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));
        request.getParamList().add(new NotificationParam("IDENTITY", identity));
        request.getParamList().add(new NotificationParam("PSWD", password));


        mailService.sendNotification(request);
    }

    public void notifyRequestorReject(ProvisionRequest req, String approverUserId, String notifyUserId, String notifyEmail) {


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

        request.getParamList().add(new NotificationParam("REQUEST_REASON", req.getRequestTitle()));
        request.getParamList().add(new NotificationParam("REQUESTOR", approver.getFirstName() + " " + approver.getLastName()));
        request.getParamList().add(new NotificationParam("TARGET_USER", targetUserName));

        mailService.sendNotification(request);


    }
}
