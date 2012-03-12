package org.openiam.spml2.spi.linux.ssh;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2/26/12
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSHException extends Exception {
    public SSHException(String s, Exception e) {
        super(s, e);
    }
    
}
