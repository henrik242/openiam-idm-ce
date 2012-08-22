package org.openiam.spml2.spi.orcl;

import org.mule.util.StringUtils;
import org.openiam.spml2.spi.common.jdbc.AbstractJDBCCommand;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lev
 * Date: 8/21/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOracleCommand extends AbstractJDBCCommand {

    private static final String SELECT_SQL = "SELECT USER_ID FROM DBA_USERS WHERE USERNAME=?";

    protected boolean identityExists(final Connection connection, final String principalName) throws SQLException {
        boolean exists = false;
        if(connection != null) {
            if(StringUtils.isNotBlank(principalName)) {
                final PreparedStatement statement = connection.prepareStatement(SELECT_SQL);
                statement.setString(1, principalName);
                final ResultSet rs = statement.executeQuery();
                if (rs != null && rs.next()) {
                    return true;
                }
            }
        }
        return exists;
    }
}
