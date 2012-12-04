package org.openiam.idm.srvc.synch.srcadapter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.MatchObjectRule;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserListResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class DefaultMatchObjectRule implements MatchObjectRule {

	private UserDataWebService userManager = null;
	public static ApplicationContext ac;
	
	private String matchAttrName = null;
	private String matchAttrValue = null;

    private static final Log log = LogFactory.getLog(DefaultMatchObjectRule.class);
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ac = applicationContext;
}
	
	public User lookup(SynchConfig config, Map<String, Attribute> rowAttr) {
		UserSearch search = new UserSearch();


        Attribute atr =  rowAttr.get(config.getCustomMatchAttr());

        if ( atr == null ) {

            log.debug("Match attribute not found. Check synchronization configuration");
            return null;

        }

		String srcFieldValue = atr.getValue();
		matchAttrValue = srcFieldValue;
		matchAttrName = config.getMatchFieldName();
	
		
		if ( MATCH_USERID.equalsIgnoreCase(config.getMatchFieldName())) {
			search.setUserId(srcFieldValue);
		}
		if (MATCH_PRINCIPAL.equalsIgnoreCase(config.getMatchFieldName()) ) {
			search.setPrincipal(srcFieldValue);
		}
		if (MATCH_EMAIL.equalsIgnoreCase(config.getMatchFieldName())) {
			search.setEmailAddress(srcFieldValue);
		}	
		if (MATCH_EMPLOYEE_ID.equalsIgnoreCase(config.getMatchFieldName())) {
			search.setEmployeeId(srcFieldValue);
		}		
		if (MATCH_CUSTOM_ATTRIBUTE.equalsIgnoreCase(config.getMatchFieldName())) {

            log.debug("- Match users with custom attribute: " + config.getCustomMatchAttr());
		
				
			// get the attribute value from the data_set
			String valueToMatch = rowAttr.get(config.getCustomMatchAttr()).getValue();

            log.debug("- Src field value used in matching: " + valueToMatch);
			
			search.setAttributeName(config.getCustomMatchAttr());
			search.setAttributeValue(valueToMatch);
			
			//
			matchAttrName = search.getAttributeName();
			matchAttrValue = search.getAttributeValue();
			
		}
		userManager = (UserDataWebService) ac.getBean("userWS");

        UserListResponse userListResponse = userManager.search(search);
        if (userListResponse.getStatus() == ResponseStatus.SUCCESS) {
            List<User> userList = userListResponse.getUserList();

            if (userList != null && !userList.isEmpty()) {
			    log.debug("User matched with existing user...");

                User u = userList.get(0);
			    return u;
		    }
        }
        log.debug("No matching user found");

		return null;
	}

	public String getMatchAttrName() {
		return matchAttrName;
	}

	public void setMatchAttrName(String matchAttrName) {
		this.matchAttrName = matchAttrName;
	}

	public String getMatchAttrValue() {
		return matchAttrValue;
	}

	public void setMatchAttrValue(String matchAttrValue) {
		this.matchAttrValue = matchAttrValue;
	}

    @Override
    public String toString() {
        return "DefaultMatchObjectRule{" +
                "userManager=" + userManager +
                ", matchAttrName='" + matchAttrName + '\'' +
                ", matchAttrValue='" + matchAttrValue + '\'' +
                '}';
    }
}
