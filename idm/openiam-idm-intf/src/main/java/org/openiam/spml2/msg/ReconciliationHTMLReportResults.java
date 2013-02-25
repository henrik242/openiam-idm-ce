package org.openiam.spml2.msg;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;

public enum ReconciliationHTMLReportResults {
	BROKEN_CSV("Broken record in CSV"),
	NOT_EXIST_IN_IDM_DB("Record don't exist in DB, but exist in CSV"),
	NOT_QNIQUE_KEY("Defined key is not unique"),
	IDM_DELETED("IDM Record status is 'Deleted'"),
	LOGIN_NOT_FOUND("Login for current user is not founded"),
	MATCH_FOUND("Records is matched"),
	RESOURSE_DELETED("Resourse Deleted");
	
	String value;
	ReconciliationHTMLReportResults(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
