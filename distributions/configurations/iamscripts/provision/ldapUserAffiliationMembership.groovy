import org.openiam.idm.srvc.org.dto.Organization;
import java.util.ArrayList;
import java.util.List;

String orgBaseDN = "ou=affiliations,dc=gtawestdir,dc=com";
List<String> orgStrList = new ArrayList<String>();

def orgManager = context.getBean("orgManager")

if (user.getAttribute("org") !=null) {

	if ( user.getAttribute("org").value != null &&  user.getAttribute("org").value.length() > 0){
		orgStrList.add("cn=" + user.getAttribute("org").value + "," + orgBaseDN);
	
		output= orgStrList;
	}
}else {
output = null;
			
}

