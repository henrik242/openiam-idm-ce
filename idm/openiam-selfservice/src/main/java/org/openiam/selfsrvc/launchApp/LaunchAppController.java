package org.openiam.selfsrvc.launchApp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Controller to retrieve user information. The information is presented through a readonly view.
 * @author suneet
 *
 */
public class LaunchAppController extends AbstractController {

	private static final Log log = LogFactory.getLog(LaunchAppController.class);
	private String successView;

    protected ResourceDataService resourceDataService;

	public LaunchAppController() {
		super();
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		

        HttpSession session =  req.getSession();
        String userId = (String)session.getAttribute("userId");
        List<Resource> launchAppList = new LinkedList<Resource>();

       List<Resource> resourceList =resourceDataService.getUserResourcesByType(userId, "WEB-APP");

        if (resourceList != null && !resourceList.isEmpty()) {
            for (Resource r : resourceList) {

                Resource resource = resourceDataService.getResource(r.getResourceId());


                Set<ResourceProp> propList=  resource.getResourceProps();
                for (ResourceProp p : propList) {
                      if ( "PROXY_URL".equalsIgnoreCase(p.getName())) {
                          resource.setURL(p.getPropValue());
                      }
                }
                launchAppList.add(resource);
            }
        }

		ModelAndView mav = new ModelAndView(successView);
		mav.addObject("resourceList", launchAppList);

		
		return mav;

	}

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }
}
