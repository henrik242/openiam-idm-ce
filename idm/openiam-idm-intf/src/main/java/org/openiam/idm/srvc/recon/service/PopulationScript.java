package org.openiam.idm.srvc.recon.service;

import org.openiam.provision.dto.ProvisionUser;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pascal
 * Date: 30.04.12
 * Time: 19:59
 * To change this template use File | Settings | File Templates.
 */
public interface PopulationScript {
    public int execute(Map<String, String> line, ProvisionUser pUser);
}
