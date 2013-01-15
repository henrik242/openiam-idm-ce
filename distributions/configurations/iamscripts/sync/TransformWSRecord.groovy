 package groovy
import java.awt.datatransfer.StringSelection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.service.AbstractTransformScript;
import org.openiam.idm.srvc.synch.service.TransformScript;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.idm.srvc.user.dto.UserStatusEnum
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;

import antlr.StringUtils;


public class TransformWSRecord extends AbstractTransformScript {

	public int execute(LineObject rowObj, ProvisionUser pUser) {


		System.out.println("** 2-Transform object called. **");


		println("Is New User:" + isNewUser)
		println("User Object:" + user)
		println("PrincipalList: " + principalList)
		println("User Roles:" + userRoleList)

		populateObject(rowObj, pUser);

		if (isNewUser) {
			pUser.setUserId(null);
		}
		pUser.setStatus(UserStatusEnum.ACTIVE);
		pUser.securityDomain = "0"

		// Set default role
		List<Role> roleList = new ArrayList<Role>();
		RoleId id = new RoleId("USR_SEC_DOMAIN", "END_USER");
		Role r = new Role();
		r.setId(id);
		roleList.add(r);

		pUser.setMemberOfRoles(roleList);

		return TransformScript.NO_DELETE;
	}

	private void populateObject(LineObject rowObj, ProvisionUser pUser) {
		Attribute attrVal = null;
		DateFormat df =  new SimpleDateFormat("MM-dd-yyyy");

		Map<String,Attribute> columnMap =  rowObj.getColumnMap();
		//CUSTOM SECTION - START
		// IN columnMap.get("<tagName>");
		// <tagName> should be names same as in WebServiceCommand#List <String> tags
		// "ns4:firstName",
		//"ns4:lastName",
		//"ns4:status",
		//"ns4:employeeId",
		//"ns4:lastUpdate",
		//"ns4:userId"
		attrVal = columnMap.get("ns4:firstName");
		if (attrVal != null) {
			pUser.setFirstName(attrVal.getValue());
		}

		attrVal = columnMap.get("ns4:lastName");
		if (attrVal != null) {
			pUser.setLastName(attrVal.getValue());
		}

		attrVal = columnMap.get("ns4:status");
		if (attrVal != null) {
			pUser.status = UserStatusEnum.valueOf( attrVal.getValue().toUpperCase());
		}
		
		attrVal = columnMap.get("ns4:employeeId");
		if (attrVal != null) {
			pUser.setEmployeeId(attrVal.getValue()) ;
		}
		
		attrVal = columnMap.get("ns4:lastUpdate");
		if (attrVal != null && attrVal.getValue()!=null && !attrVal.getValue().isEmpty()) {
			pUser.setLastUpdate(Date.valueOf(attrVal.getValue())) ;
		}
		
		attrVal = columnMap.get("ns4:userId");
		if (attrVal != null) {
			pUser.setUserId(attrVal.getValue()) ;
		}
		//CUSTOM SECTION - END
	}


	private void addAttribute(ProvisionUser user, Attribute attr) {

		if (attr != null && attr.getName() != null && attr.getName().length() > 0) {
			UserAttribute userAttr = new UserAttribute(attr.getName(), attr.getValue());
			user.getUserAttributes().put(attr.getName(), userAttr);
			println("Attribute added to the user object.");
		}
	}

}


