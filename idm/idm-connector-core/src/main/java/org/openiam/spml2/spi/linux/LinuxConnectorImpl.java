package org.openiam.spml2.spi.linux;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.spml2.spi.linux.ssh.SSHAgent;
import org.openiam.spml2.spi.linux.ssh.SSHConnectionFactory;
import org.openiam.spml2.spi.linux.ssh.SSHException;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;

/**
 * User: kevin
 * Date: 2/26/12
 * Time: 2:12 AM
 */
@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService",
        targetNamespace = "http://www.openiam.org/service/connector",
        portName = "LinuxConnectorServicePort",
        serviceName = "LinuxConnectorService")
public class LinuxConnectorImpl extends AbstractSpml2Complete implements ConnectorService {
    private static final Log log = LogFactory.getLog(LinuxConnectorImpl.class);

    protected ManagedSystemDataService managedSysService;
    protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;
    protected ResourceDataService resourceDataService;


    // ------------------- SSH connection methods
    private SSHConnectionFactory connections = new SSHConnectionFactory();

    private SSHAgent getSSH(ManagedSys managedSys) {
        SSHAgent ssh = null;

        if (managedSys != null) {
            String managedSysId = managedSys.getManagedSysId();
            if (!(managedSys.getResourceId() == null || managedSys.getResourceId().length() == 0)) {
                log.debug("ManagedSys found; Name=" + managedSys.getName());

                if ((ssh = connections.getSSH(managedSysId)) == null)
                    ssh = connections.addSSH(managedSysId, managedSys.getHostUrl(), managedSys.getPort(), managedSys.getUserId(), managedSys.getDecryptPassword());
            }
        }

        return ssh;
    }

    private SSHAgent getSSH(String targetId) {
        log.debug("Getting SSH for managed sys with id=" + targetId);
        return getSSH(managedSysService.getManagedSys(targetId));
    }
    // ------------------------------------------


    // ---------- Interface
    public ResponseType testConnection(@WebParam(name = "managedSys", targetNamespace = "") ManagedSys managedSys) {
        String host = managedSys.getHostUrl() + ":" + managedSys.getPort().toString();
        log.debug("Testing SSH connection with Linux host " + host + " (user= " + managedSys.getUserId() + ")");

        ResponseType r = new ResponseType();
        r.setStatus((getSSH(managedSys) == null) ? StatusCodeType.FAILURE : StatusCodeType.SUCCESS);
        return r;
    }


    /**
     * Extracts a LinuxUser from the given list of Extensible Objects,
     *
     * @param login      Login name of new user
     * @param objectList List containing attributes
     * @return A LinuxUser with the relevant fields populated
     */
    private LinuxUser objectToLinuxUser(String login, List<ExtensibleObject> objectList) {
        LinuxUser user = null;

        if (login != null) {
            // Extract attribues into a map. Also save groups
            HashMap<String, String> attributes = new HashMap<String, String>();
            attributes.put("login", login);

            if (objectList != null) {
                for (ExtensibleObject obj : objectList) {
                    log.debug("Object:" + obj.getName() + " - operation=" + obj.getOperation());

                    // Extract attributes
                    for (ExtensibleAttribute att : obj.getAttributes()) {
                        if (att != null) {
                            attributes.put(att.getName(), att.getValue());
                        }
                    }
                }
            }

            try {
                user = new LinuxUser(attributes);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        } else {
            log.error("Login name for Linux user not specified");
        }

        return user;
    }

    /**
     * Change the password of another account as root. Passwd expects the password to be sent twice over STDIN
     * This is more secure than changing the password via arguments to useradd/usermodify, as these would appear
     * within the process list
     *
     * @param sshAgent Handle to SSH connection
     * @param user     User with new password set
     * @throws SSHException
     */
    private void sendPassword(SSHAgent sshAgent, LinuxUser user) throws SSHException {
        String pass = user.getPassword();
        String doubledPass = pass + "\n" + pass + "\n";
        sshAgent.executeCommand(user.getUserSetPasswordCommand(), doubledPass);
    }


    /**
     * Compares the user's group list with that of the server. Any groups which are not defined on the server
     * will be created automatically
     *
     * @param sshAgent   Handle to SSH connection
     * @param userGroups Groups to which the user should be added
     * @throws SSHException
     */
    private void addNonExistingGroups(SSHAgent sshAgent, LinuxGroups userGroups) throws SSHException {
        // Get list of groups which are not yet defined on the server
        String cmd = userGroups.getGroupsNotOnServerCommand();
        if (cmd != null) {
            String newGroups = sshAgent.executeCommand(cmd);

            // Add any new groups that are not defined on the server
            LinuxGroups groupsNotOnServer = new LinuxGroups(newGroups);
            if (groupsNotOnServer.hasGroups())
                sshAgent.executeCommand(groupsNotOnServer.getAddGroupsCommand());
        }
    }



    public AddResponseType add(@WebParam(name = "reqType", targetNamespace = "") AddRequestType reqType) {
        log.debug("Add user called");
        AddResponseType responseType = new AddResponseType();
        responseType.setRequestID(reqType.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        LinuxUser user = objectToLinuxUser(reqType.getPsoID().getID(), reqType.getData().getAny());
        if (user != null) {
            SSHAgent ssh = getSSH(reqType.getTargetID());
            if (ssh != null) {
                try {
                    // Check groups and add if necessary
                    addNonExistingGroups(ssh, user.getGroups());

                    // Then add user
                    ssh.executeCommand(user.getUserAddCommand());
                    sendPassword(ssh, user);
                    ssh.executeCommand(user.getUserSetDetailsCommand());

                    responseType.setStatus(StatusCodeType.SUCCESS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    ssh.logout();
                }
            }
        }

        return responseType;
    }


    /**
     * Detects whether a given modifications list contains a rename directive
     *
     * @param modTypeList modifications list
     * @return Original user account name; null if unchanged
     */
    private String isRename(List<ModificationType> modTypeList) {
        for (ModificationType mod : modTypeList) {
            for (ExtensibleObject obj : mod.getData().getAny()) {
                for (ExtensibleAttribute att : obj.getAttributes()) {
                    if (att.getOperation() != 0 && att.getName() != null && att.getName().equalsIgnoreCase("ORIG_IDENTITY")) {
                        return att.getValue();
                    }
                }
            }
        }
        return null;
    }


    public ModifyResponseType modify(@WebParam(name = "reqType", targetNamespace = "") ModifyRequestType reqType) {
        log.debug("Modify user called");

        ModifyResponseType responseType = new ModifyResponseType();
        responseType.setRequestID(reqType.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        PSOIdentifierType psoID = reqType.getPsoID();

        String originalName = isRename(reqType.getModification());
        String login = (originalName == null) ? psoID.getID() : originalName;

        LinuxUser user = objectToLinuxUser(psoID.getID(), reqType.getModification().get(0).getData().getAny());

        if (user != null) {
            SSHAgent ssh = getSSH(psoID.getTargetID());
            if (ssh != null) {
                try {
                    // Check groups and add if necessary
                    addNonExistingGroups(ssh, user.getGroups());

                    // Then modify account
                    ssh.executeCommand(user.getUserModifyCommand(login));
                    ssh.executeCommand(user.getUserSetDetailsCommand());
                    sendPassword(ssh, user);

                    responseType.setStatus(StatusCodeType.SUCCESS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    ssh.logout();
                }
            }
        }

        return responseType;
    }


    public ResponseType delete(@WebParam(name = "reqType", targetNamespace = "") DeleteRequestType reqType) {
        log.debug("Delete user called");

        ResponseType responseType = new ResponseType();
        responseType.setRequestID(reqType.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        LinuxUser user = objectToLinuxUser(reqType.getPsoID().getID(), null);
        if (user != null) {
            SSHAgent ssh = getSSH(reqType.getPsoID().getTargetID());
            if (ssh != null) {
                try {
                    ssh.executeCommand(user.getUserDeleteCommand());
                    responseType.setStatus(StatusCodeType.SUCCESS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    ssh.logout();
                }
            }
        }

        return responseType;
    }


    public LookupResponseType lookup(@WebParam(name = "reqType", targetNamespace = "") LookupRequestType reqType) {
        log.debug("Lookup user called");

        LookupResponseType responseType = new LookupResponseType();
        responseType.setRequestID(reqType.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        LinuxUser user = objectToLinuxUser(reqType.getPsoID().getID(), null);
        if (user != null) {
            SSHAgent ssh = getSSH(reqType.getPsoID().getTargetID());
            if (ssh != null) {
                try {
                    String result = ssh.executeCommand(user.getUserExistsCommand());

                    if (result != null && result.trim().length() > 0)
                        responseType.setStatus(StatusCodeType.SUCCESS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    ssh.logout();
                }
            }
        }

        return responseType;
    }


    public ResponseType setPassword(@WebParam(name = "request", targetNamespace = "") SetPasswordRequestType request) {
        log.debug("Set password called");

        ResponseType responseType = new ResponseType();
        responseType.setRequestID(request.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = request.getPsoID().getID();
        String password = request.getPassword();
        SSHAgent ssh = getSSH(request.getPsoID().getTargetID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, password, null, null, null, null, null, null, null);

                sendPassword(ssh, user);
                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResponseType expirePassword(@WebParam(name = "request", targetNamespace = "") ExpirePasswordRequestType request) {
        log.debug("Expire password called");

        ResponseType responseType = new ResponseType();
        responseType.setRequestID(request.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = request.getPsoID().getID();
        SSHAgent ssh = getSSH(request.getPsoID().getTargetID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);

                ssh.executeCommand(user.getUserExpirePasswordCommand());
                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResetPasswordResponseType resetPassword(@WebParam(name = "request", targetNamespace = "") ResetPasswordRequestType request) {
        log.debug("Reset password called");

        // TODO
        return null;
    }


    public ValidatePasswordResponseType validatePassword(@WebParam(name = "request", targetNamespace = "") ValidatePasswordRequestType request) {
        log.debug("Validate password called");

        //TODO
        return null;
    }


    public ResponseType suspend(@WebParam(name = "request", targetNamespace = "") SuspendRequestType request) {
        log.debug("Suspend user called");

        ResponseType responseType = new ResponseType();
        responseType.setRequestID(request.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = request.getPsoID().getID();
        SSHAgent ssh = getSSH(request.getPsoID().getTargetID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);

                ssh.executeCommand(user.getUserLockCommand());
                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResponseType resume(@WebParam(name = "request", targetNamespace = "") ResumeRequestType request) {
        log.debug("Resume user called");

        ResponseType responseType = new ResponseType();
        responseType.setRequestID(request.getRequestID());
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = request.getPsoID().getID();
        SSHAgent ssh = getSSH(request.getPsoID().getTargetID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);

                ssh.executeCommand(user.getUserUnlockCommand());
                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    // >>>>>>>> Accessor/Mutator Methods for bean
    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public ManagedSystemObjectMatchDAO getManagedSysObjectMatchDao() {
        return managedSysObjectMatchDao;
    }

    public void setManagedSysObjectMatchDao(ManagedSystemObjectMatchDAO managedSysObjectMatchDao) {
        this.managedSysObjectMatchDao = managedSysObjectMatchDao;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }
}
