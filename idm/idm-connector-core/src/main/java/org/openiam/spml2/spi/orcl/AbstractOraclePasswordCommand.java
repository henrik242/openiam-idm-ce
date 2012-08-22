package org.openiam.spml2.spi.orcl;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOraclePasswordCommand extends AbstractOracleCommand {

    private static final String CHANGE_PASSWORD_SQL = "ALTER USER \"%s\" IDENTIFIED BY \"%s\"";

    protected void changePassword(final ManagedSys managedSys, final String principalName, final String password) throws SQLException, ClassNotFoundException {

        Connection connection = null;

        try {
            connection = connectionMgr.connect(managedSys);
            final String sql = String.format(CHANGE_PASSWORD_SQL, principalName, password);
            if(log.isDebugEnabled()) {
                log.debug(String.format("SQL=%s", sql));
            }

            connection.createStatement().execute(sql);
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
    }
}
