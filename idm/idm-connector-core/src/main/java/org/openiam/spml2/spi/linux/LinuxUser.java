package org.openiam.spml2.spi.linux;

import java.util.Map;

public class LinuxUser {
    private LinuxGroups groups;
    private String login;
    private String password;

    private String name;
    private String surname;
    
    private String homePhone;
    private String workPhone;
    
    private String roomNumber;
    
    private String expireDate;
    private String daysBeforeDisable;
    
    
    public LinuxUser(LinuxGroups groups, String login, String password, String name, String surname, String homePhone, String workPhone, String roomNumber, String expireDate, String daysBeforeDisable) throws Exception {
        if (login == null)
            throw new Exception("Login cannot be null!");

        this.groups = groups;
        this.login = login;         // mandatory!
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.homePhone = homePhone;
        this.workPhone = workPhone;
        this.roomNumber = roomNumber;
        this.expireDate = expireDate;
        this.daysBeforeDisable = daysBeforeDisable;
    }
    
    public LinuxUser(LinuxGroups groups, Map<String, String> a) throws Exception {
        this(groups, a.get("login"), a.get("password"), a.get("name"), a.get("surname"), a.get("homePhone"), a.get("workPhone"), a.get("roomNumber"), a.get("expireDate"), a.get("daysToDisable"));
    }
    
    
    public String getUserDetailsCommand() {
        // chfn [-f full_name] [-r room_no] [-w work_ph] [-h home_ph] [-o other]  [user]
        StringBuffer cmd = new StringBuffer();
        cmd.append("chfn ");
        cmd.append(notBlank("f", (name + " " + surname).trim(), true));
        cmd.append(notBlank("r", roomNumber, true));
        cmd.append(notBlank("w", workPhone, true));
        cmd.append(notBlank("h", homePhone, true));
        cmd.append(login);

        return cmd.toString();
    }

    // TODO: DO NOT PASS password as an argument, as this may be visible by other users on the systems who call ps
    public String getUserAddCommand() {
        // useradd [-c comment] [-d home_dir] [-e expire_date] [-f inactive_time] [-g initial_group] [-G group[,...]] [-m [-k skeleton_dir]] [-p passwd] [-s shell] [-u uid [ -o]] login
        StringBuffer cmd = new StringBuffer();
        cmd.append("useradd ");
        cmd.append(notBlank("G", groups.getGroupsAsString(), true));
        cmd.append(notBlank("p", password, true));
        cmd.append(notBlank("e", expireDate, true));
        cmd.append(notBlank("f", daysBeforeDisable, true));
        cmd.append(login);

        return cmd.toString();
    }


    public String getUserModifyCommand(String newLogin) {
        // usermod [-c comment] [-d home_dir [-m]] [-e expire_date] [-f inactive_time] [-g initial_group] [-G group [,...]] [-l login_name] [-p passwd] [-s shell] [-u uid [-o]] [-L|-U] login
        StringBuffer cmd = new StringBuffer();
        cmd.append("usermod ");
        cmd.append(notBlank("e", expireDate, true));
        cmd.append(notBlank("f", daysBeforeDisable, true));
        cmd.append(notBlank("G", groups.getGroupsAsString(), true));
        cmd.append(notBlank("l", newLogin, true));
        cmd.append(notBlank("p", password, true));
        cmd.append(login);

        return cmd.toString();
    }
    
    public String getUserDeleteCommand() {
        StringBuffer cmd = new StringBuffer();
        cmd.append("userdel -r ");
        cmd.append(login);

        return cmd.toString();
    }

    // Expires a user's password
    public String getUserExpirePasswordCommand() {
        StringBuffer cmd = new StringBuffer();
        cmd.append("passwd -e ");
        cmd.append(login);
        return cmd.toString();
    }

    // Set new password. The command will block (passwd) and expects the password to be entered twice
    public String getUserSetPasswordCommand() {
        StringBuffer cmd = new StringBuffer();
        cmd.append("passwd ");
        cmd.append(login);
        return cmd.toString();
    }
    
    // Disables access with a password
    public String getUserLockCommand() {
        StringBuffer cmd = new StringBuffer();
        cmd.append("passwd -l ");
        cmd.append(login);

       return cmd.toString();
    }
    
    // Enables password again
    public String getUserUnlockCommand() {
        StringBuffer cmd = new StringBuffer();
        cmd.append("passwd -u ");
        cmd.append(login);

        return cmd.toString();
    }




                         // chage     - for resets



    
    private String notBlank(String cmdSwitch, String arg, boolean trailingSpace) {
        String retVal = "";
        if (arg != null && arg.length() > 0) {
            StringBuffer command = new StringBuffer();
            command.append('-');
            command.append(cmdSwitch.trim());
            command.append(" \"");
            command.append(arg.trim());
            command.append(" \"");
            if (trailingSpace)
                command.append(" ");
            retVal = command.toString();
        }
        
        return retVal;
    }
    
}
