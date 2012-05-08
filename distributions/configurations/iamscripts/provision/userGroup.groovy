import java.util.ArrayList;
import java.util.List;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.groovy.helper.ServiceHelper;
import org.openiam.base.AttributeOperationEnum;

def GroupDataWebService groupService = ServiceHelper.groupService();
def Group grp;

String groupBaseDN = ",ou=group," + matchParam.baseDn;

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

            if (r.operation == null) {

                roleStrList.add("cn=" + groupName +  groupBaseDN);

            }else {
                if (r.operation != AttributeOperationEnum.DELETE ) {
                    roleStrList.add("cn=" + groupName +  groupBaseDN);
                }
            }



        }
        output = roleStrList;
    }else {
        output = null;
    }
}else {
    output = null;
}



