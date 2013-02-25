package org.openiam.selfsrvc.pswd;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openiam.base.ExtendController;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.script.ScriptIntegration;
import org.openiam.selfsrvc.helper.ScriptEngineUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.service.ProvisionService;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for the new hire form.
 * @author suneet
 *
 */
public class PasswordChangeController extends CancellableFormController {


	protected UserDataWebService userMgr;
	protected LoginDataWebService loginManager;
	protected PasswordConfiguration configuration;
	protected ProvisionService provisionService;
    protected String extendController;
	String defaultDomainId;
	String menuGroup;

    protected String pwsdResetCancelView;
    protected AuthenticationService authenticate;
	
	private static final Log log = LogFactory.getLog(PasswordChangeController.class);

    ResourceBundle res = ResourceBundle.getBundle("datasource");

    String COOKIE_SECURE = res.getString("IAM_CookieSecure");
    String COOKIE_HTTP_ONLY = res.getString("IAM_CookieHttpOnly");
    String HTTPS_ONLY = res.getString("HTTPS_ONLY");
    String COOKIE_DOMAIN = res.getString("COOKIE_DOMAIN");

    String APP_BASE_URL = res.getString("PROXYAUTH_BASE_URL");

	
	public PasswordChangeController() {
		super();
	}
	



	protected Object formBackingObject(HttpServletRequest request)		throws Exception {
		
		String rMenu = request.getParameter("hideRMenu");
		String header = request.getParameter("hideHeader");
		String cd = request.getParameter("cd"); 
		
		
		if (cd != null) {
			if (cd.equalsIgnoreCase("pswdreset")) {
				request.setAttribute("msg", "Your account has been reset. Please change your password.");
			}
			if (cd.equalsIgnoreCase("pswdexp")) {
				request.setAttribute("msg", "Your password has expired. Please change your password.");
			}
		}
		
		if (rMenu != null && rMenu.length() > 0) {		
			request.setAttribute("hideRMenu","1");
		}
		if (header != null && header.length() > 0) {
			request.setAttribute("hideHeader","1");
		}
		
		PasswordChangeCommand pswdChangeCmd = new PasswordChangeCommand();	
				
		HttpSession session =  request.getSession();
		String principal = (String)session.getAttribute("login");

        System.out.println("PasswordChangeController:: login = " + principal);
		
		pswdChangeCmd.setPrincipal(principal);

        System.out.println("PasswordChangeController:: principal set to Password command object" );
		
		return pswdChangeCmd;
	}

	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.info("onSubmit called.");
		
		String userId = (String)request.getSession().getAttribute("userId");



		PasswordChangeCommand pswdChangeCmd =(PasswordChangeCommand)command;
		
		// get objects from the command object
		String principal = pswdChangeCmd.getPrincipal();
		String password = pswdChangeCmd.getPassword();
		
		// update the password in the openiam repository of the primary id
		String managedSysId = configuration.getDefaultManagedSysId();
		String secDomainId = configuration.getDefaultSecurityDomain();
		
        // custom processing to determine which screen to show
        String token =  (String)request.getSession().getAttribute("token");

        PasswordSync passwordSync = new PasswordSync("CHANGE PASSWORD", managedSysId, password,
					principal, userId, secDomainId, "SELFSERVICE", false );





			
		// sync the password
		System.out.println("-Sync password start");
		

			
		provisionService.setPassword(passwordSync);


        String headerString = (String)request.getSession().getAttribute("iam_param_string");

        Cookie c = new Cookie("IAM_PARAM", headerString);
        c.setPath("/");

        if ("true".equalsIgnoreCase(COOKIE_SECURE))  {
            c.setSecure(true);
        }

        if ( COOKIE_DOMAIN != null && COOKIE_DOMAIN.length() > 0 ) {
            c.setDomain(COOKIE_DOMAIN);

        }


        response.addCookie(c);


        String reqUrl = (String) request.getSession().getAttribute("requrl");
        if ( reqUrl != null && reqUrl.length() > 0 ) {
            // add the attributes that are needed to be passed.



            return new ModelAndView(new RedirectView(reqUrl, true));

        }

			
		log.info("-Sync password complete");
				
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("pswdChangeCmd",pswdChangeCmd);
		
		
		return mav;
	}

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        PasswordChangeCommand pswdChangeCmd = (PasswordChangeCommand)command;
        System.out.println("Cd = " + pswdChangeCmd.getCd());
        if (pswdChangeCmd.getCd()  != null) {
            if ("pswdreset".equalsIgnoreCase(pswdChangeCmd.getCd())) {
                if ( pswdChangeCmd.getUserId() != null && !pswdChangeCmd.getUserId().isEmpty() ) {
                    authenticate.globalLogout(pswdChangeCmd.getUserId());
                }

                System.out.println("Redirect = " + pwsdResetCancelView);
                                // 2 = pswd change cancelled.
                String qry = pwsdResetCancelView + "?expire=2";
                

                return new ModelAndView(new RedirectView(APP_BASE_URL + qry,true));
            }
        }

        return new ModelAndView(new RedirectView(APP_BASE_URL + this.getCancelView(),true));

    }




	public PasswordConfiguration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(PasswordConfiguration configuration) {
		this.configuration = configuration;
	}


	public ProvisionService getProvisionService() {
		return provisionService;
	}


	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
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

    public String getExtendController() {
        return extendController;
    }

    public void setExtendController(String extendController) {
        this.extendController = extendController;
    }

    public String getPwsdResetCancelView() {
        return pwsdResetCancelView;
    }

    public void setPwsdResetCancelView(String pwsdResetCancelView) {
        this.pwsdResetCancelView = pwsdResetCancelView;
    }

    public AuthenticationService getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(AuthenticationService authenticate) {
        this.authenticate = authenticate;
    }
}
