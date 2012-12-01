package org.openiam.idm.srvc.synch.service;

import java.util.Map;

import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.user.dto.User;
import org.springframework.context.ApplicationContextAware;

/**
 * Interface to define the rule that will be used for matching objects during the synchronization process.
 * @author suneet
 *
 */
public interface MatchObjectRule extends  ApplicationContextAware {

    public static final String MATCH_USERID = "USERID";
    public static final String MATCH_PRINCIPAL = "PRINCIPAL";
    public static final String MATCH_EMAIL = "EMAIL";
    public static final String MATCH_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String MATCH_CUSTOM_ATTRIBUTE = "ATTRIBUTE";

	/**
	 * Look up the user contained in the user object with in the IDM system.
	 * The look up will be based on the match criteria defined in the config object.
	 * @param config
	 * @param user
	 * @return
	 */
	User lookup(SynchConfig config, Map<String, Attribute> rowAttr);
	
	String getMatchAttrName() ;

	void setMatchAttrName(String matchAttrName) ;

	String getMatchAttrValue() ;

	void setMatchAttrValue(String matchAttrValue) ;
}
