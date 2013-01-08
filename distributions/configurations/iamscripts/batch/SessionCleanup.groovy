
/**
 * Session Cleanup script. To log users out where the session has expired.
 * Used when IDM is used in an SSO environment without the proxy
 * User: suneetshah
 */

import groovy.sql.*
import java.util.ResourceBundle;


def paramList = [];
ResourceBundle res = ResourceBundle.getBundle("datasource");

def db=res.getString("openiam.driver_url")
def user=res.getString("openiam.username")
def password=res.getString("openiam.password")
def driver= res.getString("openiam.driver_classname")

long curTime = System.currentTimeMillis();

// if the session has expired then log them out of the DB

String query = "UPDATE AUTH_STATE " +
        "       SET TOKEN = 'LOGOUT' " +
        "   WHERE EXPIRATION < ? and TOKEN <> 'LOGOUT' ";

paramList.add(curTime);

def sql = Sql.newInstance(db,user, password, driver)

int recordsUpdated = sql.executeUpdate(query, paramList);

println("Session clean up updated the following number of records: " + recordsUpdated);

