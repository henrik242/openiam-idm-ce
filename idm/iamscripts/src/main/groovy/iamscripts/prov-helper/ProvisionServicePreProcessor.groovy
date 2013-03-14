import java.util.*;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.PreProcessor;
import org.openiam.provision.service.ProvisioningConstants;
import org.openiam.provision.service.AbstractPreProcessor;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.base.AttributeOperationEnum;
import org.springframework.context.ApplicationContext;

import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.res.service.ResourceDataService;


/**
* Pre-processor script that is used with the Provisioning service.
*/
public class ProvisionServicePreProcessor extends AbstractPreProcessor {


	public int addUser(ProvisionUser user, Map<String, Object> bindingMap) {

		// context to look up spring beans - commonly used beans. Included to help development

	 ApplicationContext context = (ApplicationContext)bindingMap.get("context");
   OrganizationDataService orgManager = (OrganizationDataService)context.getBean("orgManager");
   RoleDataService roleDataService = (RoleDataService)context.getBean("roleDataService");	
	 LoginDataService loginService = (LoginDataService)context.getBean("loginManager");
	 ResourceDataService resourceDataService = (ResourceDataService)context.getBean("resourceDataService");
	 
		
		println("ProvisionServicePreProcessor: AddUser called.");

		
	
		return ProvisioningConstants.SUCCESS;
	}
	
    public int modifyUser(ProvisionUser user, Map<String, Object> bindingMap){
    	// context to look up spring beans
		ApplicationContext context = (ApplicationContext)bindingMap.get("context");
		
    	println("ProvisionServicePreProcessor: ModifyUser called.");

		
		
    
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	
	
	
    public int deleteUser(ProvisionUser user, Map<String, Object> bindingMap){
    
    	// context to look up spring beans
		ApplicationContext context = (ApplicationContext)bindingMap.get("context");
		
        println("ProvisionServicePreProcessor: DeleteUser called.");
		
		
    
    	return ProvisioningConstants.SUCCESS;
	}
	
    public int setPassword( PasswordSync passwordSync, Map<String, Object> bindingMap){
    
    	ApplicationContext context = (ApplicationContext)bindingMap.get("context");
    	
    
	
		
    	return ProvisioningConstants.SUCCESS;
    
	}
	
	private void showBindingMap( Map<String, Object> bindingMap){
    
    	// context to look up spring beans
		
    	
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
	
    
	}
	
	
	

    
}
