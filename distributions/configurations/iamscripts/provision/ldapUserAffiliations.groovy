import java.util.ArrayList;
import java.util.List;
import org.openiam.idm.srvc.org.dto.Organization;
import org.springframework.context.support.ClassPathXmlApplicationContext

def orgManager = context.getBean("orgManager")

String orgBaseDN = "ou=affiliations,dc=gtawestdir,dc=com";

List<String> orgAffiliationList = new ArrayList<String>();
def List<Organization> affiliationList = user.userAffiliations;
println("user affilations =" + affiliationList);

if (affiliationList != null) {
	if (affiliationList.size() > 0)  {
		for (Organization o : affiliationList) {
			// get the name of the org
			
			Organization affiliationOrg = orgManager.getOrganization(o.orgId)
			
			println("- adding affiliation:" + "cn=" + affiliationOrg.organizationName + "," + orgBaseDN);
			
			orgAffiliationList.add("cn=" + affiliationOrg.organizationName + "," + orgBaseDN);
			
		}
		output = orgAffiliationList;
	}else {
		output = null;
	}
}else {
	output = null;
}

