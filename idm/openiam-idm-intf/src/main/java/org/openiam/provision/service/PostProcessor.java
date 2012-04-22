package org.openiam.provision.service;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;

import java.util.Map;

/**
 * Interface which all post process scripts should implement
 */
public interface PostProcessor {
    int addUser(ProvisionUser user, Map<String, Object> bindingMap,boolean success);
    int modifyUser(ProvisionUser user, Map<String, Object> bindingMap,boolean success);
    int deleteUser(ProvisionUser user, Map<String, Object> bindingMap,boolean success);
    int setPassword( Map<String, Object> bindingMap,boolean success);

}
