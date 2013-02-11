package org.openiam.selfsrvc.wrkflow.terminate;

import org.openiam.idm.srvc.msg.service.MailTemplateParameters;
import org.openiam.idm.srvc.msg.ws.NotificationRequest;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.selfsrvc.wrkflow.AbstractCompleteRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Class process either an approval or rejection when a request is made to terminate a user.
 * User: suneetshah
 */
public class CompleteUserStatusRequest extends AbstractCompleteRequest {

    public static final String TERMINATE_REQUEST_APPROVED_NOTIFICATION = "REQUEST_APPROVED";
    public static final String REQUEST_REJECTED_NOTIFICATION = "REQUEST_REJECTED";

    public void approveRequest(ProvisionUser pUser, ProvisionRequest req, String approverUserId ) {


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

        HashMap<String,String> mailParameters = new HashMap<String, String>();
        mailParameters.put(MailTemplateParameters.USER_ID.value(), notifyUserId);
        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), req.getRequestTitle());
        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), req.getRequestId());
        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), targetUserName);
        mailParameters.put(MailTemplateParameters.REQUESTER.value(), approver.getFirstName() + " " + approver.getLastName());

        User notifyUser = userManager.getUserWithDependent(notifyUserId,false).getUser();
        mailParameters.put(MailTemplateParameters.TO.value(), notifyUser.getEmail() );

        mailService.sendNotificationRequest(new NotificationRequest(TERMINATE_REQUEST_APPROVED_NOTIFICATION,mailParameters));
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

        HashMap<String,String> mailParameters = new HashMap<String, String>();
        mailParameters.put(MailTemplateParameters.USER_ID.value(), notifyUserId);
        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), req.getRequestTitle());
        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), req.getRequestId());
        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), targetUserName);
        mailParameters.put(MailTemplateParameters.REQUESTER.value(), approver.getFirstName() + " " + approver.getLastName());

        mailService.sendNotificationRequest(new NotificationRequest(REQUEST_REJECTED_NOTIFICATION,mailParameters));

    }

}
