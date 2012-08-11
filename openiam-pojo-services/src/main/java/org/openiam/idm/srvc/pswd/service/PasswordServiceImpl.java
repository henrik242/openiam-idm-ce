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
package org.openiam.idm.srvc.pswd.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.exception.EncryptionException;
import org.openiam.exception.ObjectNotFoundException;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.login.LoginDAO;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyAttribute;
import org.openiam.idm.srvc.policy.dto.PolicyObjectAssoc;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.idm.srvc.policy.service.PolicyObjectAssocDAO;
import org.openiam.idm.srvc.pswd.dto.*;
import org.openiam.idm.srvc.pswd.rule.PasswordValidator;
import org.openiam.idm.srvc.secdomain.dto.SecurityDomain;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.util.encrypt.Cryptor;
import org.openiam.util.encrypt.HashDigest;


/**
 * @author suneet
 *
 */

public class PasswordServiceImpl implements PasswordService {
	
	protected SecurityDomainDataService secDomainService; 
	protected PasswordValidator passwordValidator;
	
	protected LoginDataService loginManager;
	protected UserDataService userManager;
	PolicyObjectAssocDAO policyAssocDao;
	PolicyDataService policyDataService;
	
	protected Cryptor cryptor;
	protected PasswordHistoryDAO passwordHistoryDao;
    protected HashDigest hash;
	
	
	private static final Log log = LogFactory.getLog(PasswordServiceImpl.class);
    private static final long DAY_AS_MILLIS = 86400000l;



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.policy.pswd.PasswordService#isPasswordValid(org.openiam.idm.srvc.policy.dto.Password)
	 */
	public PasswordValidationCode isPasswordValid(Password pswd) throws ObjectNotFoundException {
		
		Policy pswdPolicy = getPasswordPolicy(pswd.getDomainId(), pswd.getPrincipal(), pswd.getManagedSysId());
		
		if (pswdPolicy == null) {
			return PasswordValidationCode.PASSWORD_POLICY_NOT_FOUND;
		}
		log.info("Selected Password policy=" + pswdPolicy.getPolicyId());

        try {

		    return passwordValidator.validate(pswdPolicy, pswd);

        }catch( IOException io) {
            log.error(io);
            return PasswordValidationCode.FAIL_OTHER;
        }
	}

    @Override
    public PasswordValidationCode isPasswordValidForUser(Password pswd, User user, Login lg) throws ObjectNotFoundException {

        Policy pswdPolicy = getPasswordPolicyByUser(pswd.getDomainId(), user);

        if (pswdPolicy == null) {
            return PasswordValidationCode.PASSWORD_POLICY_NOT_FOUND;
        }
        log.info("Selected Password policy=" + pswdPolicy.getPolicyId());

        try {

            return passwordValidator.validateForUser(pswdPolicy, pswd, user, lg);

        }catch( IOException io) {
            log.error(io);
            return PasswordValidationCode.FAIL_OTHER;
        }
    }

    @Override
    public PasswordValidationCode isPasswordValidForUserAndPolicy(Password pswd, User user, Login lg, Policy policy) throws ObjectNotFoundException {

        Policy pswdPolicy = policy;
        if(pswdPolicy == null) {
            pswdPolicy = getPasswordPolicyByUser(pswd.getDomainId(), user);
        }

        if (pswdPolicy == null) {
            return PasswordValidationCode.PASSWORD_POLICY_NOT_FOUND;
        }
        log.info("Selected Password policy=" + pswdPolicy.getPolicyId());

        try {

            return passwordValidator.validateForUser(pswdPolicy, pswd, user, lg);

        }catch( IOException io) {
            log.error(io);
            return PasswordValidationCode.FAIL_OTHER;
        }
    }


    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.pswd.service.PasswordService#daysToPasswordExpiration(java.lang.String, java.lang.String, java.lang.String)
      */
	public int daysToPasswordExpiration(String domainId, String principal,
			String managedSysId) {
		
		long DAY = 86400000L;
		
		long curTime = System.currentTimeMillis();
		
		
		//Date curDate = new Date(System.currentTimeMillis());
		
		Login lg =	loginManager.getLoginByManagedSys(domainId, principal, managedSysId);
		if (lg == null) {
			return -1;
		}
		if (lg.getPwdExp() == null) {
			// no expiration date
			return 9999;
		}
		
		long endTime = lg.getPwdExp().getTime();
		
		long diffInMilliseconds = endTime - curTime;
		long diffInDays = diffInMilliseconds / DAY;
		if (diffInDays < 1) {
			return 0;
		}
		return (int)diffInDays;
		


	}


	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordService#isPasswordChangeAllowed(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean isPasswordChangeAllowed(String domainId, String principal,
			String managedSysId) {

		boolean enabled = false;
		// get the policy
		Policy policy =  getPasswordPolicy(domainId, principal, managedSysId);
		
		log.info("Password policy=" + policy);
		
		PolicyAttribute changeAttr = policy.getAttribute("PASSWORD_CHANGE_ALLOWED");
		if (changeAttr != null) {
			if (changeAttr.getValue1() != null && changeAttr.getValue1().equalsIgnoreCase("0")) {
				return false;
			}
		}
		
		
		PolicyAttribute attribute = policy.getAttribute("RESET_PER_TIME");
		if (attribute != null && attribute.getValue1() != null) {
			enabled = true;

		}
		if (enabled) {
			int changeCount = passwordChangeCount(domainId, principal, managedSysId);
			int changesAllowed =  Integer.parseInt(attribute.getValue1()); 
			
			if (changeCount >= changesAllowed) {
				return false;
			}			
		}	
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordService#passwordChangeCountByDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	public int passwordChangeCount(String domainId, String principal,
			String managedSysId) {

		Login lg =	loginManager.getLoginByManagedSys(domainId, principal, managedSysId);
		if (lg == null) {
			return -1;
		}
		return lg.getPasswordChangeCount();

	}

	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordService#getPasswordPolicy(org.openiam.idm.srvc.user.dto.User)
	 */
	public Policy getPasswordPolicy(String domainId, String principal, String managedSysId)  {
		// Find a password policy for this user
		// order of search, type, classification, domain, global
		
		// get the user for this principal
		Login lg = loginManager.getLoginByManagedSys(domainId, principal, managedSysId);
		log.info("login=" + lg);
		User user = this.userManager.getUserWithDependent(lg.getUserId(), false);
		
		return getPasswordPolicyByUser(domainId, user);
	}

    @Override
    public Policy getPasswordPolicyByUser(String domainId, User user) {
        // Find a password policy for this user
        // order of search, type, classification, domain, global

        PolicyObjectAssoc policyAssoc;

        log.info("User type and classifcation=" + user.getUserId() + " " + user.getUserTypeInd());

        if (user.getClassification() != null) {
            log.info("Looking for associate by classification.");
            policyAssoc = policyAssocDao.findAssociationByLevel("CLASSIFICATION", user.getClassification());
            if (policyAssoc != null) {
                return getPolicy(policyAssoc);
            }
        }

        // look to see if a policy exists for the type of user
        if (user.getUserTypeInd() != null) {
            log.info("Looking for associate by type.");
            policyAssoc = policyAssocDao.findAssociationByLevel("TYPE", user.getUserTypeInd());
            log.info("PolicyAssoc found=" + policyAssoc);
            if (policyAssoc != null) {
                return getPolicy(policyAssoc);
            }
        }

        if (domainId != null) {
            log.info("Looking for associate by domain.");
            policyAssoc = policyAssocDao.findAssociationByLevel("DOMAIN", domainId);
            if (policyAssoc != null) {
                return getPolicy(policyAssoc);
            }
        }
        log.info("Using global association password policy.");
        // did not find anything - get the global policy
        policyAssoc = policyAssocDao.findAssociationByLevel("GLOBAL", "GLOBAL");
        if (policyAssoc == null) {
            return null;
        }
        return getPolicy(policyAssoc);
    }

    private Policy getPolicy(PolicyObjectAssoc policyAssoc) {
		log.info("Retreiving policyId=" + policyAssoc.getPolicyId());
		return policyDataService.getPolicy(policyAssoc.getPolicyId());
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordService#passwordInHistory(org.openiam.idm.srvc.pswd.dto.Password, org.openiam.idm.srvc.policy.dto.Policy)
	 * 1 - In History, 0 - Not in history, -1 No policy defined
	 */
	public int passwordInHistory(Password pswd, Policy policy) {
		// get the list of passwords for this user.
		String decrypt = null;
		
		PolicyAttribute attr = policy.getAttribute("PWD_HIST_VER");
		if (attr == null || attr.getValue1() == null) {
			// no policy defined
			return -1;
		}
		int version =  Integer.parseInt( attr.getValue1() );
		List<PasswordHistory> historyList = this.passwordHistoryDao.findPasswordHistoryByPrincipal(
				 pswd.getDomainId(), pswd.getPrincipal(), pswd.getManagedSysId(), version);
		if (historyList == null || historyList.isEmpty()) {
			// no history
			return 0;
		}
		// check the list.
		log.info("Found " + historyList.size() + " passwords in the history");
		for ( PasswordHistory hist  : historyList) {
			String pwd = hist.getPassword();
			try {
				decrypt =  cryptor.decrypt(pwd);
			}catch(EncryptionException e) {
				log.error("Unable to decrypt password in history: " + pwd);
				throw new IllegalArgumentException("Unable to decrypt password in password history list");
			}
			if (pswd.getPassword().equals(decrypt)) {
				log.info("matching password found.");
				return 1;
			}
		}
		log.info("No match found.");
		return 0;
	}


    @Override
    public PasswordResetTokenResponse generatePasswordResetToken(PasswordResetTokenRequest request) {

        PasswordResetTokenResponse resp = new PasswordResetTokenResponse(ResponseStatus.SUCCESS );

        // number of days in which the password token will expire
        int expirationDays = 0;

        if (request == null ||
                request.getPrincipal() == null ||
                request.getDomainId() == null ||
                request.getManagedSysId() == null) {

            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }

        Policy pl =  getPasswordPolicy(request.getDomainId(), request.getPrincipal(), request.getManagedSysId());
        if (pl == null) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.PASSWORD_POLICY_NOT_FOUND);
            return resp;
        }

        PolicyAttribute expirationTime = pl.getAttribute("PWD_EXPIRATION_ON_RESET");
        if (expirationTime != null) {
            if (expirationTime.getValue1() != null ) {
                expirationDays = Integer.parseInt(expirationTime.getValue1());
            }else {
                // default to expiration time if the policy has not been defined.
                expirationDays = 3;
            }
        }

        Login l = loginManager.getLoginByManagedSys(request.getDomainId(), request.getPrincipal(), request.getManagedSysId());


        long expireDate = getExpirationTime(expirationDays);

        Date tokenExpDate = new Date(expireDate);

        String str = request.getPrincipal() + "*" + expireDate;

        String token = hash.HexEncodedHash(str);

        resp.setPasswordResetToken(token );


        // update our database
        l.setPswdResetToken(token);
        l.setPswdResetTokenExp(tokenExpDate);
        loginManager.updateLogin(l);


        return resp;
    }

    @Override
    public Response validatePasswordResetToken(String token) {

        Response resp = new Response(ResponseStatus.SUCCESS );

        // look up the token
        Login l = loginManager.getPasswordResetToken(token);
        if ( l == null ) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;

        }

        // check if the token is still valid
        Date expToken = l.getPswdResetTokenExp();
        long expTokenMillis = expToken.getTime();

        long curTime = System.currentTimeMillis();

        if (curTime > expTokenMillis) {

            // token is old - fails validation
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;

        }
        return resp;
    }


    protected long getExpirationTime(int numberOfDays) {

        long curTime = System.currentTimeMillis();
        long tokenLife = numberOfDays * DAY_AS_MILLIS;

        return (curTime + tokenLife);


    }

    public SecurityDomainDataService getSecDomainService() {
		return secDomainService;
	}

	public void setSecDomainService(SecurityDomainDataService secDomainService) {
		this.secDomainService = secDomainService;
	}

	public PasswordValidator getPasswordValidator() {
		return passwordValidator;
	}

	public void setPasswordValidator(PasswordValidator passwordValidator) {
		this.passwordValidator = passwordValidator;
	}


	public LoginDataService getLoginManager() {
		return loginManager;
	}


	public void setLoginManager(LoginDataService loginManager) {
		this.loginManager = loginManager;
	}


	public UserDataService getUserManager() {
		return userManager;
	}


	public void setUserManager(UserDataService userManager) {
		this.userManager = userManager;
	}


	public PolicyObjectAssocDAO getPolicyAssocDao() {
		return policyAssocDao;
	}


	public void setPolicyAssocDao(PolicyObjectAssocDAO policyAssocDao) {
		this.policyAssocDao = policyAssocDao;
	}


	public PolicyDataService getPolicyDataService() {
		return policyDataService;
	}


	public void setPolicyDataService(PolicyDataService policyDataService) {
		this.policyDataService = policyDataService;
	}




	public Cryptor getCryptor() {
		return cryptor;
	}


	public void setCryptor(Cryptor cryptor) {
		this.cryptor = cryptor;
	}


	public PasswordHistoryDAO getPasswordHistoryDao() {
		return passwordHistoryDao;
	}


	public void setPasswordHistoryDao(PasswordHistoryDAO passwordHistoryDao) {
		this.passwordHistoryDao = passwordHistoryDao;
	}


    public HashDigest getHash() {
        return hash;
    }

    public void setHash(HashDigest hash) {
        this.hash = hash;
    }
}
