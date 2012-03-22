package org.openiam.spml2.spi.mysql;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Implementation object for the MySQL Connector
 * User: suneetshah
 * Date: 3/21/12
 * Time: 10:07 PM
 */
@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService",
        targetNamespace = "http://www.openiam.org/service/connector",
        portName = "MySQLConnectorPort",
        serviceName = "MySQLConnector")
public class MySQLConnectorImpl extends AbstractSpml2Complete implements ConnectorService {

    protected MySQLAddCommand addCommand;

    public AddResponseType add(AddRequestType reqType) {

        return addCommand.add(reqType);

    }

    public ResponseType testConnection(@WebParam(name = "managedSys", targetNamespace = "") ManagedSys managedSys) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



    public ModifyResponseType modify(@WebParam(name = "reqType", targetNamespace = "") ModifyRequestType reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResponseType delete(@WebParam(name = "reqType", targetNamespace = "") DeleteRequestType reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LookupResponseType lookup(@WebParam(name = "reqType", targetNamespace = "") LookupRequestType reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResponseType setPassword(@WebParam(name = "request", targetNamespace = "") SetPasswordRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResponseType expirePassword(@WebParam(name = "request", targetNamespace = "") ExpirePasswordRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResetPasswordResponseType resetPassword(@WebParam(name = "request", targetNamespace = "") ResetPasswordRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ValidatePasswordResponseType validatePassword(@WebParam(name = "request", targetNamespace = "") ValidatePasswordRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResponseType suspend(@WebParam(name = "request", targetNamespace = "") SuspendRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ResponseType resume(@WebParam(name = "request", targetNamespace = "") ResumeRequestType request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public MySQLAddCommand getAddCommand() {
        return addCommand;
    }

    public void setAddCommand(MySQLAddCommand addCommand) {
        this.addCommand = addCommand;
    }
}
