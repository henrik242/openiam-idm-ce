<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    String mode = request.getParameter("mode");

%>


		<table  width="700pt">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td class="pageTitle" width="70%">
								<h2 class="contentheading">Role Management</h2>
						</td>
						</tr>
					</table>
			    </td>
            </tr>
            <% if ("1".equals(mode)) { %>
            <tr>
                <td><font color="red">The information has been successfully saved.<br>
                    To exit this screen press [Cancel]</font>
                </td>
            </tr>
            <% } %>

			<tr>
				<td>       
<form:form commandName="roleDetailCmd">
	<form:hidden path="formMode" />
<table width="700pt"  class="bodyTable" height="100%" >
<tr>
	<td>    
			<fieldset class="userformSearch" >
			<legend>ROLE DETAILS FOR: ${roleDetailCmd.role.roleName} </legend>

				<table class="fieldsetTable"  width="100%" >

          <tr>
			  <td><label for="username" class="attribute">Security Domain</label></td>
              <td><form:input path="role.id.serviceId" size="32" maxlength="20" readonly="true" /></td>
          </tr>
          <tr>
			  			<td><label for="username" class="attribute">Role ID</label><font color="red">*</font></td>
              <td >
<c:if test="${roleDetailCmd.formMode == 'NEW'}" >
              <form:input path="role.id.roleId" size="32" maxlength="32" />
</c:if>
<c:if test="${roleDetailCmd.formMode != 'NEW'}" >
              <form:input path="role.id.roleId" size="32" maxlength="32" readonly="true" />

</c:if>
                  <form:errors path="role.id.roleId" cssClass="error" /></td>
          </tr>

          <tr>
              <td><label for="username" class="attribute">Name</label><font color="red">*</font></td>
			 			  <td><form:input path="role.roleName" size="40" maxlength="40"  />
			  <form:errors path="role.roleName" cssClass="error" /><br>
			  </td>
		  </tr>
          <tr>
			  			<td><label for="username" class="attribute">Description</label></td>
              <td><form:input path="role.description" size="40" maxlength="80" /></td>
          </tr>
          <tr>
              <td><label for="username" class="attribute">Role Type</label></td>
			  			<td>
			  			<form:select path="role.metadataTypeId">
			  			<form:option value=""  label="-Select a value" />
			  			<form:options items="${roleDetailCmd.typeList}" itemValue="metadataTypeId" itemLabel="description"  />
		            </form:select>
			 			 </td>
		  </tr>
        <tr>
              <td><label for="username" class="attribute">Role Status</label></td>
			  			<td>
			  		<form:select path="role.status">
			  			<form:option value=""  label="-Select a value" />
			  			<form:option value="ACTIVE"  label="ACTIVE" />
			  			<form:option value="DELETED"  label="DELETED" />
			  			<form:option value="PENDING_APPROVAL"  label="PENDING APPROVAL" />
		            </form:select>
			  </td>
		  </tr>

            <tr>
                <td><label for="username" class="attribute">Role Visible in Self Service</label></td>
                <td>
                    <form:select path="role.provisionObjName">
                        <form:option value=""  label="-Select a value" />
                        <form:option value="YES"  label="YES" />
                        <form:option value="NO"  label="NO" />
                    </form:select>
                </td>
            </tr>


          <tr>
              <td><label for="username" class="attribute">Role Owner</label></td>
			  			<td><form:input path="role.ownerId" size="40" maxlength="40" /></td>
		  		</tr>

          <tr>
              <td><label for="username" class="attribute">Inherit From Parent</label></td>
			  		<td>
			  		<form:select path="role.inheritFromParent">
			  			<form:option value=""  label="-Select a value" />
			  			<form:option value="0"  label="NO" />
			  			<form:option value="1"  label="YES" />
		            </form:select>
			  </td>
		  </tr>		  
          <tr>
              <td><label for="username" class="attribute">Create</label></td>
			  			<td><label for="username" class="attribute">On</label> ${roleDetailCmd.role.createDate} by ${roleDetailCmd.role.createdBy} 
			  </td>
		  </tr>

                <tr>
                    <td colspan="2">

                        <table width="600pt" >
                            <tr>
                                <td align="center" height="100%">
                                    <fieldset class="userform" >
                                        <legend>CUSTOM ATTRIBUTES </legend>
                                        <table class="resourceTable" cellspacing="2" cellpadding="2" width="600pt">

                                            <tr class="header">
                                                <th>Name</th>
                                                <th>Value</th>
                                            </tr>

                                            <c:if test="${roleDetailCmd.attributeList != null}" >
                                                <c:forEach items="${roleDetailCmd.attributeList}" var="attributeList" varStatus="attr">
                                                    <tr>
                                                        <td><form:input path="attributeList[${attr.index}].name" size="20"  />

                                                            <form:hidden path="attributeList[${attr.index}].roleAttrId" />
                                                            <form:hidden path="attributeList[${attr.index}].roleId" />
                                                            <form:hidden path="attributeList[${attr.index}].serviceId" />

                                                        </td>
                                                        <td>
                                                            <form:input path="attributeList[${attr.index}].value" size="40" maxlength="200" /> </td>
                                                    </tr>

                                                </c:forEach>
                                            </c:if>
                                            <tr>
                                                <td colspan="2"><i>To remove a custom attribute, leave the name blank</i><br>
                                                    <i>To add a new row, click on SAVE first. A new row will be added.</i></td>
                                            </tr>
                                        </table>
                                        </fieldset>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>



                </TABLE>


          <tr class="buttonRow" align="right">
              <td c align="right">
              <c:if test="${roleDetailCmd.role.id != null}" >
                <input type="submit" name="btn" value="Re-Synchronize" onclick="return confirm('Are you sure you want to Re-Synchronize this Role');">

              	<input type="submit" name="btn" value="Delete" onclick="return confirm('Are you sure you want to delete this Role');">
              </c:if>
              <input type="submit" name="btn" value="Save"> <input type="submit" name="_cancel" value="Cancel" /></td>
          </tr>
        </fieldset>
</table>
</form:form>

	</td>
 </tr>
</table>
