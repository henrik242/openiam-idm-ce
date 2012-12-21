package org.openiam.webadmin.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.idm.srvc.msg.dto.NotificationDto;
import org.openiam.idm.srvc.msg.ws.SysMessageWebService;
import org.openiam.idm.srvc.synch.util.UserSearchUtils;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.synch.ws.IdentitySynchWebService;

import java.util.Collections;
import java.util.List;


public class BulkProvsioningValidator implements Validator {

	private static final Log log = LogFactory.getLog(BulkProvsioningValidator.class);
    private IdentitySynchWebService idSyncClient;
    private UserDataWebService userDataWebService;
    private SysMessageWebService sysMessageService;

	public boolean supports(Class cls) {
		 return BulkProvisioningCommand.class.equals(cls);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object cmd, Errors arg1) {
        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;
		
	}

	
	public void validateUserSelectionForm(Object cmd, Errors err) {
        log.info("validateUserSelectionForm() ...." );

        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;


        // check if at least on search criteria has been selected.
        if (! provCmd.isSearchDefined()) {
            err.rejectValue("lastName", "required");
            return;
        }

        log.info("validated that search criteria exist. Checking the resultset next() ...." );

        BulkMigrationConfig config = provCmd.getConfig();

        Integer resultSize = (Integer)  idSyncClient.testBulkMigrationImpact(config).getResponseValue();

        log.info("validateUserSelectionForm: " + resultSize);

        if (resultSize != null) {
            int resultSetSize = Math.min(resultSize.intValue(), 100);
            provCmd.setResultSetSize(resultSetSize);
            config.setMaxResultSize(resultSetSize);
            log.info("Result set size: " + resultSize.intValue());
        }

        List<User> userList = userDataWebService.search(UserSearchUtils.buildSearch(config)).getUserList();
        if(userList != null) {
            provCmd.setUsers(userList);
        } else {
            provCmd.setUsers(Collections.EMPTY_LIST);
        }
        List<NotificationDto> notificationDtos = sysMessageService.getAllMessages().getSysMessageList();
        if(userList != null) {
            provCmd.setNotifications(notificationDtos);
        } else {
            provCmd.setNotifications(Collections.EMPTY_LIST);
        }

    }


	
	public void validateOperation(Object cmd, Errors err) {
        log.info("validateOperation() ...." );

        BulkProvisioningCommand provCmd =  (BulkProvisioningCommand) cmd;

        if ( provCmd.getActionType() == null || provCmd.getActionType().isEmpty()) {

            err.rejectValue("actionType", "required");
            return;
        }

        if (    BulkMigrationConfig.ACTION_RESET_PASSWORD.equalsIgnoreCase(provCmd.getActionType()) &&
                (provCmd.getNewPassword() == null || provCmd.getNewPassword().isEmpty()  ) ) {

            err.rejectValue("newPassword", "required");
            return;
        }

        if (BulkMigrationConfig.ACTION_MODIFY_ACCESS.equalsIgnoreCase(provCmd.getActionType())) {

            if (    (provCmd.getTargetResource() == null || provCmd.getTargetResource().isEmpty()) &&
                    (provCmd.getTargetRole() == null || provCmd.getTargetRole().isEmpty())  ) {

                    err.rejectValue("targetResource", "required");
            }


            if (    (provCmd.getTargetResource() != null && !provCmd.getTargetResource().isEmpty()) &&
                    (provCmd.getTargetRole() != null && !provCmd.getTargetRole().isEmpty())  ) {

                    err.rejectValue("targetResource", "both");

            }
        }

        if (BulkMigrationConfig.ACTION_SEND_EMAIL.equalsIgnoreCase(provCmd.getActionType())) {
            if(StringUtils.isEmpty(provCmd.getSelectedNotificationName())) {
                err.rejectValue("selectedNotificationId", "required");
            }
        }
		
	}

    public IdentitySynchWebService getIdSyncClient() {
        return idSyncClient;
    }

    public void setIdSyncClient(IdentitySynchWebService idSyncClient) {
        this.idSyncClient = idSyncClient;
    }

    public UserDataWebService getUserDataWebService() {
        return userDataWebService;
    }

    public void setUserDataWebService(UserDataWebService userDataWebService) {
        this.userDataWebService = userDataWebService;
    }

    public SysMessageWebService getSysMessageService() {
        return sysMessageService;
    }

    public void setSysMessageService(SysMessageWebService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }
}
