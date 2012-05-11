package org.openiam.provision.service;

import org.openiam.provision.dto.ProvisionUser;

import java.util.Map;

/**
 * The pre-processor allows us to define a set of rules for provisioning users that can be used regardless of how provisioning
 * is triggered. For example, if an attribute just as a job code is supposed to indicate Role Membership, then these rules
 * can be defined in the PreProcessor script. These rules would then be in place for user created through the webconsole, selfserivce,
 * and synchronization
 *
 * User: suneetshah
 * Date: 5/10/12
 * Time: 10:00 PM
 * @version 2.2
 */
public abstract class AbstractPreProcessor implements ProvisionServicePreProcessor {

}
