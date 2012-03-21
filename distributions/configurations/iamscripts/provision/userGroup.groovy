import java.util.List;
import org.openiam.idm.srvc.grp.dto.Group;

def List<Group> groupList = user.getMemberOfGroups();
println("user groups =" + groupList);

StringBuilder groupString = new StringBuilder();
for (Group g: groupList) {
    groupString.append(g.getGrpId()).append('\n');
}
output=groupString.toString();

