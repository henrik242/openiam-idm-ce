package org.openiam.selfsrvc.login;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.service.AuthenticationConstants;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyAttribute;
import org.openiam.idm.srvc.prov.request.ws.RequestWebService;
import org.openiam.idm.srvc.pswd.service.ChallengeResponseService;
import org.openiam.idm.srvc.pswd.ws.PasswordWebService;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.selfsrvc.AppConfiguration;
import org.openiam.selfsrvc.pswd.PasswordConfiguration;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class LoginController extends SimpleFormController {


	private static final Log log = LogFactory.getLog(LoginController.class);
	private String rootMenu;
	
	protected NavigatorDataWebService navigationDataService;
	protected SecurityDomainDataService secDomainService;
	protected AppConfiguration appConfiguration;
	protected PasswordConfiguration configuration;
	protected PasswordWebService passwordService;
    protected String extendController;
	 
	 String publicLeftMenuGroup;
	 String publicRightMenuGroup1;
	 String publicRightMenuGroup2;
	 String publicRightMenuGroup3;
	 String leftMenuGroup;
	 String rightMenuGroup1;
	 String rightMenuGroup2;
	 String rightMenuGroup3;

     String cookiepath;
     String karoscookie;







	 protected UserDataWebService userMgr;
	 protected GroupDataWebService groupManager;
	 protected RoleDataWebService roleDataService;
	 protected ChallengeResponseService challengeResponse;
	 protected LoginDataWebService loginManager;
     protected RequestWebService provRequestService;
     protected OrganizationDataService orgManager;

    ResourceDataService resourceDataService;
    ResourceBundle res = ResourceBundle.getBundle("datasource");

    String COOKIE_SECURE = res.getString("IAM_CookieSecure");
    String COOKIE_HTTP_ONLY = res.getString("IAM_CookieHttpOnly");
    String HTTPS_ONLY = res.getString("HTTPS_ONLY");
    String COOKIE_DOMAIN = res.getString("COOKIE_DOMAIN");



    public LoginController() {
		super();
	}

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        LoginCommand loginCmd = new LoginCommand();
        String remoteHost = request.getRemoteHost();

        loginCmd.setClientIP( remoteHost);

        String expire = request.getParameter("expire");
        if (expire != null) {
            String reqUrl = (String)request.getSession().getAttribute("requrl") ;

            
            if ("2".equals(expire)) {
                request.getSession().invalidate();
                request.setAttribute("mode","2");
            }

            // rebuild the session so that we have a clean session when the user signs in again.

            if (reqUrl != null && reqUrl.length() > 0) {
                request.getSession().setAttribute("requrl", reqUrl);
            }



        }

        return loginCmd;
    }

	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		
		Map<Object, Object> dataMap = new HashMap<Object, Object>();

		HttpSession session = request.getSession();
			
		session.setAttribute("welcomePageUrl", request.getContextPath() + appConfiguration.getWelcomePageUrl() );
		session.setAttribute("logoUrl", appConfiguration.getLogoUrl());
		session.setAttribute("title", appConfiguration.getTitle());
		session.setAttribute("defaultLang", appConfiguration.getDefaultLang());
		
		
		
		
		String userId = (String)session.getAttribute("userId");

		return dataMap;
	
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

        String unauthurl = res.getString("UNAUTHORIZED_ACCESS");
        String APP_BASE_URL = res.getString("PROXYAUTH_BASE_URL");

		log.debug("LoginController:: onSubmit called.");
		
		System.out.println("onSubmit called.");
		
		LoginCommand loginCmd = (LoginCommand)command;
		String userId = loginCmd.getSubject().getUserId();
        List<String> oidList = new ArrayList<String>();
        String permitOverRide;
        String PO = loginCmd.getPo();
        // 90 DAYS
        int MAX_AGE = 60 * 60 * 24 * 90;
		
		HttpSession session = request.getSession();
		session.setAttribute("userId", userId);
        if (loginCmd.getSubject().getSsoToken().getToken() != null &&
            !loginCmd.getSubject().getSsoToken().getToken().contains("saml")){
		    session.setAttribute("token", loginCmd.getSubject().getSsoToken().getToken());
        }

        if (APP_BASE_URL != null && !APP_BASE_URL.isEmpty()) {

            String host = request.getHeader("Host");
            System.out.println("LoginController:: Host=" + host);

            if (host != null) {

                if (host.contains(":")) {
                    String[] t = host.split(":");
                    if (t != null) {
                        host = t[0];
                    }
                }
                System.out.println("LoginController:: updated Host=" + host);

                APP_BASE_URL =  APP_BASE_URL.replace("%", host);
            }


            System.out.println("LoginController:: APP_BASE_URL: " + APP_BASE_URL);

        }

		
		// get the menus that the user has permissions too
		List<Menu> menuList = navigationDataService.menuGroupByUser(rootMenu, loginCmd.getSubject().getUserId(), "en").getMenuList();
		session.setAttribute("permissions", menuList);
	

		// load information to put on to the welcome screen
		User usr = userMgr.getUserWithDependent(userId, true).getUser();


		List<Group> groupList = groupManager.getUserInGroups(userId).getGroupList();
		List<Role> roleList =  roleDataService.getUserRoles(userId).getRoleList();
		boolean answerStatus = challengeResponse.userAnserExists(userId);
        Login lg = loginManager.getPrimaryIdentity(userId).getPrincipal();


        System.out.println("Roles for user: " + roleList);


        String queryString = "&tk=" + loginCmd.getSubject().getSsoToken().getToken();
		
		String principal = loginCmd.getPrincipal();

        session.setAttribute("userObj",usr);


	    Login principalLg = loginManager.getPrimaryIdentity(loginCmd.getSubject().getUserId()).getPrincipal();
        session.setAttribute("login", principalLg.getId().getLogin());

        System.out.println("LoginController:: Identity =" + principalLg.getId().getLogin());




		if (loginCmd.getSubject().getResultCode() > 1) {
			if ( loginCmd.getSubject().getResultCode() == AuthenticationConstants.RESULT_SUCCESS_PASSWORD_EXP) {
                if (APP_BASE_URL != null && APP_BASE_URL.length() > 1) {
                    return new ModelAndView(new RedirectView(APP_BASE_URL + "/passwordChange.selfserve?hideRMenu=1&cd=pswdexp" + queryString, true));
                } else {
				 return new ModelAndView(new RedirectView("/passwordChange.selfserve?hideRMenu=1&cd=pswdexp" + queryString, true));
                }
			}
		}




        String reqUrl = (String) session.getAttribute("requrl");
        // check authorization for this users roles

        boolean authorized = false;

        System.out.println("Role list=" + roleList);

        if (resourceDataService.isUserAuthorizedByProperty(userId,"PROXY_URL",reqUrl) ) {
            authorized = true;
        }


        System.out.println("authorization=" + authorized);

        if (!authorized ) {
            
            System.out.println("Authorization failed - redirecting to=" + unauthurl);
            
            return new ModelAndView(new RedirectView(unauthurl, true));
        }


        String roleStr = null;

        if (roleList != null && !roleList.isEmpty()) {

            for ( Role r : roleList) {
                if (roleStr == null) {
                    roleStr = r.getId().getRoleId();
                }else {
                    roleStr = roleStr + "," + r.getId().getRoleId();
                }

            }
        }

        System.out.println("Authorization success  - build the parameter list" );

        StringBuffer headerString = new StringBuffer();
        headerString.append("userid=" + principalLg.getId().getLogin());
        headerString.append("&tkn=" + loginCmd.getSubject().getSsoToken().getToken());
        if (usr.getFirstName() != null && usr.getFirstName().length() > 0) {
            headerString.append("&firstname=" + usr.getFirstName());
        }else {
            headerString.append("&firstname=NA");
        }
        if (usr.getLastName() != null && usr.getLastName().length() > 0) {
            headerString.append("&secondname=" + usr.getLastName());
        }else {
            headerString.append("&secondname=NA");
        }
        headerString.append("&fullname=" + usr.getFirstName() + " " + usr.getLastName());
        if (roleStr != null && roleStr.length() > 0) {
            headerString.append("&role=" + roleStr );
        }else {
            headerString.append("&role=NO_ROLE");
        }


        log.info("Cookie string = " + headerString.toString() );

        session.setAttribute("iam_param_string", headerString.toString());

                // ------ Check if we are forwarding to the target app or to change password.
        // if change password, then dont set the cookie yet
        /* If the password was reset and the policy says change the password after a reset, then force a password reset */

        Policy policy = passwordService.getPasswordPolicy(configuration.getDefaultSecurityDomain(), principalLg.getId().getLogin(),
                configuration.getDefaultManagedSysId()).getPolicy();
        PolicyAttribute attr = policy.getAttribute("CHNG_PSWD_ON_RESET");
        boolean changePswdOnReset = false;
        if (attr.getValue1() != null && attr.getValue1().equalsIgnoreCase("1")) {
            if ( principalLg.getResetPassword() == 1) {
                changePswdOnReset = true;
            }
        }

        String baseUrl = null;
        String urlParam = null;
        if (reqUrl != null && reqUrl.length() > 0 && reqUrl.contains("?"))  {

            StringTokenizer tokenizer = new StringTokenizer(reqUrl,"?");
            if (tokenizer.hasMoreTokens()) {
                baseUrl =  tokenizer.nextToken();
                urlParam =    tokenizer.nextToken();

            }


        }

        if (changePswdOnReset) {
            System.out.println("Password reset flag is on" );
            String targetUrl = null;
            if (urlParam != null) {
              //  targetUrl = baseUrl + "?" + headerString.toString() + "&" + urlParam;
                targetUrl = baseUrl + "?" + urlParam;
            }else {
               // targetUrl = reqUrl + "?" + headerString.toString();
               targetUrl = reqUrl;
            }




            // reset the password
            //reqUrl = reqUrl + "?" + headerString.toString();
            session.setAttribute("requrl",  targetUrl );

            if (APP_BASE_URL != null && APP_BASE_URL.length() > 1) {
                return new ModelAndView(new RedirectView(APP_BASE_URL + "/passwordChange.selfserve?hideRMenu=1&cd=pswdreset"+ queryString, true));
            }else {

                return new ModelAndView(new RedirectView("/passwordChange.selfserve?hideRMenu=1&cd=pswdreset"+ queryString, true));
            }
        }


        // ------



        Cookie c = new Cookie("IAM_PARAM", headerString.toString());
        c.setPath("/");
        
        if ("true".equalsIgnoreCase(COOKIE_SECURE))  {
            c.setSecure(true);
        }
        
         if ( COOKIE_DOMAIN != null && COOKIE_DOMAIN.length() > 0 ) {
            c.setDomain(COOKIE_DOMAIN);

        }

        /*else {

            c.setDomain( request.getHeader("Host"));
        }
        */
        response.addCookie(c);


        if ( reqUrl != null && reqUrl.length() > 0 ) {
            // add the attributes that are needed to be passed.

            String targetUrl = null;
            if (urlParam != null) {
                //targetUrl = baseUrl + "?" + headerString.toString() + "&" + urlParam;
                targetUrl = baseUrl + "?" + urlParam;
            }else {
                //targetUrl = reqUrl + "?" + headerString.toString();
                targetUrl = reqUrl;
            }

            System.out.println("All is good - redirecting to - " + targetUrl );

            return new ModelAndView(new RedirectView(targetUrl, true));

        }


		
		// load the objects that are needed in the primary application
		
		ModelAndView mav = new ModelAndView(getSuccessView());

		return mav;
	}


    private boolean roleContains(String roleId, List<Role> roleList) {

        if (roleList == null || roleList.isEmpty()) {
            return false;
        }



        for (Role r : roleList) {
            if (r != null) {
                log.info("Checking Role name " + r);

                if (r.getId().getRoleId().equalsIgnoreCase(roleId)) {
                    return true;
                }
            }

        }
        return false;

    }


    private void addOid(List<String> oidList, String newOid) {

        for (String oid : oidList) {
            if (oid.equalsIgnoreCase(newOid)) {
                // found - its already in the list
                return;
            }

        }
        oidList.add(newOid);
    }

    private String getOidString(List<String> oidList) {
        StringBuffer oid = new StringBuffer();

        log.info("Oid String list = " + oidList);

        int ctr = 0;
        for ( String o : oidList) {

            if (ctr == 0) {
                oid.append( o );
            } else {
                oid.append("," + o);
            }
            ctr++;


        }
        if (oidList.isEmpty()) {
            return "NA";
        }
        return oid.toString();

    }



	public String getRootMenu() {
		return rootMenu;
	}


	public void setRootMenu(String rootMenu) {
		this.rootMenu = rootMenu;
	}


	public SecurityDomainDataService getSecDomainService() {
		return secDomainService;
	}


	public void setSecDomainService(SecurityDomainDataService secDomainService) {
		this.secDomainService = secDomainService;
	}


	public AppConfiguration getAppConfiguration() {
		return appConfiguration;
	}


	public void setAppConfiguration(AppConfiguration appConfiguration) {
		this.appConfiguration = appConfiguration;
	}


	public String getPublicLeftMenuGroup() {
		return publicLeftMenuGroup;
	}


	public void setPublicLeftMenuGroup(String publicLeftMenuGroup) {
		this.publicLeftMenuGroup = publicLeftMenuGroup;
	}


	public String getPublicRightMenuGroup1() {
		return publicRightMenuGroup1;
	}


	public void setPublicRightMenuGroup1(String publicRightMenuGroup1) {
		this.publicRightMenuGroup1 = publicRightMenuGroup1;
	}


	public String getPublicRightMenuGroup2() {
		return publicRightMenuGroup2;
	}


	public void setPublicRightMenuGroup2(String publicRightMenuGroup2) {
		this.publicRightMenuGroup2 = publicRightMenuGroup2;
	}


	public String getLeftMenuGroup() {
		return leftMenuGroup;
	}


	public void setLeftMenuGroup(String leftMenuGroup) {
		this.leftMenuGroup = leftMenuGroup;
	}


	public String getRightMenuGroup1() {
		return rightMenuGroup1;
	}


	public void setRightMenuGroup1(String rightMenuGroup1) {
		this.rightMenuGroup1 = rightMenuGroup1;
	}


	public String getRightMenuGroup2() {
		return rightMenuGroup2;
	}


	public void setRightMenuGroup2(String rightMenuGroup2) {
		this.rightMenuGroup2 = rightMenuGroup2;
	}



	

	public ChallengeResponseService getChallengeResponse() {
		return challengeResponse;
	}


	public void setChallengeResponse(ChallengeResponseService challengeResponse) {
		this.challengeResponse = challengeResponse;
	}


	public NavigatorDataWebService getNavigationDataService() {
		return navigationDataService;
	}


	public void setNavigationDataService(
			NavigatorDataWebService navigationDataService) {
		this.navigationDataService = navigationDataService;
	}


	public GroupDataWebService getGroupManager() {
		return groupManager;
	}


	public void setGroupManager(GroupDataWebService groupManager) {
		this.groupManager = groupManager;
	}


	public RoleDataWebService getRoleDataService() {
		return roleDataService;
	}


	public void setRoleDataService(RoleDataWebService roleDataService) {
		this.roleDataService = roleDataService;
	}


	public UserDataWebService getUserMgr() {
		return userMgr;
	}


	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}


	public LoginDataWebService getLoginManager() {
		return loginManager;
	}


	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}


	public PasswordConfiguration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(PasswordConfiguration configuration) {
		this.configuration = configuration;
	}


	public PasswordWebService getPasswordService() {
		return passwordService;
	}


	public void setPasswordService(PasswordWebService passwordService) {
		this.passwordService = passwordService;
	}


	public String getPublicRightMenuGroup3() {
		return publicRightMenuGroup3;
	}


	public void setPublicRightMenuGroup3(String publicRightMenuGroup3) {
		this.publicRightMenuGroup3 = publicRightMenuGroup3;
	}


	public String getRightMenuGroup3() {
		return rightMenuGroup3;
	}


	public void setRightMenuGroup3(String rightMenuGroup3) {
		this.rightMenuGroup3 = rightMenuGroup3;
	}




    public RequestWebService getProvRequestService() {
        return provRequestService;
    }

    public void setProvRequestService(RequestWebService provRequestService) {
        this.provRequestService = provRequestService;
    }

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }

    public String getExtendController() {
        return extendController;
    }

    public void setExtendController(String extendController) {
        this.extendController = extendController;
    }

    public String getCookiepath() {
        return cookiepath;
    }

    public void setCookiepath(String cookiepath) {
        this.cookiepath = cookiepath;
    }


    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public String getKaroscookie() {
        return karoscookie;
    }

    public void setKaroscookie(String karoscookie) {
        this.karoscookie = karoscookie;
    }


}
