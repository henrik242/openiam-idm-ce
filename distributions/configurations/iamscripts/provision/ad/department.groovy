import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openiam.idm.srvc.org.dto.Organization;

def orgManager = context.getBean("orgManager")


if (user.deptCd == null || user.deptCd.length() == 0)

 output=null

else {
	Organization orgObject = orgManager.getOrganization(user.deptCd );
	if (orgObject != null) {
		output = orgObject.organizationName
	}

 output = null;

}