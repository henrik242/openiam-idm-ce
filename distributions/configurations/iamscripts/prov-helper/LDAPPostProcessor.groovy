import java.util.Map;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.PostProcessor;
import org.openiam.provision.service.ProvisioningConstants;


public class LDAPPreProcessor implements PostProcessor {
	
	public int addUser(ProvisionUser user, Map<String, Object> bindingMap, boolean success) {
	
		println("Postprocessor: AddUser called.");
		println("PostProcessor: Provisioning to resource was successful?" + success)
		println("PostProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
	
		return ProvisioningConstants.SUCCESS;
	}
	
    public int modifyUser(ProvisionUser user, Map<String, Object> bindingMap, boolean success){
    
    	println("Postprocessor: ModifyUser called.");
		println("PostProcessor: Provisioning to resource was successful?" + success)
		println("PostProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
    
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	
    public int deleteUser(ProvisionUser user, Map<String, Object> bindingMap, boolean success){
    
        println("Postprocessor: DeleteUser called.");
		println("PostProcessor: Provisioning to resource was successful?" + success)
		println("PostProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
    
    	return ProvisioningConstants.SUCCESS;
	}
	
    public int setPassword( Map<String, Object> bindingMap, boolean success){
    	println("PostProcessor: SetPassword called.");
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	

    
}
