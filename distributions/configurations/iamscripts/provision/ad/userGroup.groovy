import java.util.ArrayList;
import java.util.List; 
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.groovy.helper.ServiceHelper;

def GroupDataWebService groupService = ServiceHelper.groupService();
def Group grp;

String groupBaseDN = ",OU=dev,DC=iamdev,DC=local";

List<String> roleStrList = new ArrayList<String>();
def List<Group> groupList = user.getMemberOfGroups();

if (groupList != null) {
	if (groupList.size() > 0)  {
		for (Group r : groupList) {
			String groupName = r.grpName;
			
			if (groupName == null) {
			
				grp =  groupService.getGroup(r.grpId).getGroup();
        		groupName = grp.grpName;
        
			}
			
			println("Adding group id  " + r.grpId + " --> " + groupName);
			
			roleStrList.add("cn=" + groupName +  groupBaseDN);
			
		}
		output = roleStrList;
	}else {
		output = null;
	}
}else {
	output = null;
}

