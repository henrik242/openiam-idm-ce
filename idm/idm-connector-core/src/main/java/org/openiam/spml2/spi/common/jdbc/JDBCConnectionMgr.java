package org.openiam.spml2.spi.common.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Manages connections to LDAP
 * @author Suneet Shah
 *
 */
public class JDBCConnectionMgr {

    Connection sqlCon = null;


    private static final Log log = LogFactory.getLog(JDBCConnectionMgr.class);

    public JDBCConnectionMgr() {
    }

	public Connection  connect(ManagedSys managedSys) throws ClassNotFoundException, SQLException {
        Class.forName(managedSys.getDriverUrl());
        final String url = managedSys.getConnectionString() ;
        sqlCon = DriverManager.getConnection(url,managedSys.getUserId(), managedSys.getDecryptPassword() );
        return sqlCon;
	}
}
