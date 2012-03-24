/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */
/**
 *
 */
package org.openiam.idm.srvc.synch.service.generic;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.MatchObjectRule;
import org.openiam.idm.srvc.synch.service.SourceAdapter;
import org.openiam.idm.srvc.synch.service.TransformScript;
import org.openiam.idm.srvc.synch.service.ValidationScript;
import org.openiam.idm.srvc.synch.srcadapter.MatchRuleFactory;
import org.openiam.idm.srvc.synch.srcadapter.SynchScriptFactory;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.IOException;
import java.util.*;

/**
 * Scan Ldap for any new records, changed users, or delete operations and then synchronizes them back into OpenIAM.
 *
 * @author suneet
 */
public class LdapAdapterForGenericObject implements SourceAdapter {

    protected LineObject rowHeader = new LineObject();
    protected ProvisionUser pUser = new ProvisionUser();
    private String keystore;

    public static ApplicationContext ac;

    protected LoginDataService loginManager;
    protected RoleDataService roleDataService;
    protected AuditHelper auditHelper;
    protected MatchRuleFactory matchRuleFactory;

    static protected ResourceBundle secres = ResourceBundle.getBundle("securityconf");

    LdapContext ctx = null;

    protected UserDataService userMgr;
    ProvisionService provService = null;
    String systemAccount;
    private static final Log log = LogFactory.getLog(LdapAdapterForGenericObject.class);


    public SyncResponse startSynch(SynchConfig config) {
        // rule used to match object from source system to data in IDM
        MatchObjectRule matchRule = null;
       // String changeLog = null;
       // Date mostRecentRecord = null;
        long mostRecentRecord = 0L;
        String lastRecProcessed = null;


        SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);

        return resp;

    }






    private boolean connect(SynchConfig config) throws NamingException {

        Hashtable<String, String> envDC = new Hashtable();
        keystore = secres.getString("KEYSTORE");
        System.setProperty("javax.net.ssl.trustStore", keystore);

        String hostUrl = config.getSrcHost(); //   managedSys.getHostUrl();
        log.debug("Directory host url:" + hostUrl);


        envDC.put(Context.PROVIDER_URL, hostUrl);
        envDC.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        envDC.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple
        envDC.put(Context.SECURITY_PRINCIPAL, config.getSrcLoginId());  //"administrator@diamelle.local"
        envDC.put(Context.SECURITY_CREDENTIALS, config.getSrcPassword());

        if (hostUrl.contains("ldaps")) {

            envDC.put(Context.SECURITY_PROTOCOL, "SSL");
        }


        ctx = new InitialLdapContext(envDC, null);
        if (ctx != null) {
            return true;
        }

        return false;


    }

    private void closeConnection() {
        try {
            if (ctx != null) {
                ctx.close();
            }

        } catch (NamingException ne) {
            log.error(ne.getMessage(), ne);
            ne.printStackTrace();
        }

    }


    public MatchRuleFactory getMatchRuleFactory() {
        return matchRuleFactory;
    }


    public void setMatchRuleFactory(MatchRuleFactory matchRuleFactory) {
        this.matchRuleFactory = matchRuleFactory;
    }


    public String getSystemAccount() {
        return systemAccount;
    }


    public void setSystemAccount(String systemAccount) {
        this.systemAccount = systemAccount;
    }


    public LoginDataService getLoginManager() {
        return loginManager;
    }


    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }


    public RoleDataService getRoleDataService() {
        return roleDataService;
    }


    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }


    public UserDataService getUserMgr() {
        return userMgr;
    }


    public void setUserMgr(UserDataService userMgr) {
        this.userMgr = userMgr;
    }


    public AuditHelper getAuditHelper() {
        return auditHelper;
    }


    public void setAuditHelper(AuditHelper auditHelper) {
        this.auditHelper = auditHelper;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public void setMuleContext(MuleContext ctx) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class LastRecordTime {
        long mostRecentRecord;
        String generalizedTime;

    }



}
