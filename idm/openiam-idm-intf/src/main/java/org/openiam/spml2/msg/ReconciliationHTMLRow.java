package org.openiam.spml2.msg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;

public class ReconciliationHTMLRow {
	private static final String HEADER_COLOR = "#a3a3a3";
	private static final String SUB_HEADER_COLOR = "#c3c3c3";
	private static final String RESULT_COLOR = "#e3e3e3";
	private static final String CONFLICT_COLOR = "#990000";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String row;

	public ReconciliationHTMLRow(String header) throws Exception {
		super();
		StringBuilder build = new StringBuilder();

		if (StringUtils.isEmpty(header) || header.split(",").length < 1) {
			throw new Exception("wrongHeader");
		}
		String head[] = header.split(",");
		build.append("<tr style='background-color:" + HEADER_COLOR + "'>");
		build.append("<td rowSpan='2' align='center'>");
		build.append("Case");
		build.append("</td>");
		build.append("<td align='center' colSpan='" + head.length + "'>");
		build.append("Fields name");
		build.append("</td>");
		build.append("</tr>");
		build.append("<tr style='background-color:" + HEADER_COLOR + "'>");
		for (String str : head) {
			build.append("<td>");
			build.append(str);
			build.append("</td>");
		}
		build.append("</tr>");
		row = build.toString();
	}

	public ReconciliationHTMLRow(String sepatatorText, int colSpan)
			throws Exception {
		StringBuilder build = new StringBuilder();
		build.append("<tr style='background-color:" + SUB_HEADER_COLOR + "'>");
		build.append("<td colSpan='" + colSpan + "' align='center'>");
		build.append(sepatatorText);
		build.append("</td>");
		row = build.toString();
	}

	public ReconciliationHTMLRow(String result, String values) throws Exception {
		super();
		StringBuilder build = new StringBuilder();

		if (StringUtils.isEmpty(result) || StringUtils.isEmpty(values)
				|| values.split(",").length < 1) {
			throw new Exception("wrong data");
		}
		List<String> vals = new ArrayList<String>(Arrays.asList(values
				.split(",")));
		if (values.charAt(values.length() - 1) == ',')
			vals.add("&nbsp;");
		build.append("<tr>");
		build.append("<td style='background-color:" + RESULT_COLOR + "'>");
		build.append(result);
		build.append("</td>");
		for (String str : vals) {
			if (str.contains("][")) {
				build.append("<td style='background-color:" + CONFLICT_COLOR
						+ "'>");
			} else
				build.append("<td>");
			build.append(str);
			build.append("</td>");
		}
		build.append("</tr>");
		row = build.toString();
	}

	public String toString() {
		return row;
	}
}
