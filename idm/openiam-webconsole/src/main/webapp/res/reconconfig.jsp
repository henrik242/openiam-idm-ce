<%@ page language="java" contentType="text/html;"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>

<script>
	function showDialog() {
		if ($("#dialog").css('display') == 'none') {
			$("#dialog").css('left',
					($(document).width() - $("#dialog").width()) / 2);
			$("#dialog").css('top',
					($(document).height() - $("#dialog").height()) / 2);
			$("#dialog").css('display', 'block');
		} else {
			$("#dialog").css('display', 'none');
		}

	}
</script>
<table width="800pt">
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td class="pageTitle" width="70%">
						<h2 class="contentheading">Resource Management</h2>
					</td>
				</tr>
			</table>
		</td>
		<%
			String mode = request.getParameter("mode");
			if (mode != null) {
		%>
	
	<tr>
		<td class="msg" colspan="5" align="center"><b>Configuration
				has been successfully updated.</b></td>
	</tr>
	<%
		}
	%>

	<tr>
		<td><form:form commandName="reconCmd">

				<form:hidden path="config.reconConfigId" />
				<form:hidden path="config.resourceId" />

				<table width="950pt">
					<tr>
						<td align="center" height="100%">
							<fieldset class="userform">
								<legend>RECONCILIATION CONFIGURATION FOR RESOURCE:
									${reconCmd.res.name}</legend>

								<table class="fieldsetTable" width="100%">

									<tr>
										<td class="tddark">Status</td>
										<td class="tdlightnormal"><form:select
												path="config.status">
												<form:option value="ACTIVE" />
												<form:option value="IN-ACTIVE" />
											</form:select></td>
									</tr>

									<tr>
										<td class="tddark">Frequency</td>
										<td class="tdlightnormal"><form:select
												path="config.frequency" multiple="false">
												<form:option value="-" label="-Please Select-" />
												<form:option value="5MIN" label="Every 5 min" />
												<form:option value="15MIN" label="Every 15 min" />
												<form:option value="60MIN" label="Every 1 Hour" />
												<form:option value="NIGHTLY" label="Run Nightly" />
											</form:select></td>
									</tr>
									<c:if test="${reconCmd.isCSV}">
										<tr>
											<td class="tddark">Source CSV</td>
											<td class="tdlightnormal"><span
												style="color: #000099; text-decoration: underline;"
												onclick="showDialog();"> Upload CSV file</span></td>
										</tr>
									</c:if>
								</TABLE>
							</fieldset>
						</TD>
					</TR>
				</TABLE>

				<table width="950pt">
					<tr>
						<td align="center" height="100%">
							<fieldset class="userform">
								<legend>RECONCILIATION EVENT MAPPING</legend>
								<table class="resourceTable" cellspacing="2" cellpadding="2">
									<tr>
										<td colspan="2">
									<tr class="header">
										<td>Situation</td>
										<td>Response</td>
										<!--
    			<th>Script</td>
    			-->
									</tr>
									<c:forEach items="${reconCmd.situationList}"
										var="situationList" varStatus="sit">

										<tr class="tdlight">
											<td class="tableEntry">${situationList.situation} <form:hidden
													path="situationList[${sit.index}].reconConfigId" /> <form:hidden
													path="situationList[${sit.index}].reconSituationId" /> <form:hidden
													path="situationList[${sit.index}].situation" />
											</td>
											<td class="tableEntry"><form:select
													path="situationList[${sit.index}].situationResp">
													<form:option value="" label="-Please Select-" />
													<form:option value="NOTHING" label="DO NOTHING" />
													<form:option value="CREATE_IDM_ACCOUNT"
														label="CREATE IDM ACCOUNT" />
													<form:option value="CREATE_RES_ACCOUNT"
														label="CREATE RESOURCE ACCOUNT" />
													<form:option value="DEL_RES_ACCOUNT"
														label="DELETE RESOURCE ACCOUNT" />
													<form:option value="DEL_IDM_ACCOUNT"
														label="DELETE IDM ACCOUNT" />
													<form:option value="DEL_IDM_USER" label="DELETE IDM USER" />
													<form:option value="DEL_IDM_USER-NOT_TARGET"
														label="DELETE IDM USER-EXCLUDE TARGET SYSTEM" />
													<form:option value="UPD_IDM_USER" label="UPDATE USER" />
													<!--<form:option value="UPD_RESOURCE" label="UPDATE RESOURCE"/>-->
												</form:select></td>

											<td class="tableEntry"><form:input
													path="situationList[${sit.index}].script" size="30"
													maxlength="60" /></td>
										</tr>
									</c:forEach>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr align="right">
						<td colspan="2"><c:if test="${reconCmd.isCSV}">
								<input type="submit" name="btn" value="Import CSV" />
							</c:if> <c:if test="${reconCmd.config != null}">
								<input type="submit" name="btn" value="Reconcile Now"
									onclick="return confirm('Are your sure that you want to start reconciliation?');" />
							</c:if> <input type="submit" name="btn" value="Delete"
							onclick="return confirm('Are you sure you want to delete this Configuration?');" />
							<input type="submit" name="btn" value="Save" /> <input
							type="submit" name="_cancel" value="Cancel" /></td>
					</tr>
				</table></TD>
	</TR>
</TABLE>
</form:form>
</td>
</tr>
</table>


<div id="dialog"
	style="display: none; position: absolute; z-index: 120; background-color: #fff; border: 3px solid #74A824;">
	<h3 style="margin-left: 12px; margin-top: 12px;">Upload CSV file</h3>
	<form action="upload" enctype="multipart/form-data" method="POST">
		<input type="file" name="file1">
		<div style="margin-top: 12px;">
			<input type="submit" value="UploadFile" onclick="showDialog();" /><span
				style="color: #000099; margin-left: 12px; text-decoration: underline;"
				onclick="showDialog();">Cancel</span>
		</div>
	</form>
</div>



