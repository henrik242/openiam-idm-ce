<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



   		<table  width="600pt" >
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
               <% if (request.getAttribute("msg") != null) { %>
               <tr>
                   <td colspan="2" align="center" class="msg" >
                       <%=request.getAttribute("msg") %>
                   </td>
               </tr>
               <% } %>

               <tr>
				<td>       
	
<form:form commandName="roleResCmd">
	<form:hidden path="roleId" />
	<form:hidden path="domainId" />

    <table width="600pt">
        <tr>
            <td align="center" height="100%">
                <fieldset class="userform" >
                    <legend>SELECT RESOURCE TYPE</legend>

                    <table class="fieldsetTable"  width="600pt" >

                        <tr>
                            <td class="plaintext">Select Resource Type:</td>
                            <td>

                                <form:select path="resourceTypeId" multiple="false">
                                    <form:option value="" label="-Please Select-"/>
                                    <form:options items="${resTypeList}" itemValue="resourceTypeId" itemLabel="description"/>
                                </form:select>

                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="error"><form:errors path="resourceTypeId" /></td>
                        </tr>
                    </table>
                </fieldset>
        </tr>
        <tr class="buttonRow">
            <td align="right"> <input type="submit" name="formBtn" value="Search"> <input type="submit" name="_cancel" value="Cancel" />  </td>
        </tr>
    </table>



    <table width="600pt">
			<tr>
				<td align="center" height="100%">
			     <fieldset class="userform" >
						<legend>ASSOCIATE RESOURCES TO A ROLE</legend>

   
     	<table class="resourceTable" cellspacing="2" cellpadding="2" width="600pt">
    			<tr class="header">
    				<th>Resource Name</th>
    				<th></th>

    			</tr>
             <c:if test="${roleResCmd.resourceList != null }" >
             <c:forEach items="${roleResCmd.resourceList}" var="resourceList" varStatus="res">

				<tr class="plaintext">
								<td class="tableEntry">
                                    <form:checkbox path="resourceList[${res.index}].selected" />${resourceList.name}
								</td>
								<td class="tableEntry">
                                    <c:choose>
                                        <c:when test="${!resourceList.selected}">
                                            <a href="updateRoleMembership.cnt?objtype=RES&action=ADD&role=${roleResCmd.roleId}&domain=${roleResCmd.domainId}&objid=${resourceList.resourceId}">ADD to Role</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="updateRoleMembership.cnt?objtype=RES&action=DEL&role=${roleResCmd.roleId}&domain=${roleResCmd.domainId}&objid=${resourceList.resourceId}">REMOVE from Role</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
				</tr>

			</c:forEach>
             </c:if>
			</table>
         </fieldset>
	</td>	
	</tr>    
</table>
</form:form>
        </td>
       </tr>
     </table>
</div>
