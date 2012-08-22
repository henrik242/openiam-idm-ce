package org.openiam.spml2.spi.orcl;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/22/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOracleAccountStatusCommand extends AbstractOraclePasswordCommand {

    protected enum AccountStatus {
        LOCKED("lock"),
        UNLOCKED("unlock");

        private String name;

        AccountStatus(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final String SQL = "ALTER USER \"%s\" account %s";

    protected void changeAccountStatus(final ManagedSys managedSys, final String principalName, final AccountStatus accountStatus)
        throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = connectionMgr.connect(managedSys);
            final String sql = String.format(SQL, principalName, accountStatus.toString());
            connection.createStatement().execute(sql);
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
    }
}
