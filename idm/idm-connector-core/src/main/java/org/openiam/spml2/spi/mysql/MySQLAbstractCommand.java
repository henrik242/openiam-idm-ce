package org.openiam.spml2.spi.mysql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.spml2.spi.common.jdbc.JDBCConnectionMgr;

/**
 * Base class for commands that are used by the MySQL Connector
 */
public abstract class MySQLAbstractCommand {

    protected static final Log log = LogFactory.getLog(MySQLAbstractCommand.class);

    protected ManagedSystemDataService managedSysService;
    protected ResourceDataService resourceDataService;
    protected JDBCConnectionMgr connectionMgr;

    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public JDBCConnectionMgr getConnectionMgr() {
        return connectionMgr;
    }

    public void setConnectionMgr(JDBCConnectionMgr connectionMgr) {
        this.connectionMgr = connectionMgr;
    }
}

