package org.openiam.idm.srvc.authz.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.authz.dto.AuthAttribute;
import org.openiam.idm.srvc.authz.dto.AuthzRequest;
import org.openiam.idm.srvc.authz.dto.AuthzResponse;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.service.ProvisionService;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "org.openiam.idm.srvc.authz.ws.AuthorizationWebService",
		targetNamespace = "urn:idm.openiam.org/srvc/authz/service",
		portName = "AuthorizationWebServicePort",
		serviceName = "AuthorizationWebService")
public class AuthorizationWebServiceImpl implements AuthorizationWebService
{
    private static final Log log = LogFactory.getLog(AuthorizationWebServiceImpl.class);
    protected LoginDataService loginManager;
    protected ResourceDataService resourceDataService;
    protected UserDataService userManager;


    public AuthzResponse isAuthorized( AuthzRequest request) {
     log.info("isAuthorized called.");

        AuthzResponse response  = new AuthzResponse();
        response.setStatus( ResponseStatus.FAILURE);

        if (request.getAttributeList() == null || request.getAttributeList().size() == 0) {
            return response;
        }
       if (request.getPrincipalName() == null || request.getPrincipalName().length() == 0) {
           return response;
       }
       if (request.getDomain() == null || request.getDomain().length() == 0) {
           return response;
       }

        // get the user object for this identity
        String principal = request.getPrincipalName();
        String domain = request.getDomain();

        Login lg = loginManager.getLoginByManagedSys(domain, principal, "0");
        if (lg == null) {
            //response.setAuthErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            response.setAuthErrorMessage("Invalid Login");
            return response;

        }

        String userId = lg.getUserId();

        response.setStatus(ResponseStatus.SUCCESS);

        List<Resource> resList = getResourceObjForUser(userId);

		log.info("- resList= " + resList);
		if (resList == null) {
			log.info("resource list for user is null");

            response.setAuthorized(false);
            return response;
		}

        // get the property that we are looking for.
        List<AuthAttribute> attrList  = request.getAttributeList();

        if (attrList != null && attrList.size() > 0) {
            AuthAttribute atr =  attrList.get(0);
            String propertyName = atr.getName();
            String propertyValue = atr.getValue();



            if (propertyName == null && propertyName.length() ==0) {
                response.setAuthorized(false);
                return response;
            }
            if (propertyValue == null && propertyValue.length() ==0) {
                response.setAuthorized(false);
                return response;
            }

            log.debug("Property Name * Value = " + propertyName + " " + propertyValue);

            for (Resource res : resList) {

                ResourceProp prop =  res.getResourceProperty(propertyName);
                if (prop != null) {
                    String val = prop.getPropValue();

                    log.debug("Resource configuration value=" + val);

                    if (val != null && val.length() > 0) {
                        val = val.toLowerCase();
                        propertyValue = propertyValue.toLowerCase();

                        if (propertyValue.contains(val)) {
                            response.setAuthorized(true);
                        }

                    }
                }
            }

        }

        return response;
    }

    public List<Resource> getResourceObjForUser(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("UserId object is null");
		}

        return resourceDataService.getResourceObjForUser(userId);
	}

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public UserDataService getUserManager() {
        return userManager;
    }

    public void setUserManager(UserDataService userManager) {
        this.userManager = userManager;
    }
}
