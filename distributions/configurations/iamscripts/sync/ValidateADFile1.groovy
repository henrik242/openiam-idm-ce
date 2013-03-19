

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


public class ValidateADFile1 implements ValidationScript {

    static String DOMAIN = "USR_SEC_DOMAIN";
    static String AD_MANAGED_SYS_ID = "110";

	public int isValid(LineObject rowObj) {

        def LoginDataWebService loginService  = loginService();
		
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
       /* attrVal = columnMap.get("cn");
        if (attrVal != null && attrVal.value != null) {

            Login lg =  loginService.getLoginByManagedSys(DOMAIN, attrVal.value, AD_MANAGED_SYS_ID).principal;
            // identity already exists
            if (lg != null) {
                return ValidationScript.NOT_VALID;
            }



        }
        */

	return ValidationScript.VALID;
		
	}
	private boolean isNull(Attribute attrVal) {
		if (attrVal == null || attrVal.getValue() == null  || attrVal.getValue().length() == 0  ) {
			return true;
		}
		return false;
	}

    static LoginDataWebService loginService() {
        ResourceBundle res = ResourceBundle.getBundle("datasource");
        String BASE_URL =  res.getString("openiam.service_host") + res.getString("openiam.idm.ws.path");


        String serviceUrl = BASE_URL + "/LoginDataWebService"
        String port ="LoginDataWebServicePort"
        String nameSpace = "urn:idm.openiam.org/srvc/auth/service"

        Service service = Service.create(QName.valueOf(serviceUrl))

        service.addPort(new QName(nameSpace,port),
                SOAPBinding.SOAP11HTTP_BINDING,	serviceUrl)

        return service.getPort(new QName(nameSpace,	port),
                LoginDataWebService.class);
    }
}
