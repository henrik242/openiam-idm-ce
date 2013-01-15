package org.openiam.selfsrvc.example;

import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.io.Serializable;

/**
 * Command object for the ManageMyIdentityController
 *
 * @author suneet
 */
public class FormExampleCommand implements Serializable {


    protected String field1 = "1";
    protected String field2 = "2";
    protected String field3 = "3";
    protected boolean readOnly;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
