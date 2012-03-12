package org.openiam.spml2.spi.linux.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/** Class to manage multiple SSH connections. The Factory stores an SSH agent for each given ID (managed system ID).
 *  New agents can be added and bound to a specified ID.
 */

public class SSHConnectionFactory {

    private static final Log log = LogFactory.getLog(SSHConnectionFactory.class);
    private static HashMap<String, SSHAgent> connections = new HashMap<String, SSHAgent>();


    /**
     * Fetch an SSHAgent associated with the given ID
     * @param id Managed system ID to which the SSHAgent is connected
     * @return Null if the agent does not exist or the connection cannot be formed
     */
    public static SSHAgent getSSH(String id) {
        SSHAgent ssh = connections.get(id);

        if (ssh != null) {
            log.warn("Using previously-opened connection: " + id);

            if (!ssh.isAuthenticationComplete()) {
                try {
                    if (!ssh.connect())
                        ssh = null;
                } catch (SSHException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return ssh;
    }


    /**
     * Closes an SSH connection and removes it from the map
     * @param id Managed system ID to which the SSHAgent is connected
     */
    public static void removeSSH(String id) {
        SSHAgent ssh = connections.get(id);
        if (ssh != null) {
            ssh.logout();
            connections.remove(id);
        }
    }


    /**
     * Register an SSH connection and add it to the SSH agent list
     * @param id Managed system ID to which the SSHAgent is connected
     * @param host URL or IP of managed connection
     * @param port Port to which to connect
     * @param username Username of SSH account
     * @param password Password of SSH account
     * @return
     */
    public static SSHAgent addSSH(String id, String host, Integer port, String username, String password) {
        log.warn("Creating new SSH connection for ID:" + id);
        SSHAgent ssh = new SSHAgent(host, port, username, password);
        try {
            if (!ssh.connect())
                ssh = null;

            connections.put(id, ssh);
        } catch (SSHException e) {
            log.error(e.getMessage());
            ssh = null;
        }

        return ssh;
    }
}
