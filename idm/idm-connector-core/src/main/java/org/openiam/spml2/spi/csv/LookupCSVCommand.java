package org.openiam.spml2.spi.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.openiam.base.BaseAttribute;
import org.openiam.connector.type.LookupResponse;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.ExtensibleType;
import org.openiam.spml2.msg.LookupRequestType;
import org.openiam.spml2.msg.LookupResponseType;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ReturnDataType;
import org.openiam.spml2.msg.StatusCodeType;

public class LookupCSVCommand extends AbstractCSVCommand {
	public LookupResponseType lookup(LookupRequestType reqType) {
		LookupResponseType response = new LookupResponseType();
		response.setStatus(StatusCodeType.SUCCESS);
		List<BaseAttribute> targetMembershipList = new ArrayList<BaseAttribute>();

		boolean groupMembershipEnabled = true;

		log.debug("add request called..");

		String requestID = reqType.getRequestID();
		/*
		 * PSO - Provisioning Service Object - - ID must uniquely specify an
		 * object on the target or in the target's namespace - Try to make the
		 * PSO ID immutable so that there is consistency across changes.
		 */

		Map<QName, String> otherData = reqType.getOtherAttributes();

		/* Indicates what type of data we should return from this operations */
		ReturnDataType returnData = reqType.getReturnData();

		/*
		 * A) Use the targetID to look up the connection information under
		 * managed systems
		 */
		ManagedSys managedSys = managedSysService.getManagedSys(reqType
				.getPsoID().getTargetID());

		// Initialise
		try {
			ProvisionUser user = reqType.getpUser();
			if (user == null) {
				response.setStatus(StatusCodeType.FAILURE);
				response.setError(ErrorCode.CSV_ERROR);
				response.addErrorMessage("Sync object is null");
			}
			if (this.lookupObjectInCSV(user, managedSys)) {
				response.setStatus(StatusCodeType.SUCCESS);

			} else
				response.setStatus(StatusCodeType.FAILURE);
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
