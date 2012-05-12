import java.util.Map;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.PreProcessor;
import org.openiam.provision.service.ProvisioningConstants;
import org.openiam.provision.service.AbstractPreProcessor;


public class ProvisionServicePreProcessor extends AbstractPreProcessor {
	
	public int addUser(ProvisionUser user, Map<String, Object> bindingMap) {
		
		println("ProvisionServicePreProcessor: AddUser called.");
		println("ProvisionServicePreProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}

	
	
		return ProvisioningConstants.SUCCESS;
	}
	
    public int modifyUser(ProvisionUser user, Map<String, Object> bindingMap){
    	
    	println("ProvisionServicePreProcessor: ModifyUser called.");
		println("ProvisionServicePreProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
    
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	
    public int deleteUser(ProvisionUser user, Map<String, Object> bindingMap){
    
        println("ProvisionServicePreProcessor: DeleteUser called.");
		println("ProvisionServicePreProcessor: User=" + user.toString());
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
    
    	return ProvisioningConstants.SUCCESS;
	}
	
    public int setPassword( PasswordSync passwordSync, Map<String, Object> bindingMap){
    
     	println("ProvisionServicePreProcessor: SetPassword called.");
		println("Show binding map");
		
		for (Map.Entry<String, Object> entry : bindingMap.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
				println("- Key=" + key + "  value=" + value.toString() );
		}
		
    
    	return ProvisioningConstants.SUCCESS;
    
	}
	

    
}
