import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.groovy.helper.ServiceHelper;

def OrganizationDataService orgService = ServiceHelper.orgService();
def Organization org;

if (user.companyId != null) {
	org = orgService.getOrganization(user.companyId);
	output = org.getOrganizationName();
}else{

	output=null
}