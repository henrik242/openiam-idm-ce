import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openiam.idm.srvc.org.dto.Organization;


def orgManager = context.getBean("orgManager")

output = null;

			if (user.companyId != null && user.companyId.length() > 0) {
				Organization orgObject = orgManager.getOrganization(user.companyId );
				if (orgObject != null) {
					output = orgObject.organizationName
				}
			}
