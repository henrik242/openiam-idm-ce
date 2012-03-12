package org.openiam.spml2.spi.linux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2/26/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinuxGroups {
    private List<String> groups;
    private String groupsAsString = null;

    public LinuxGroups(List<String> groupArg) {
        populateStringAndGroupList(groupArg);
    }

    public LinuxGroups(String serverGroupFileText) {
        String[] splitGroups = serverGroupFileText.split("\\n");
        ArrayList<String> parsedGroups = new ArrayList<String>();
                
        for (String s: splitGroups)
            parsedGroups.add(s.split(":", 2)[0]);
        
        populateStringAndGroupList(parsedGroups);
    }
    
    public String getGroupsAsString() {
        return  groupsAsString;
    }

    public String getListServerGroupsCommand() {
        return "cat /etc/groups";
    }

    /***
     * Returns true if the argument's groups are a subset of this group
     * @param g
     * @return true if g is in this, false otherwise
     */
    public boolean contains(LinuxGroups g) {
        return isSubset(g, this);
    }

    /***
     * Returns true if this group is a subset of the given group
     * @param g
     * @return true if this is in g, false otherwise
     */
    public boolean isContained(LinuxGroups g) {
        return isSubset(this, g);
    }
    
    
    private boolean isSubset(LinuxGroups g, LinuxGroups g1) {
        return g1.groups.containsAll(g.groups);
    }
    

    private void populateStringAndGroupList(List<String> groupArg) {
        groups = new ArrayList<String>();

        StringBuffer sb = new StringBuffer();
        if (groupArg != null && groupArg.size() > 0) {

            for (String g : groupArg) {
                String g_trim = g.trim();
                groups.add(g_trim);
                sb.append(g_trim);
                sb.append(',');
            }

            sb.deleteCharAt(sb.length() - 1); // last comma
        }

        groupsAsString = sb.toString();
    }
}
