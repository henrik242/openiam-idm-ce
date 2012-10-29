package org.openiam.selfsrvc.myidentity;

import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.io.Serializable;

/**
 * Command object for the ManageMyIdentityController
 *
 * @author suneet
 */
public class ManageMyIdentityCommand implements Serializable {


    protected Login principal;
    protected String userId; // personId
    protected ManagedSys[] managedSysAry;
    protected boolean newRecord = false;

    public Login getPrincipal() {
        return principal;
    }

    public void setPrincipal(Login principal) {
        this.principal = principal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ManagedSys[] getManagedSysAry() {
        return managedSysAry;
    }

    public void setManagedSysAry(ManagedSys[] managedSysAry) {
        this.managedSysAry = managedSysAry;
    }


    public boolean isNewRecord() {
        return newRecord;
    }

    public void setNewRecord(boolean newRecord) {
        this.newRecord = newRecord;
    }
}
