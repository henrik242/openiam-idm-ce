package org.openiam.spml2.spi.linux;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleGroup;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.provision.type.ExtensibleUser;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.spml2.spi.linux.ssh.SSHAgent;
import org.openiam.spml2.spi.linux.ssh.SSHConnectionFactory;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
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
    private SSHAgent getSSH(ManagedSys managedSys) {
        String managedSysId = managedSys.getManagedSysId();
        SSHAgent ssh = null;

        if (!(managedSys.getResourceId() == null || managedSys.getResourceId().length() == 0)) {
            log.debug("ManagedSys found; Name=" + managedSys.getName());

            if ((ssh = SSHConnectionFactory.getSSH(managedSysId)) == null)
                ssh = SSHConnectionFactory.addSSH(managedSysId, managedSys.getHostUrl(), managedSys.getPort(), managedSys.getUserId(), managedSys.getDecryptPassword());
        }

        return ssh;
    }


    private SSHAgent getSSH(String targetId) {
        return getSSH(managedSysService.getManagedSys(targetId));
    }
    // ------------------------------------------


    

    // ---------- Interface

    public ResponseType testConnection(@WebParam(name = "managedSys", targetNamespace = "") ManagedSys managedSys) {
        String host =  managedSys.getHostUrl() + ":" + managedSys.getPort().toString();
        log.warn("Testing SSH connection with Linux host " + host + " (user= " + managedSys.getUserId() + ")");

        ResponseType r = new ResponseType();
        r.setStatus((getSSH(managedSys) == null) ? StatusCodeType.FAILURE : StatusCodeType.SUCCESS);
        return r;
    }





    public AddResponseType add(@WebParam(name = "reqType", targetNamespace = "") AddRequestType reqType) {
        log.warn("Add user called");
        AddResponseType responseType = new AddResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);

        // Extract attribues into a map. Also save groups
        HashMap<String, String> attributes = new HashMap<String, String>();
        LinuxGroups groups = null;
        
        List<ExtensibleObject> objList = reqType.getData().getAny();
        for (ExtensibleObject obj: objList) {
            log.warn("Object:" + obj.getName() + " - operation=" + obj.getOperation());

            // Extract attributes
            for (ExtensibleAttribute att: obj.getAttributes()) {
                attributes.put(att.getName(),  att.getValue());
            }

             // Extract groups
            ArrayList<String> groupList = new ArrayList<String>();
            ExtensibleUser extUser = (ExtensibleUser) obj;
            
            for (ExtensibleGroup g : extUser.getGroup()) {
                groupList.add(g.getGroup().getGrpName());
            }
            groups = new LinuxGroups(groupList);
        }


  
        // Add to SSH
        SSHAgent ssh = getSSH(reqType.getTargetID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(groups, attributes);
                String command = user.getUserAddCommand();
                log.error("SSH command: " + command);

                String result = ssh.executeCommand(command);
                log.warn(result);

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ModifyResponseType modify(@WebParam(name = "reqType", targetNamespace = "") ModifyRequestType reqType) {
        // TODO: GET VALUES
        ModifyResponseType resp = new ModifyResponseType();
        resp.setRequestID(reqType.getRequestID());
        resp.setStatus(StatusCodeType.SUCCESS);
        return resp;
    }


    public ResponseType delete(@WebParam(name = "reqType", targetNamespace = "") DeleteRequestType reqType) {
        ResponseType responseType = new ResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);

        // TODO: get
        String targetId = null;
        String login = null;
        SSHAgent ssh = getSSH(reqType.getRequestID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);
                String command = user.getUserDeleteCommand();

                log.error("SSH command: " + command);
                String result = ssh.executeCommand(command);
                log.warn(result);

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }  finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public LookupResponseType lookup(@WebParam(name = "reqType", targetNamespace = "") LookupRequestType reqType) {
        // TODO: search in user list

        return null;
    }


    public ResponseType setPassword(@WebParam(name = "request", targetNamespace = "") SetPasswordRequestType request) {
        ResponseType responseType = new ResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);
        
        String login = null;    // TODO: Get actual values
        String password = null;

        SSHAgent ssh = getSSH(request.getRequestID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, password, null, null, null, null, null, null, null);
                String command = user.getUserSetPasswordCommand();

                log.error("SSH command: " + command);
                log.debug(ssh.executeCommand(command));
                log.debug(ssh.executeCommand(password));    // first
                log.debug(ssh.executeCommand(password));    // retype

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }  finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResponseType expirePassword(@WebParam(name = "request", targetNamespace = "") ExpirePasswordRequestType request) {
        ResponseType responseType = new ResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = null;    // TODO: Get actual values

        SSHAgent ssh = getSSH(request.getRequestID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);
                String command = user.getUserExpirePasswordCommand();

                log.error("SSH command: " + command);
                log.debug(ssh.executeCommand(command));

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }  finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResetPasswordResponseType resetPassword(@WebParam(name = "request", targetNamespace = "") ResetPasswordRequestType request) {
        // TODO: choose new 'reset' value and call setPassword
        return null;
    }


    public ValidatePasswordResponseType validatePassword(@WebParam(name = "request", targetNamespace = "") ValidatePasswordRequestType request) {



          return null;
    }



    public ResponseType suspend(@WebParam(name = "request", targetNamespace = "") SuspendRequestType request) {
        ResponseType responseType = new ResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = null;    // TODO: Get actual values

        SSHAgent ssh = getSSH(request.getRequestID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);
                String command = user.getUserLockCommand();

                log.error("SSH command: " + command);
                log.debug(ssh.executeCommand(command));

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }  finally {
                ssh.logout();
            }
        }

        return responseType;
    }


    public ResponseType resume(@WebParam(name = "request", targetNamespace = "") ResumeRequestType request) {
        ResponseType responseType = new ResponseType();
        responseType.setStatus(StatusCodeType.FAILURE);

        String login = null;    // TODO: Get actual values

        SSHAgent ssh = getSSH(request.getRequestID());
        if (ssh != null) {
            try {
                LinuxUser user = new LinuxUser(null, login, null, null, null, null, null, null, null, null);
                String command = user.getUserUnlockCommand();

                log.error("SSH command: " + command);
                log.debug(ssh.executeCommand(command));

                responseType.setStatus(StatusCodeType.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }  finally {
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
