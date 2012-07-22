package org.openiam.provision.service;

import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.connector.type.UserResponse;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that implements functionality required for provisioning a selected set of resources.
 */
public class DeprovisionSelectedResourceHelper extends BaseProvisioningHelper {

    public ProvisionUserResponse deprovisionSelectedResources( String userId, String requestorUserId, List<String> resourceList)  {

        log.debug("deprovisionSelectedResources().....for userId=" + userId);

        IdmAuditLog auditLog = null;

        ProvisionUserResponse response = new ProvisionUserResponse(ResponseStatus.SUCCESS);
        Map<String, Object> bindingMap = new HashMap<String, Object>();

        String requestId = "R" + UUIDGen.getUUID();

        if (resourceList == null || resourceList.isEmpty()) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.OBJECT_NOT_FOUND);
            return response;
        }

        User usr = this.userMgr.getUserWithDependent(userId, false);
        if (usr == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        ProvisionUser pUser = new ProvisionUser(usr);

        Login lg = loginManager.getPrimaryIdentity(userId);

        List<Login> principalList = loginManager.getLoginByUser(userId);

        // setup audit information

        Login lRequestor = loginManager.getPrimaryIdentity(requestorUserId);
        Login lTargetUser = loginManager.getPrimaryIdentity(userId);

        if (lRequestor != null && lTargetUser != null) {

            auditLog = auditHelper.addLog("DEPROVISION RESOURCE", lRequestor.getId().getDomainId(), lRequestor.getId().getLogin(),
                    "IDM SERVICE", usr.getCreatedBy(), "0", "USER", usr.getUserId(),
                    null, "SUCCESS", null, "USER_STATUS",
                    usr.getStatus().toString(),
                    requestId, null, null, null,
                    null, lTargetUser.getId().getLogin(), lTargetUser.getId().getDomainId());
        }


        for (String resourceId : resourceList) {

            bindingMap.put("IDENTITY", lg);
            bindingMap.put("RESOURCE", res);

            Resource res = resourceDataService.getResource(resourceId);
            if (res != null) {
                String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                if (preProcessScript != null && !preProcessScript.isEmpty()) {
                    PreProcessor ppScript = createPreProcessScript(preProcessScript);
                    if (ppScript != null) {
                        if (executePreProcess(ppScript, bindingMap, pUser, "DELETE") == ProvisioningConstants.FAIL) {
                            continue;
                        }
                    }
                }
            }

            log.debug("Resource object = " + res);

            if (res.getManagedSysId() != null)  {
                String mSysId = res.getManagedSysId();

                if (!mSysId.equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {

                    log.debug("Looking up identity for : " + mSysId);

                    Login l = getLoginForManagedSys(mSysId, principalList);

                    log.debug("Identity for Managedsys =" + l);

                    if (l != null) {

                        l.setStatus("INACTIVE");
                        l.setAuthFailCount(0);
                        l.setPasswordChangeCount(0);
                        l.setIsLocked(0);
                        loginManager.updateLogin(l);

                        ManagedSys mSys = managedSysService.getManagedSys(l.getId().getManagedSysId());

                        ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                        ManagedSystemObjectMatch matchObj = null;
                        ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");

                        log.debug("Deleting id=" + l.getId().getLogin());
                        log.debug("- delete using managed sys id=" + mSys.getManagedSysId());


                        PSOIdentifierType idType = new PSOIdentifierType(l.getId().getLogin(), null,
                                l.getId().getManagedSysId());

                        boolean connectorSuccess = false;

                        if (connector.getConnectorInterface() != null &&
                                connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {
                            UserResponse resp = remoteDelete(l, requestId, mSys, connector, matchObj, pUser, auditLog);
                            if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                connectorSuccess = true;
                            }

                        } else {
                            ResponseType resp = localDelete(l, requestId, idType, mSys, pUser, auditLog);

                            if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                connectorSuccess = true;
                            }
                        }

                        String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                        if (postProcessScript != null && !postProcessScript.isEmpty()) {
                            PostProcessor ppScript = createPostProcessScript(postProcessScript);
                            if (ppScript != null) {
                                executePostProcess(ppScript, bindingMap, pUser, "DELETE", connectorSuccess);
                            }
                        }

                    }

                }
            }




        }













        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }


    private Login getLoginForManagedSys(String managedSysId, List<Login> principalList) {
        for (Login l : principalList) {

            log.debug("Looking for identity for managedSysId " + l.getId());

            if (l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {
                return l;
            }

        }
        return null;
    }
}
