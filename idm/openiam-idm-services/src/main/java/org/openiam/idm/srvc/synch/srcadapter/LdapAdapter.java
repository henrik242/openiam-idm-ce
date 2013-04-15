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
package org.openiam.idm.srvc.synch.srcadapter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.util.StringUtils;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.Response;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Scan Ldap for any new records, changed users, or delete operations and then synchronizes them back into OpenIAM.
 *
 * @author suneet
 */
public class LdapAdapter implements SourceAdapter {

    public ApplicationContext ac;

    private LoginDataService loginManager;
    private RoleDataService roleDataService;
    private AuditHelper auditHelper;
    private MatchRuleFactory matchRuleFactory;

    private static ResourceBundle secres = ResourceBundle.getBundle("securityconf");

    private LdapContext ctx;

    private UserDataService userMgr;
    private String systemAccount;
    private static final Log log = LogFactory.getLog(LdapAdapter.class);

    private static final int THREAD_COUNT = 5;

    public SyncResponse startSynch(final SynchConfig config) {

        // String changeLog = null;
        // Date mostRecentRecord = null;
        //    long mostRecentRecord = 0L;
        String lastRecProcessed = null;
        final ProvisionService provService = (ProvisionService) ac.getBean("defaultProvision");

        log.debug("LDAP startSynch CALLED.^^^^^^^^");

        String requestId = UUIDGen.getUUID();

        IdmAuditLog synchStartLog_ = new IdmAuditLog();
        synchStartLog_.setSynchAttributes("SYNCH_USER", config.getSynchConfigId(), "START", "SYSTEM", requestId);
        final IdmAuditLog synchStartLog = auditHelper.logEvent(synchStartLog_);

        try {

            if (!connect(config)) {

                SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_CONNECTION);
                return resp;
            }
            // get the last execution time
            if (config.getLastRecProcessed() != null) {
                lastRecProcessed = config.getLastRecProcessed();
            }

            // get change log field
            if (config.getSynchType().equalsIgnoreCase("INCREMENTAL")) {
                if (lastRecProcessed != null) {
                    // update the search filter so that it has the new time
                    String ldapFilterQuery = config.getQuery();
                    // replace wildcards with the last exec time

                    config.setQuery(ldapFilterQuery.replace("?", lastRecProcessed));

                    log.debug("Updated ldap filter = " + config.getQuery());
                }
            }

            final ValidationScript validationScript = StringUtils.isNotEmpty(config.getValidationRule()) ? SynchScriptFactory.createValidationScript(config.getValidationRule()) : null;
            final TransformScript transformScript = StringUtils.isNotEmpty(config.getTransformationRule()) ? SynchScriptFactory.createTransformationScript(config.getTransformationRule()) : null;
            // rule used to match object from source system to data in IDM
            final MatchObjectRule matchRule = matchRuleFactory.create(config);

            NamingEnumeration<SearchResult> results = search(config);
            List<SearchResult> resultList = Collections.list(results);
            int allRowsCount = resultList.size();
            int threadCoount = THREAD_COUNT;
            int rowsInOneExecutors = allRowsCount / threadCoount;
            int remains = allRowsCount % (rowsInOneExecutors * threadCoount);
            if (remains != 0) {
                threadCoount++;
            }
            log.debug("Thread count = " + threadCoount + "; Rows in one thread = " + rowsInOneExecutors + "; Remains rows = " + remains);

            List<Future> threadResults = new LinkedList<Future>();
            ExecutorService service = Executors.newCachedThreadPool();
            for (int i = 0; i < threadCoount; i++) {
                final int startIndex = i * rowsInOneExecutors;
                int shiftIndex = i != threadCoount - 1 ? rowsInOneExecutors : remains;

                final List<SearchResult> part = resultList.subList(startIndex, startIndex + shiftIndex);
                threadResults.add(service.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            proccess(config, provService, synchStartLog, validationScript, transformScript, matchRule, startIndex, part);
                        } catch (NamingException ne) {


                            log.error(ne);

                            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.DIRECTORY_NAMING_EXCEPTION.toString(), ne.toString());
                            auditHelper.logEvent(synchStartLog);

                            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
                            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
                            resp.setErrorText(ne.toString());
                        }
                    }
                }));
            }

            waitUntilWorkDone(threadResults);


        } catch (ClassNotFoundException cnfe) {

            log.error(cnfe);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.CLASS_NOT_FOUND.toString(), cnfe.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            return resp;
        } catch (IOException fe) {


            log.error(fe);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.FILE_EXCEPTION.toString(), fe.toString());
            auditHelper.logEvent(synchStartLog);


            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FILE_EXCEPTION);
            resp.setErrorText(fe.toString());
            return resp;


        } catch (InterruptedException e) {
            log.error(e);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.INTERRUPTED_EXCEPTION.toString(), e.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.INTERRUPTED_EXCEPTION);
            return resp;
        } catch (NamingException ne) {


            log.error(ne);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.DIRECTORY_NAMING_EXCEPTION.toString(), ne.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            resp.setErrorText(ne.toString());
            return resp;
        }
        log.debug("LDAP SYNCHRONIZATION COMPLETE^^^^^^^^");

        SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);
        //resp.setLastRecordTime(mostRecentRecord);
        resp.setLastRecProcessed(lastRecProcessed);
        return resp;

    }

    private void proccess(SynchConfig config, ProvisionService provService, IdmAuditLog synchStartLog, ValidationScript validationScript, TransformScript transformScript, MatchObjectRule matchRule, int ctr, List<SearchResult> part) throws NamingException {
        for (SearchResult sr : part) {
            Attributes attrs = sr.getAttributes();

            ProvisionUser pUser = new ProvisionUser();
            LineObject rowObj = new LineObject();

            log.debug("-New Row to Synchronize --" + ctr++);

            if (attrs != null) {
                // try {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {

                    javax.naming.directory.Attribute attr = (javax.naming.directory.Attribute) ae.next();

                    List<String> valueList = new ArrayList<String>();

                    String key = attr.getID();

                    log.debug("attribute id=: " + key);


                    for (NamingEnumeration e = attr.getAll(); e.hasMore(); ) {
                        Object o = e.next();
                        if (o.toString() != null) {
                            valueList.add(o.toString());
                            log.debug("- value:=" + o.toString());
                        }
                    }
                    if (valueList.size() > 0) {
                        Attribute rowAttr = new Attribute();
                        rowAttr.populateAttribute(key, valueList);
                        rowObj.put(key, rowAttr);
                    } else {
                        log.debug("- value is null");
                    }
                }


            }


            LastRecordTime lrt = getRowTime(rowObj);

            log.debug("STarting validation and transformation..");


            // start the synch process
            // 1) Validate the data
            // 2) Transform it
            // 3) if not delete - then match the object and determine if its a new object or its an udpate

            // validate
            if (validationScript != null) {

                int retval = validationScript.isValid(rowObj);
                if (retval == ValidationScript.NOT_VALID) {
                    log.error("Row Object Faied Validation=" + rowObj.toString());
                    // log this object in the exception log

                    continue;
                }
                if (retval == ValidationScript.SKIP) {
                    continue;
                }
            }

            // check if the user exists or not
            Map<String, Attribute> rowAttr = rowObj.getColumnMap();
            //
            User usr = matchRule.lookup(config, rowAttr);


            // transform
            if (transformScript != null) {

                // initialize the transform script
                transformScript.init();

                if (usr != null) {
                    transformScript.setNewUser(false);
                    transformScript.setUser(userMgr.getUserWithDependent(usr.getUserId(), true));
                    transformScript.setPrincipalList(loginManager.getLoginByUser(usr.getUserId()));
                    transformScript.setUserRoleList(roleDataService.getUserRolesAsFlatList(usr.getUserId()));

                } else {
                    transformScript.setNewUser(true);
                }

                int retval = transformScript.execute(rowObj, pUser);

                log.debug("Transform result=" + retval);

                pUser.setSessionId(synchStartLog.getSessionId());


                if (retval == TransformScript.DELETE && usr != null) {
                    log.debug("deleting record - " + usr.getUserId());
                    ProvisionUserResponse userResp = provService.deleteByUserId(new ProvisionUser(usr), UserStatusEnum.DELETED, systemAccount);


                } else {
                    // call synch
                    if (retval != TransformScript.DELETE) {
                        System.out.println("Provisioning user=" + pUser.getLastName());
                        if (usr != null) {
                            log.debug("updating existing user...systemId=" + pUser.getUserId());
                            pUser.setUserId(usr.getUserId());
                            ProvisionUserResponse userResp = provService.modifyUser(pUser);

                        } else {
                            log.debug("adding new user...");
                            pUser.setUserId(null);
                            ProvisionUserResponse userResp = provService.addUser(pUser);


                        }
                    }
                }
            }
            // show the user object


        }
    }

    private void waitUntilWorkDone(List<Future> results) throws InterruptedException {
        boolean success = false;
        while (!success) {
            for (Future future : results) {
                if (!future.isDone()) {
                    success = false;
                    break;
                } else {
                    success = true;
                }
            }
            Thread.sleep(500);
        }
    }

    public Response testConnection(SynchConfig config) {
        try {
            if (connect(config)) {
                closeConnection();
                Response resp = new Response(ResponseStatus.SUCCESS);
                return resp;
            } else {
                Response resp = new Response(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_CONNECTION);
                return resp;
            }
        } catch (NamingException e) {
            e.printStackTrace();
            log.error(e);

            Response resp = new Response(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_CONNECTION);
            resp.setErrorText(e.getMessage());
            return resp;
        }
    }

    private LastRecordTime getRowTime(LineObject rowObj) {
        Attribute atr = rowObj.get("modifyTimestamp");

        if (atr != null && atr.getValue() != null) {
            return getTime(atr);
        }
        atr = rowObj.get("createTimestamp");

        if (atr != null && atr.getValue() != null) {
            return getTime(atr);
        }
        return new LastRecordTime();


    }

    private LastRecordTime getTime(Attribute atr) {
        LastRecordTime lrt = new LastRecordTime();

        String s = atr.getValue();
        int i = s.indexOf("Z");
        if (i == -1) {
            i = s.indexOf("-");
        }
        if (i > 0) {
            lrt.mostRecentRecord = Long.parseLong(s.substring(0, i));
            lrt.generalizedTime = atr.getValue();
            return lrt;

        }
        lrt.mostRecentRecord = Long.parseLong(s);
        lrt.generalizedTime = atr.getValue();
        return lrt;


    }

    private NamingEnumeration<SearchResult> search(SynchConfig config) throws NamingException {

        // String attrIds[] = {"1.1", "+", "*"};

        String attrIds[] = {"1.1", "+", "*", "accountUnlockTime", "aci", "aclRights", "aclRightsInfo", "altServer", "attributeTypes", "changeHasReplFixupOp", "changeIsReplFixupOp", "copiedFrom", "copyingFrom", "createTimestamp", "creatorsName", "deletedEntryAttrs", "dITContentRules", "dITStructureRules", "dncomp", "ds-pluginDigest", "ds-pluginSignature", "ds6ruv", "dsKeyedPassword", "entrydn", "entryid", "hasSubordinates", "idmpasswd", "isMemberOf", "ldapSchemas", "ldapSyntaxes", "matchingRules", "matchingRuleUse", "modDNEnabledSuffixes", "modifiersName", "modifyTimestamp", "nameForms", "namingContexts", "nsAccountLock", "nsBackendSuffix", "nscpEntryDN", "nsds5ReplConflict", "nsIdleTimeout", "nsLookThroughLimit", "nsRole", "nsRoleDN", "nsSchemaCSN", "nsSizeLimit", "nsTimeLimit", "nsUniqueId", "numSubordinates", "objectClasses", "parentid", "passwordAllowChangeTime", "passwordExpirationTime", "passwordExpWarned", "passwordHistory", "passwordPolicySubentry", "passwordRetryCount", "pwdAccountLockedTime", "pwdChangedTime", "pwdFailureTime", "pwdGraceUseTime", "pwdHistory", "pwdLastAuthTime", "pwdPolicySubentry", "pwdReset", "replicaIdentifier", "replicationCSN", "retryCountResetTime", "subschemaSubentry", "supportedControl", "supportedExtension", "supportedLDAPVersion", "supportedSASLMechanisms", "supportedSSLCiphers", "targetUniqueId", "vendorName", "vendorVersion"};

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(attrIds);

        String searchFilter = config.getQuery();

        return ctx.search(config.getBaseDn(), searchFilter, searchCtls);


    }


    private boolean connect(SynchConfig config) throws NamingException {

        Hashtable<String, String> envDC = new Hashtable();
        String keystore = secres.getString("KEYSTORE");
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
