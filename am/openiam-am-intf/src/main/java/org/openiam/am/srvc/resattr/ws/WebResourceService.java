package org.openiam.am.srvc.resattr.ws;

import org.openiam.am.srvc.resattr.dto.AttributeMap;
import org.openiam.am.srvc.resattr.dto.WebResourceResponse;
import org.openiam.base.ws.Response;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;


@WebService(targetNamespace = "urn:idm.openiam.org/srvc/res/service", name = "WebResourceService")
public interface WebResourceService {

    @WebMethod
    AttributeMap getAttributeMap(
            @WebParam(name = "attributeId", targetNamespace = "")
            String attributeId) throws Exception;

    @WebMethod
    List<AttributeMap> getAttributeMapCollection(@WebParam(name = "resourceId", targetNamespace = "") String resourceId) throws Exception;

    @WebMethod
    AttributeMap addAttributeMap(
            @WebParam(name = "attribute", targetNamespace = "") AttributeMap attribute) throws Exception;

    @WebMethod
    Response addAttributeMapCollection(
            @WebParam(name = "attributeList", targetNamespace = "")
            List<AttributeMap> attributeList) throws Exception;


    @WebMethod
    AttributeMap updateAttributeMap(
            @WebParam(name = "attribute", targetNamespace = "") AttributeMap attribute) throws Exception;



    @WebMethod
    void removeAttributeMap(
            @WebParam(name = "attributeId", targetNamespace = "")
            String attributeId) throws Exception;


    @WebMethod
    int removeResourceAttributeMaps(
            @WebParam(name = "resourceId", targetNamespace = "")
            String resourceId)throws Exception ;



    @WebMethod
    WebResourceResponse getSSOAttributes(
            @WebParam(name = "resourceId", targetNamespace = "")
            String resourceId,
            @WebParam(name = "principalName", targetNamespace = "")
            String principalName,
            @WebParam(name = "securityDomain", targetNamespace = "")
            String securityDomain,
            @WebParam(name = "managedSysId", targetNamespace = "")
            String managedSysId);



}