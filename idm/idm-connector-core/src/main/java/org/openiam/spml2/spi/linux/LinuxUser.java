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
    

    // Sets a user's details, such as full name, room, phone numbers etc.
    public String getUserSetDetailsCommand() {
        // chfn [-f full_name] [-r room_no] [-w work_ph] [-h home_ph] [-o other]  [user]
        StringBuilder cmd = new StringBuilder();
        cmd.append("chfn ");
        cmd.append(notBlank("f", (name + " " + surname).trim(), true));
        cmd.append(notBlank("r", roomNumber, true));
        cmd.append(notBlank("w", workPhone, true));
        cmd.append(notBlank("h", homePhone, true));
        cmd.append(login);

        return cmd.toString();
    }

    // Add a user account
    public String getUserAddCommand() {
        // useradd [-c comment] [-d home_dir] [-e expire_date] [-f inactive_time] [-g initial_group] [-G group[,...]]
        // [-m [-k skeleton_dir]] [-p passwd] [-s shell] [-u uid [ -o]] login

        StringBuilder cmd = new StringBuilder();
        cmd.append("useradd -N ");              // do not create a group with the user login
        cmd.append(notBlank("G", groups.getGroupsAsCommaSeparatedString(), true));
        cmd.append(notBlank("e", expireDate, true));
        cmd.append(notBlank("f", daysBeforeDisable, true));
        cmd.append(login);

        return cmd.toString();
    }

    // Modify an existing user
    public String getUserModifyCommand(String oldLogin) {
        // usermod [-c comment] [-d home_dir [-m]] [-e expire_date] [-f inactive_time] [-g initial_group] [-G group [,...]]
        // [-l login_name] [-p passwd] [-s shell] [-u uid [-o]] [-L|-U] login

        StringBuilder cmd = new StringBuilder();
        cmd.append("usermod ");
        cmd.append(notBlank("e", expireDate, true));
        cmd.append(notBlank("f", daysBeforeDisable, true));
        cmd.append(notBlank("G", groups.getGroupsAsCommaSeparatedString(), true));
        cmd.append(notBlank("l", login, true));
        cmd.append(oldLogin);

        return cmd.toString();
    }

    // Deletes a user with a given login name
    public String getUserDeleteCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("userdel -r ");
        cmd.append(login);

        return cmd.toString();
    }

    // Expires a user's password
    public String getUserExpirePasswordCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("passwd -e ");
        cmd.append(login);
        return cmd.toString();
    }

    // Set new password. The command will block (passwd) and expects the password to be entered twice
    public String getUserSetPasswordCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("passwd ");
        cmd.append(login);

        return cmd.toString();
    }
    
    // Disables access with a password
    public String getUserLockCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("passwd -l ");
        cmd.append(login);

       return cmd.toString();
    }
    
    // Enables password again
    public String getUserUnlockCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("passwd -u ");
        cmd.append(login);

        return cmd.toString();
    }

    // Searches for user
    public String getUserExistsCommand() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("grep \"^");
        cmd.append(login);
        cmd.append(":\" /etc/passwd");

        return cmd.toString();
    }


    /**
     * Only adds a switch if the argument is not null or empty. The argument is also placed in quotes.
     * @param cmdSwitch switch, without dash
     * @param arg argument for switch
     * @param trailingSpace if true, adds a space to the end of the expression
     * @return Formatted command switch and argument; empty string if argument is empty
     */
    private String notBlank(String cmdSwitch, String arg, boolean trailingSpace) {
        String retVal = "";
        if (arg != null && arg.length() > 0) {
            StringBuilder command = new StringBuilder();
            command.append('-');
            command.append(cmdSwitch.trim());
            command.append(" \"");
            command.append(arg.trim());
            command.append("\"");
            if (trailingSpace)
                command.append(" ");
            retVal = command.toString();
        }
        
        return retVal;
    }



    public LinuxGroups getGroups() {
        return groups;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getDaysBeforeDisable() {
        return daysBeforeDisable;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String tmpPass = (password == null) ? "" : password;
        
        sb.append(";Name: ").append(name);
        sb.append(";Surname:").append(surname);
        sb.append(";Login: ").append(login);
        sb.append(";Password set? ").append(tmpPass.length() > 0);
        sb.append(";Home/Work: " + homePhone + "/" + workPhone);
        sb.append(";Groups:").append(groups == null ? "none" : groups.getGroupsAsCommaSeparatedString());
        return sb.toString();
    }
}
