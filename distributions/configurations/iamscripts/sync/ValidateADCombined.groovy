

import java.util.Map;

import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.service.ValidationScript
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import javax.xml.ws.Service
import javax.xml.namespace.QName
import javax.xml.ws.soap.SOAPBinding
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.auth.dto.Login
import org.openiam.idm.srvc.auth.dto.LoginId


public class ValidateADCombined implements ValidationScript {

    static String DOMAIN = "USR_SEC_DOMAIN";
    static String AD_MANAGED_SYS_ID = "110";

	public int isValid(LineObject rowObj) {
		println("1-Validation script called.");

		Attribute attrVal = null;
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();

        attrVal = columnMap.get("UserPrincipalName");
        if (attrVal == null) {
            return ValidationScript.NOT_VALID;

        }
        if (attrVal.value == null || attrVal.value.isEmpty()) {
            return ValidationScript.NOT_VALID;
        }

        // check if this CN already exists (skip if it does)
        attrVal = columnMap.get("cn");
        if (attrVal == null) {
            return ValidationScript.NOT_VALID;

        }
        if (attrVal.value == null || attrVal.value.isEmpty()) {
            return ValidationScript.NOT_VALID;
        }

	
		return ValidationScript.VALID;
		
	}
	private boolean isNull(Attribute attrVal) {
		if (attrVal == null || attrVal.getValue() == null  || attrVal.getValue().length() == 0  ) {
			return true;
		}
		return false;
	}


}
