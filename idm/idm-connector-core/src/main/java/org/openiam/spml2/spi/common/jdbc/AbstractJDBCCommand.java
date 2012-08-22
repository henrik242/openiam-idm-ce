package org.openiam.spml2.spi.common.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJDBCCommand {

    protected static final Log log = LogFactory.getLog(AbstractJDBCCommand.class);

    protected ManagedSystemDataService managedSysService;
    protected ResourceDataService resourceDataService;
    protected JDBCConnectionMgr connectionMgr;

    protected void populateResponse(final ResponseType response, final StatusCodeType status,
                                    final ErrorCode err, final String msg) {
        response.setStatus(status);
        response.setError(err);
        response.addErrorMessage(msg);
    }

    protected List<AttributeMap> attributeMaps(final Resource resource) {
        return managedSysService.getResourceAttributeMaps(resource.getResourceId());
    }

    @Required
    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    @Required
    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    @Required
    public void setConnectionMgr(JDBCConnectionMgr connectionMgr) {
        this.connectionMgr = connectionMgr;
    }
}
