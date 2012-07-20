<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



		<table  width="700pt">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td class="pageTitle" width="70%">
								<h2 class="contentheading">Resource - Entitlements</h2>
						</td>
						</tr>
					</table>
			</td>
<%
    String msg = (String)request.getAttribute("msg");
	if (msg != null) { %> 
   <tr>
 		<td class="msg" colspan="5" align="center"  >
 		  <b><%=msg %></b>
 		</td>
  </tr> 
<% } %>


			<tr>
				<td>

<form:form commandName="entitlementCmd">
          <form:hidden path="resId" />
          <form:hidden path="managedSysId" />

    <table width="650pt">
        <tr>
            <td align="center" height="100%">
                <fieldset class="userform" >
                    <legend>RESOURCE ENTITLEMENTS FOR: ${entitlementCmd.resourceName }</legend>

                    <table class="fieldsetTable"  width="100%" >

                        <tr>
                            <td class="tddark">Select Entitlement Type</td>
                            <td class="tdlightnormal">
                                <form:select path="privlegeType">
                                    <form:option value="" />
                                    <form:option value="ROLE" />
                                    <form:option value="GROUP" />
                                    <form:option value="MODULE / COMPONENT" />
                                    <form:option value="OTHER" />
                                </form:select>
                                <input type="submit" name="btn" value="Go" />
                            </td>
                        </tr>
                    </TABLE>
                </fieldset>
            </TD>
        </TR>
    </TABLE>

	<table  width="650pt">
			<tr>
				<td align="center" height="100%">
			     <fieldset class="userform" >

		<table class="resourceTable" cellspacing="2" cellpadding="2" >	
	
    
        <tr class="header">
			  <th></th>
			  <th>Entitlement Name</th>
			  <th>Description</th>

       </tr>
      	  <c:forEach items="${entitlementCmd.resourcePrivileges}" var="resourcePrivileges" varStatus="privilege">
   			
			<tr>
				<td class="tableEntry"><form:checkbox path="resourcePrivileges[${privilege.index}].selected" /> </td>
			    <td class="tableEntry"><form:hidden path="resourcePrivileges[${privilege.index}].resourcePrivilegeId" />
			    	<form:hidden path="resourcePrivileges[${privilege.index}].resourceId" />
                    <form:hidden path="resourcePrivileges[${privilege.index}].privilegeType" />
                    <form:input path="resourcePrivileges[${privilege.index}].name" maxlength="30" size="30" />
			     </td>
				<td class="tableEntry"><form:input path="resourcePrivileges[${privilege.index}].description" maxlength="40" size="40"/>  </td>

			</tr>
		</c:forEach>
	</table>

           <tr>
              <td  align="right"> <input type="submit" name="btn" value="Delete" onclick="return confirm('Are you sure you want to delete this entitlement?');" />  <input type="submit" name="btn" value="Save" /> <input type="submit" name="_cancel" value="Cancel" />  </td>
          </tr>
</table>
</TD>
</TR>
</TABLE>

</form:form>

	</td>
 </tr>
</table>

