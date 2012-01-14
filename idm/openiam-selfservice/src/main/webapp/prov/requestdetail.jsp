<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<form:form commandName="requestDetailCmd">
     <table width="675pt" border="0" cellspacing="2" cellpadding="1" align="center">
	<tr>
      <td colspan="4" class="title">         
          <strong>Request Details</strong>
      </td>
   </tr>
   
   <tr>
 		<td colspan="4" align="center" bgcolor="8397B4" >
 		  <font></font>
 		</td>
  </tr> 
          <tr>
			  <td class="tddark">Request Id</td>
              <td ><form:input path="request.requestId" size="32" maxlength="32" readonly="true" /></td>
              <td>Request Date</td>
			  <td><form:input path="request.requestDate" size="32" maxlength="32" readonly="true" /></td>
		  </tr>
          <tr>
			  <td class="tddark">Status</td>
              <td ><form:input path="request.status" size="32" maxlength="32" readonly="true" /></td>
              <td>Status Change Date</td>
			  <td><form:input path="request.statusDate" size="32" maxlength="32" readonly="true"/></td>
		  </tr>
          <tr>
			  <td class="tddark">Requestor</td>
              <td colspan="3">${requestDetailCmd.requestor.firstName} ${requestDetailCmd.requestor.lastName} </td>
		  </tr>
          <tr>
			  <td class="tddark">Request Description</td>
              <td colspan="3">${requestDetailCmd.request.requestReason} </td>
		  </tr>
          <tr>
			  <td class="tddark">Request for User</td>
              <td colspan="3">
              	<table cellpadding="2">

	              	<tr class="tdlightnormal">
	              		<td>Name:</td>
	              		<td>${requestDetailCmd.userDetail.firstName} ${requestDetailCmd.userDetail.middleInit} ${requestDetailCmd.userDetail.lastName}</td>
	                </tr>
	                
	            	<tr class="tdlightnormal">
	              		<td>Title:</td>
	              		<td>${requestDetailCmd.userDetail.title}</td>
	                </tr>
	                
	                
	            	<tr class="tdlightnormal">
	              		<td>Organization:</td>
	              		<td>${requestDetailCmd.orgName}</td>
	                </tr>
	              	<tr class="tdlightnormal">
	              		<td>Phone:</td>
	              		<td>${requestDetailCmd.userDetail.areaCd}-${requestDetailCmd.userDetail.phoneNbr}</td>
	                </tr>
	              	<tr class="tdlightnormal">
	              		<td>Email:</td>
	              		<td>${requestDetailCmd.userDetail.email}</td>
	                </tr>
	                
              	</table>
              	</td>
           
		  </tr>
         <tr>
               <td class="tddark">Request Membership to Roles:</td>
               <td colspan="3">
                   <table cellpadding="2">

                   <c:forEach items="${requestDetailCmd.roleList}" var="role" >
                       <tr class="tdlightnormal">
                           <td>${role.roleName}</td>
                       </tr>
                   </c:forEach>
                   </table>
               </td>
           </tr>>


          <tr>
			  <td class="tddark">Request Membership to Groups:</td>
              <td colspan="3">
              	<table cellpadding="2">

	              <c:forEach items="${requestDetailCmd.groupList}" var="group" >
	              	<tr class="tdlightnormal">
	              		<td>${group.grpName}</td>
	              	</tr>
	              </c:forEach>
              	</table>
              </td>
		  </tr>

            <tr>
			  <td class="tddark">Comment</td>
              <td colspan="3">
                  <form:textarea path="comment" rows="5" cols="60" />
              </td>
		  </tr>
         <tr>
    	  <td colspan="4">&nbsp;</td>
    	</tr>
    	<tr>
 		   <td colspan="4" align="center" bgcolor="8397B4" >
 		    <font></font>
 		   </td>
    	</tr>
          <tr>
              <td colspan="4" align="right"> <input type="submit" name="btn" value="Approve"> <input type="submit" name="btn" value="Reject"> </td>
          </tr>
</table>

</form:form>
