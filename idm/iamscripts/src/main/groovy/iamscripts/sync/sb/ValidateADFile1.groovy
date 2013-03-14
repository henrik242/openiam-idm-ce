

import java.util.Map;

import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.service.ValidationScript;


public class ValidateADFile1 implements ValidationScript {
	
	public int isValid(LineObject rowObj) {
		println("1-Validation script called.");
		
		Attribute attrVal = null;
		Map<String,Attribute> columnMap =  rowObj.getColumnMap();
		
		
	
		return ValidationScript.VALID;
		
	}
	private boolean isNull(Attribute attrVal) {
		if (attrVal == null || attrVal.getValue() == null  || attrVal.getValue().length() == 0  ) {
			return true;
		}
		return false;
	}
}
