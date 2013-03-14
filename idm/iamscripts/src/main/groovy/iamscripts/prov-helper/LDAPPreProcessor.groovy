import java.util.Map;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.PreProcessor;
import org.openiam.provision.service.ProvisioningConstants;


public class LDAPPreProcessor implements PreProcessor {
	
	public int addUser(ProvisionUser user, Map<String, Object> bindingMap) {
		
		println("LDAP PreProcessor: AddUser called.");
		println("PreProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}

	
	
		return ProvisioningConstants.SUCCESS;
	}
	
    public int modifyUser(ProvisionUser user, Map<String, Object> bindingMap){
    	
   		
    
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	
    public int deleteUser(ProvisionUser user, Map<String, Object> bindingMap){
    
     
    	return ProvisioningConstants.SUCCESS;
	}
	
    public int setPassword( Map<String, Object> bindingMap){
    
  		
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	

    
}
