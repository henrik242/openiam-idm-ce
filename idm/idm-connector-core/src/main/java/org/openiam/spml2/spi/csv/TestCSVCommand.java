package org.openiam.spml2.spi.csv;

import java.util.List;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.StatusCodeType;

public class TestCSVCommand extends AbstractCSVCommand {
	public ResponseType test(ManagedSys managedSys) {
		ResponseType response = new ResponseType();
		try {
			List<ProvisionUser> users = this.getUsersFromCSV(managedSys);
		} catch (Exception e) {
			response.setStatus(StatusCodeType.FAILURE);
			response.setRequestID(managedSys.getManagedSysId());
		}
		response.setStatus(StatusCodeType.SUCCESS);
		return response;
	}
}
