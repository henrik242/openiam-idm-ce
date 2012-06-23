package org.openiam.webadmin.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ExtendController;
import org.openiam.idm.srvc.cd.dto.ReferenceData;
import org.openiam.idm.srvc.cd.service.ReferenceDataService;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.loc.dto.Location;
import org.openiam.idm.srvc.loc.ws.LocationDataWebService;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.AsynchUserProvisionService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.openiam.webadmin.admin.AppConfiguration;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Controller for the new hire form.
 * @author suneet
 *
 */
public class BulkProvisioningController extends AbstractWizardFormController {

    private GroupDataWebService groupManager;
    private RoleDataWebService roleDataService;
    private NavigatorDataWebService navigatorDataService;
    private LocationDataWebService locationService;

    private AsynchUserProvisionService provService;
    private OrganizationDataService orgManager;

    private ReferenceDataService refDataService;
    private ResourceDataService resourceDataService;
    private MetadataWebService metadataService;


	protected AppConfiguration configuration;
    String resourceType;


	private static final Log log = LogFactory.getLog(BulkProvisioningController.class);


	public BulkProvisioningController() {
		super();
	}
	

	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true) );
	}
	
	@Override
	protected void validatePage(Object command, Errors errors, int page) {
		log.debug("Validate page:" + page);
        BulkProvsioningValidator validator = (BulkProvsioningValidator)getValidator();
		switch (page) {
		case 0: 
			validator.validateNewUserType(command, errors);
			break;
		case 1:
			validator.validateNewUserForm(command, errors);
			break;
		}
		
	}
	
	
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException arg3)
			throws Exception {
		
		log.debug("In processFinish..");

        BulkProvisioningCommand newUserCmd =(BulkProvisioningCommand)command;
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");



        return new ModelAndView("pub/confirm");

	        
	}






	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		Map model = new HashMap();   
        model.put("message", "Request to reset the password has been canceled");   
        return new ModelAndView("pub/cancel");   
        
	}
	
	

	
	@Override
	protected Map referenceData(HttpServletRequest request, int page) throws Exception {
		
		log.debug("in referenceData");
		
		switch (page) {
		case 0:
			return loadUserSearchValues(request);
		case 1:
			return loadTargetSystemValue(request);

		}
		return null;
	}

	protected Map loadUserSearchValues(HttpServletRequest request) {
		log.debug("referenceData:loadUserTypes called.");
		
		HttpSession session =  request.getSession();
		
		log.info("User type category =" + configuration.getUserCategoryType());
		

		Map model = new HashMap();
        model.put("orgList", orgManager.getOrganizationList(null,"ACTIVE")); // orgManager.getTopLevelOrganizations() );
        // get the divisions
        model.put("divList", orgManager.allDivisions(null));
        // load the department list
        model.put("deptList",orgManager.allDepartments(null));

        model.put("elementList",  getComleteMetadataElementList());

	
		return model;
		
	}

	
	protected Map loadTargetSystemValue(HttpServletRequest request) {
		log.info("referenceData:loadUserInformation called.");
		
		HttpSession session =  request.getSession();

        List<Resource> resourceList = resourceDataService.getResourcesByType(resourceType);
        Map model = new HashMap();
        if (resourceList != null ) {
            model.put("resourceList", resourceList);
        }

        model.put("roleList", roleDataService.getAllRoles().getRoleList());


		return model;
		
	}



    private MetadataElement[]  getComleteMetadataElementList()  {
        log.info("getUserMetadataTypes called.");

        return metadataService.getAllElementsForCategoryType("USER_TYPE").getMetadataElementAry();

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


	public NavigatorDataWebService getNavigatorDataService() {
		return navigatorDataService;
	}


	public void setNavigatorDataService(NavigatorDataWebService navigatorDataService) {
		this.navigatorDataService = navigatorDataService;
	}




	public OrganizationDataService getOrgManager() {
		return orgManager;
	}


	public void setOrgManager(OrganizationDataService orgManager) {
		this.orgManager = orgManager;
	}


	public LocationDataWebService getLocationService() {
		return locationService;
	}


	public void setLocationService(LocationDataWebService locationService) {
		this.locationService = locationService;
	}


	public ReferenceDataService getRefDataService() {
		return refDataService;
	}


	public void setRefDataService(ReferenceDataService refDataService) {
		this.refDataService = refDataService;
	}


	public AppConfiguration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(AppConfiguration configuration) {
		this.configuration = configuration;
	}


	public ResourceDataService getResourceDataService() {
		return resourceDataService;
	}


	public void setResourceDataService(ResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}




    public AsynchUserProvisionService getProvService() {
        return provService;
    }

    public void setProvService(AsynchUserProvisionService provService) {
        this.provService = provService;
    }

    public MetadataWebService getMetadataService() {
        return metadataService;
    }

    public void setMetadataService(MetadataWebService metadataService) {
        this.metadataService = metadataService;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
