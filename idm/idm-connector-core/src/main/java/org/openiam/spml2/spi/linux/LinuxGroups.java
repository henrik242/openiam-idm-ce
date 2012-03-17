package org.openiam.spml2.spi.linux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2/26/12
 * Time: 8:09 PM
 */
public class LinuxGroups {
    private List<String> groups;
    private String groupsAsString = null;

    public LinuxGroups(List<String> groupArg) {
        populateStringAndGroupList(groupArg);
    }

    public LinuxGroups(String serverResultText) {
        ArrayList<String> parsedGroups = new ArrayList<String>();

        if (serverResultText != null) {
            String[] splitGroups = serverResultText.split("\\n");
            Collections.addAll(parsedGroups, splitGroups);
        }

        populateStringAndGroupList(parsedGroups);
    }


    public String getAddGroupsCommand() {
        StringBuilder cmd = new StringBuilder();
        for (String g : groups) {
            cmd.append("groupadd ");
            cmd.append(g);
            cmd.append(";");
        }

        return cmd.toString();
    }


    public String getDeleteGroupsCommand() {
        StringBuilder cmd = new StringBuilder();
        for (String g : groups) {
            cmd.append("groupdel ");
            cmd.append(g);
            cmd.append(";");
        }

        return cmd.toString();
    }


    /**
     * Command that checks which groups do not exist on the server
     *
     * @return The command will return the non-existent groups
     */
    public String getGroupsNotOnServerCommand() {
        String cmd = null;

        if (groups != null && groups.size() > 0) {
            StringBuilder groupsWithNL = new StringBuilder();

            for (String g : groups) {
                groupsWithNL.append(g);
                groupsWithNL.append("\\n");
            }

            cmd = "echo -e \"" + groupsWithNL.toString() + "\" | grep -v -F \"`awk -F: '{print $1}' /etc/group`\"";
        }

        return cmd;
    }


    public String getGroupsAsCommaSeparatedString() {
        return groupsAsString;
    }

    public boolean hasGroups() {
        return groups.size() > 0;
    }


    private void populateStringAndGroupList(List<String> groupArg) {
        groups = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        if (groupArg != null && groupArg.size() > 0) {
            for (String g : groupArg) {
                String g_trim = g.trim();
                if (g_trim.length() > 0) {
                    groups.add(g_trim);
                    sb.append(g_trim);
                    sb.append(',');
                }
            }

            if (sb.length() > 0)
                sb.deleteCharAt(sb.length() - 1); // last comma
        }

        groupsAsString = sb.toString();
    }
}
