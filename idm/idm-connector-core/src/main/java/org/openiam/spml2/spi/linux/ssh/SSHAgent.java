// http://www.informit.com/guides/content.aspx?g=java&seqNum=489

package org.openiam.spml2.spi.linux.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;


/**
 * The SSHAgent allows a Java application to execute commands on a remote server via SSH
 *
 * @author shaines
 */
public class SSHAgent {

    private static final Log log = LogFactory.getLog(SSHAgent.class);

    private String hostname;
    private String username;
    private String password;
    private int port;

    private Connection connection;

    public SSHAgent(String hostname, Integer port, String username, String password) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;

        if (port != null && port > 0)
            this.port = port;
        else
            this.port = 22;
    }


    public boolean connect() throws SSHException {
        try {
            // Connect to the server
            connection = new Connection(hostname, port);
            connection.connect();

            // Authenticate
            boolean result = connection.authenticateWithPassword(username, password);
            log.debug("Connection result: " + result);
            return result;
        } catch (Exception e) {
            throw new SSHException("An exception occurred while trying to connect to the host: " + hostname + ", Exception=" + e.getMessage(), e);
        }
    }

    public String executeCommand(String command) throws  SSHException {
        return executeCommand(command, null);
    }
    
    /**
     * Executes the specified command and returns the response from the server
     *
     * @param command The command to execute
     * @param moreArgs Arguments to be piped into STDIO. Accepts newlines
     * @return The response that is returned from the server (or null)
     * @throws SSHException
     */
    public String executeCommand(String command, String moreArgs) throws SSHException {
        try {
            // Open a session
            Session session = connection.openSession();

            log.debug("Sending SSH command: " + command);

            // Execute the command
            session.execCommand(command);

            if (moreArgs != null) {
                log.debug(".. piping arguments to STDIN");
                BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(session.getStdin()));
                String[] args = moreArgs.split("\\n");
                
                for (String a : args) {
                    bf.write(a);
                    bf.newLine();
                }

                bf.flush();
            }
            
            // Read the results
            StringBuilder sb = new StringBuilder();
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            // DEBUG: dump the exit code
            log.debug("ExitCode: " + session.getExitStatus());

            // Close the session
            session.close();

            // Return the results to the caller
            return sb.toString();
        } catch (Exception e) {
            throw new SSHException("An exception occurred while executing the following command: " + command + ". Exception = " + e.getMessage(), e);
        }
    }


    /**
     * Logs out from the server
     */
    public void logout() {
        try {
            connection.close();
        } catch (Exception e) {
            log.error("An exception occurred while closing the SSH connection: " + e.getMessage(), e);
        }
    }

    /**
     * Returns true if the underlying authentication is complete, otherwise returns false
     *
     * @return
     */
    public boolean isAuthenticationComplete() {
        return connection.isAuthenticationComplete();
    }
}