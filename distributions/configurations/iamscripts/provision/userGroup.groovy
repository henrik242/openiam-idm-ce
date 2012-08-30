import java.util.ArrayList;
import java.util.List; 
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.groovy.helper.ServiceHelper;
import org.openiam.base.AttributeOperationEnum;

import org.openiam.base.BaseAttribute;
import org.openiam.base.BaseAttributeContainer;

def GroupDataWebService groupService = ServiceHelper.groupService();
def Group grp;

String groupBaseDN = ",ou=group,ou=people," + matchParam.baseDn;

def List<Group> groupList = user.getMemberOfGroups();

BaseAttributeContainer attributeContainer = new BaseAttributeContainer();

if (groupList != null) {
	if (groupList.size() > 0)  {
		for (Group r : groupList) {
			String groupName = r.grpName;
			
			if (groupName == null) {
			
				grp =  groupService.getGroup(r.grpId).getGroup();
        		groupName = grp.grpName;
        
			}
			
			println("Adding group id  " + r.grpId + " --> " + (groupName + groupBaseDN));
			
			String qualifiedGroupName = "cn=" + groupName +  groupBaseDN
			
			attributeContainer.getAttributeList().add(new BaseAttribute(qualifiedGroupName, qualifiedGroupName, r.operation));
			
			
		}
		//output = roleStrList;
		output = attributeContainer;
	}else {
		output = null;
	}
}else {
	output = null;
}
