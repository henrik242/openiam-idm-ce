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
								<h2 class="contentheading">User Management</h2>
						</td>
						</tr>
					</table>
			</td>
				<tr>
				<td>		
<form:form commandName="userOrgCmd">

<form:hidden path="perId" />

	<table width="700pt">
			<tr>
				<td align="center" height="100%">
			     <fieldset class="userform" >
						<legend>SELECT ORG AFFILIATIONS FOR USER</legend>
	
		<table class="resourceTable" cellspacing="2" cellpadding="2" width="100%" >	
	
    		<table width="100%">
    			<tr class="header">
    				<th>Name</td>
    				<th>Alias</td>
    			</tr>
		      <c:forEach items="${userOrgCmd.orgList}" var="orgList" varStatus="org">
		  
				<tr>
								<td class="tableEntry">
									<form:checkbox path="orgList[${org.index}].selected" />
									${orgList.organizationName}
									<form:hidden path="orgList[${org.index}].orgId" />
								</td>
								<td class="tableEntry">${orgList.alias}</td>
				</tr>
				
			</c:forEach>
			</table>
	</td>	
	</tr>    
   


  <tr>
    <td colspan="4" align="right"> 
          <input type="submit" value="Save"/>   <input type="submit" name="_cancel" value="Cancel" />
    </td>
  </tr>
  
</table>
</TD>
</TR>
</TABLE>

</form:form>

	</td>
 </tr>
</table>
