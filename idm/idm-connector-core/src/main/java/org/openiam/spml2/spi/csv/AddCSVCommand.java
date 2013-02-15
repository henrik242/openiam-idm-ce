package org.openiam.spml2.spi.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.openiam.base.BaseAttribute;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.ExtensibleType;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ReturnDataType;
import org.openiam.spml2.msg.StatusCodeType;

public class AddCSVCommand extends AbstractCSVCommand {
	public AddResponseType add(AddRequestType reqType) {
		AddResponseType response = new AddResponseType();
		response.setStatus(StatusCodeType.SUCCESS);
		List<BaseAttribute> targetMembershipList = new ArrayList<BaseAttribute>();

		boolean groupMembershipEnabled = true;

		log.debug("add request called..");

		String requestID = reqType.getRequestID();
		/*
		 * ContainerID - May specify the container in which this object should
		 * be created ie. ou=Development, org=Example
		 */
		PSOIdentifierType containerID = reqType.getContainerID();
		/*
		 * PSO - Provisioning Service Object - - ID must uniquely specify an
		 * object on the target or in the target's namespace - Try to make the
		 * PSO ID immutable so that there is consistency across changes.
		 */
		PSOIdentifierType psoID = reqType.getPsoID();
		/* targetID - */
		String targetID = reqType.getTargetID();

		// Data sent with request - Data must be present in the request per the
		// spec
		ExtensibleType data = reqType.getData();
		Map<QName, String> otherData = reqType.getOtherAttributes();

		/* Indicates what type of data we should return from this operations */
		ReturnDataType returnData = reqType.getReturnData();

		/*
		 * A) Use the targetID to look up the connection information under
		 * managed systems
		 */
		ManagedSys managedSys = managedSysService.getManagedSys(targetID);

		// Initialise
		try {
			ProvisionUser user = reqType.getpUser();
			if (user == null) {
				response.setStatus(StatusCodeType.FAILURE);
				response.setError(ErrorCode.CSV_ERROR);
				response.addErrorMessage("Sync object is null");
			}
			this.addUsersToCSV(psoID.getID(), user, managedSys);
		} catch (Exception e) {
			e.printStackTrace();

			log.error(e);
			// return a response object - even if it fails so that it can be
			// logged.
			response.setStatus(StatusCodeType.FAILURE);
			response.setError(ErrorCode.CSV_ERROR);
			response.addErrorMessage(e.toString());

		}
		return response;
	}
}
