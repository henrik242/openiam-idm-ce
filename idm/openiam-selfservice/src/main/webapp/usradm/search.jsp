<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%
	List statusList = (List)session.getAttribute("statusList");
	pageContext.setAttribute("statusList",statusList);
	
	List secondaryStatusList = (List)session.getAttribute("secondaryStatusList");
	pageContext.setAttribute("secondaryStatusList",secondaryStatusList);
	
	//List groupList = (List)session.getAttribute("groupList");
	List groupList = (List)request.getAttribute("groupList");
	pageContext.setAttribute("groupList",groupList);
	List roleList = (List)request.getAttribute("roleList");
	pageContext.setAttribute("roleList",roleList);
	String msg = (String)request.getAttribute("msg");
	
	List elementList = (List)request.getAttribute("elementList");
	pageContext.setAttribute("elementList",elementList);

    List orgList = (List)session.getAttribute("orgList");
    pageContext.setAttribute("orgList",orgList);
	
%>
<div id="content">
	<form name="searchForm" method="post" action="/selfservice/idman/userSearch.do?action=search" class="user-info">
		<fieldset>
			<div class="block">
				<div class="wrap alt">
					<div class="col-1">
						<div class="row alt">
							<label for="t-1">Last Name</label>
							<input name="lastName" type="text" size="30" maxlength="30">
						</div>
						<p class="info">'%' for wildcard searches</p>
						<div class="row">
							<label>Organization</label>
							<select name="companyName"><option value="100" selected="selected">OpenIAM LLC</option></select>
						</div>
						<div class="row">
							<label>Group</label>
							<select name="group"><option value="" selected="selected"></option>
								<option value="END_USER_GRP">End User Group</option>
								<option value="HR_GRP">HR Group</option>
								<option value="MNGR_GRP">Manager Group</option>
								<option value="402881103554446d0135545464a60001">My Group</option>
								<option value="402881103554446d0135545641c50004">My Group - a</option>
								<option value="SEC_ADMIN_GRP">Sec Admin Group</option>
								<option value="SECURITY_GRP">Security Group</option>
								<option value="SUPER_SEC_ADMIN_GRP">Super Admin Group</option>
							</select>
						</div>
						<div class="row">
							<label>Role</label>
							<select name="role"><option value=""></option>
								<option value="IDM*SEC_ADMIN">IDM-&gt;Security Admin</option>
								<option value="IDM*SUPER_SEC_ADMIN">IDM-&gt;Super Security Admin</option>
								<option value="USR_SEC_DOMAIN*END_USER" selected="selected">USR_SEC_DOMAIN-&gt;End User</option>
								<option value="USR_SEC_DOMAIN*HELPDESK">USR_SEC_DOMAIN-&gt;Help Desk</option>
								<option value="USR_SEC_DOMAIN*HR">USR_SEC_DOMAIN-&gt;Human Resource</option>
								<option value="USR_SEC_DOMAIN*LOCAL_ADMIN">USR_SEC_DOMAIN-&gt;LOCAL ADMIN</option>
								<option value="USR_SEC_DOMAIN*MANAGER">USR_SEC_DOMAIN-&gt;Manager</option>
								<option value="USR_SEC_DOMAIN*nurse">USR_SEC_DOMAIN-&gt;Nurse</option>
								<option value="USR_SEC_DOMAIN*radiologist">USR_SEC_DOMAIN-&gt;Radiologist</option>
								<option value="USR_SEC_DOMAIN*SECURITY_MANAGER">USR_SEC_DOMAIN-&gt;Security Manager</option>
								<option value="USR_SEC_DOMAIN*SEC_ADMIN">USR_SEC_DOMAIN-&gt;Security Admin</option>
								<option value="USR_SEC_DOMAIN*technologist">USR_SEC_DOMAIN-&gt;Technologist</option>
								<option value="USR_SEC_DOMAIN*Test Role">USR_SEC_DOMAIN-&gt;Modify Test Role</option>
							</select>
						</div>
						<div class="row">
							<label>Extended Attributes</label>
							<select name="attributeName"><option value="" selected="selected"></option>
								<option value="BusinessCategory">Contractor-&gt;BusinessCategory</option>
								<option value="Display Name">Contractor-&gt;Display Name</option>
								<option value="EndDate">Contractor-&gt;EndDate</option>
								<option value="Given Name">Contractor-&gt;Given Name</option>
								<option value="Initials">Contractor-&gt;Initials</option>
								<option value="LabeledURI">Contractor-&gt;LabeledURI</option>
								<option value="Preferred Language">Contractor-&gt;Preferred Language</option>
								<option value="StartDate">Contractor-&gt;StartDate</option>
								<option value="VehicleLicense">Contractor-&gt;VehicleLicense</option>
								<option value="inactive timeout">DIRUser-&gt;inactive timeout</option>
								<option value="manager">DIRUser-&gt;manager</option>
								<option value="org domain name">DIRUser-&gt;org domain name</option>
								<option value="Org Unit">DIRUser-&gt;Org Unit</option>
								<option value="permit-override">DIRUser-&gt;permit-override</option>
								<option value="preferred language">DIRUser-&gt;preferred language</option>
								<option value="BusinessCategory">InetOrgPerson-&gt;BusinessCategory</option>
								<option value="Display Name">InetOrgPerson-&gt;Display Name</option>
								<option value="Given Name">InetOrgPerson-&gt;Given Name</option>
								<option value="Initials">InetOrgPerson-&gt;Initials</option>
								<option value="LabeledURI">InetOrgPerson-&gt;LabeledURI</option>
								<option value="Preferred Language">InetOrgPerson-&gt;Preferred Language</option>
								<option value="VehicleLicense">InetOrgPerson-&gt;VehicleLicense</option>
								<option value="About">SPECTANTUser-&gt;About</option>
								<option value="Linkedin ID">SPECTANTUser-&gt;Linkedin ID</option>
							</select>
						</div>
					</div>
					<div class="col-1">
						 <div class="row alt">
							<label for="t-7">Email</label>
							<input name="email" type="text" id="email" size="20" maxlength="50">
						</div>
						<p class="info">'%' for wildcard searches</p>
						<div class="row alt">
							<label for="t-14">Phone</label>
							<input type="text" name="areaCode" maxlength="3" size="3" value="" class="code">
							<input type="text" name="phoneNumber" maxlength="10" size="10" value="" class="phone">
						</div>
						<p class="info">(AreaCode- Phone)</p>
						<div class="row">
							<label>User Status</label>
							<select name="status"><option value="" selected="selected"></option>
								<option value="ACTIVE">ACTIVE</option>
								<option value="APPROVAL_DECLINED">APPROVAL_DECLINED</option>
								<option value="DELETED">DELETED</option>
								<option value="INACTIVE">INACTIVE</option>
								<option value="LEAVE">LEAVE</option>
								<option value="PENDING_APPROVAL">PENDING_APPROVAL</option>
								<option value="PENDING_INITIAL_LOGIN">PENDING_INITIAL_LOGIN</option>
								<option value="PENDING_START_DATE">PENDING_START_DATE</option>
								<option value="PENDING_USER_VALIDATION">PENDING_USER_VALIDATION</option>
								<option value="RETIRED">RETIRED</option>
								<option value="TERMINATE">TERMINATE</option>
							</select>
						</div>
						<div class="row">
							<label>Secondary Status</label>
							<select name="secondaryStatus"><option value="" selected="selected"></option>
								<option value="DISABLED">DISABLED</option>
								<option value="LOCKED">LOCKED</option>
								<option value="LOCKED_ADMIN">LOCKED_ADMIN</option>
							</select>
						</div>
						<div class="row alt">
							<input type="text" name="attributeValue" maxlength="40" size="40" value="" class="long">
						</div>
						<p class="info">'%' for wildcard searches</p>
					</div>
					<div class="button">
						<input type="submit" value="Reset">
					</div>
					<div class="button">
						<input type="submit" value="Submit">
					</div>
				</div>
			</div>
		</fieldset>
	</form>
</div>