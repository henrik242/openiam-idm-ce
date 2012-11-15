package org.openiam.spml2.spi.exchange;

import javax.jws.WebParam;
import javax.jws.WebService;
import org.openiam.connector.type.LookupRequest;
import org.openiam.connector.type.LookupResponse;
import org.openiam.connector.type.PasswordRequest;
import org.openiam.connector.type.ResponseType;
import org.openiam.connector.type.ResumeRequest;
import org.openiam.connector.type.SearchRequest;
import org.openiam.connector.type.SearchResponse;
import org.openiam.connector.type.SuspendRequest;
import org.openiam.connector.type.UserRequest;
import org.openiam.connector.type.UserResponse;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.spml2.interf.RemoteConnectorService;
@WebService(endpointInterface = "org.openiam.spml2.interf.RemoteConnectorService",
        targetNamespace = "http://www.openiam.org/service/connector",
        portName = "ExchangeConnectorServicePort",
        serviceName = "ExchangeConnectorService")
public class ExchangeConnectorImpl implements RemoteConnectorService {
    @Override
    public UserResponse add(@WebParam(name = "reqType", targetNamespace = "") UserRequest reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UserResponse modify(@WebParam(name = "reqType", targetNamespace = "") UserRequest reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UserResponse delete(@WebParam(name = "reqType", targetNamespace = "") UserRequest reqType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LookupResponse lookup(@WebParam(name = "lookupRequest", targetNamespace = "") LookupRequest lookupRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SearchResponse search(@WebParam(name = "searchRequest", targetNamespace = "") SearchRequest searchRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType setPassword(@WebParam(name = "request", targetNamespace = "") PasswordRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType resetPassword(@WebParam(name = "request", targetNamespace = "") PasswordRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType suspend(@WebParam(name = "request", targetNamespace = "") SuspendRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType resume(@WebParam(name = "request", targetNamespace = "") ResumeRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType testConnection(@WebParam(name = "managedSys", targetNamespace = "") ManagedSys managedSys) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseType reconcileResource(@WebParam(name = "config", targetNamespace = "") ReconciliationConfig config) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
