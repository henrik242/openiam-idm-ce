import java.util.ArrayList;
import java.util.List;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.User;

List<String> roleStrList = new ArrayList<String>();
def List<Role> roleList = user.getMemberOfRoles();
println("user roles =" + roleList);

if (roleList != null) {
	if (roleList.size() > 0)  {
		for (Role r : roleList) {
			roleStrList.add(r.id.serviceId + "*" + r.id.roleId);
		}
		output = roleStrList;
	}else {
		output = null;
	}
}else {
	output = null;
}

