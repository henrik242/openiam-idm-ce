<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<script type="text/javascript">
<!--

function defaultFields() {
	var theForm = document.getElementById ('newHireCmd');
	theForm.userPrincipalName.value = theForm.user.firstName.value + '.' + theForm.user.lastName.value  ;
}

String.prototype.toProperCase = function() 
{
    return this.charAt(0).toUpperCase() + this.substring(1,this.length).toLowerCase();
}




//-->
</script>

<form:form commandName="selfRegCmd">
<table width="620" border="0" cellspacing="2" cellpadding="1" align="center"> 
	<tr>
      <td colspan="4" class="title">         
          <strong>User Registration</strong>
      </td>
      <td colspan="2" class="text" align="right">         
          <font size="1" color="red">*</font> Required         
      </td>
   </tr>
   
   <tr>
 		<td colspan="6" align="center" bgcolor="8397B4" >
 		  <font></font>
 		</td>
  </tr> 
   <tr>
       <td class="tddarknormal" align="right"></td>
       <td class="tdlight" colspan="3">
			<font color="red"></font>
       </td>
    </tr>

   <tr>
       <td class="tddarknormal" align="right">First Name<font color="red">*</font></td>
       <td class="tdlightnormal">
			<form:input path="user.firstName" size="40" maxlength="40" onchange="defaultFields(); firstName.value = firstName.value.toProperCase();" />    
			<br><form:errors path="user.firstName" cssClass="error" /> 
		</td>
	   <td class="tddarknormal" align="right">Middle</td>
	   <td class="tdlightnormal">
			<form:input path="user.middleInit" size="10" maxlength="10"  onchange="middleName.value = middleName.value.toProperCase();" />
	  </td>
       <td class="tddarknormal" align="right">Last Name<font color="red">*</font></td>
       <td class="tdlightnormal">
			<form:input path="user.lastName" size="40" maxlength="40" onchange="defaultFields(); lastName.value = lastName.value.toProperCase();" /> 
			<form:errors path="user.lastName" cssClass="error" />  
		</td>
		
    </tr>
   <tr>
	  <td class="tddarknormal" align="right">Nickname</td>
	  <td>
			<form:input path="user.nickname" size="20"  maxlength="20"  onchange="nickname.value = nickname.value.toProperCase();" />
       </td>
	   <td class="tddarknormal" align="right">Maiden Name</td>
	   <td class="tdlightnormal">
			<form:input path="user.maidenName" size="20" maxlength="20"  onchange="maiden.value = maiden.value.toProperCase();"   />
	  </td>
	  <td class="tddarknormal" align="right">Suffix</td>
	  <td>
			<form:input path="user.suffix" size="20"  maxlength="20"  onchange="suffix.value = suffix.value.toProperCase();" />
       </td>
    </tr>
    <tr>
		 <td class="tddarknormal" align="right">Organization<font color="red">*</font></td>
		 <td class="tdlight" valign="middle">
		   <form:select path="user.companyId" multiple="false">
              <form:option value="" label="-Please Select-"/>
              <form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
          </form:select>
           <BR><form:errors path="user.companyId" cssClass="error" />   
          
		 </td>

        <td class="tddarknormal" align="right">Gender</td>
        <td class="tdlight" colspan="3">
         		<form:select path="user.sex">
				  <form:option value="-" label="-Please Select-"  />
	              <form:option value="M" label="Male" />
				  <form:option value="F" label="Female" />
          		</form:select>
          		<br><form:errors path="user.sex" cssClass="error" />  
        </td>
            
        </td>
     </tr>  
    <tr >
         <td class="tddarknormal"  align="right">Functional Title</td>
         <td class="tdlight">
         	<form:input path="user.title" size="40" /> 
         		<BR><form:errors path="user.title" cssClass="error" /> 
         </td>
		<td class="tddarknormal"  align="right" rowspan="2">Role<font color="red">*</font> </td>
        <td  class="tdlight" colspan="2" rowspan="2">
            <form:select path="role" multiple="true">
              <form:option value="-" label="-Please Select-"/>
              <c:forEach items="${roleList}" var="role">
                <form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.id.serviceId}-${role.roleName}" />
              </c:forEach>

          </form:select> 
         <form:errors path="role" cssClass="error" />        		
		
     </tr>


    <tr >
   		<td class="tddarknormal" align="right">DOB<br>(MM/dd/yyyy)</td>
        <td class="tdlight"><form:input path="user.birthdate" size="20" /><br>
        	<font color="red"><form:errors path="user.birthdate" /></font>
        </td>
    

    </tr>

 
   <tr>
   	<td colspan="6" class="tddark" align="center">Contact Information</td>
   </tr>

 <tr>
       <td class="tddarknormal" align="right">Corporate Email <font color="red">*</font></td>
       <td class="tdlight"><form:input path="email1" size="40" maxlength="40"  />
       <font color="red"><form:errors path="email1" /></font>

       </td>
       <td class="tddarknormal" align="right">Work Phone<font color="red">*</font></td>
       <td class="tdlight" colspan="3"><form:input path="workAreaCode" size="3"  maxlength="3" onblur="return validateInt(workAreaCode)"  /> 
       <form:input path="workPhone" size="10" maxlength="10" onblur="return  validateInt(workPhone)" /> 
      	<font color="red"><form:errors path="workAreaCode" />
       		<form:errors path="workPhone" />
       	</font>
       </td>
   </tr>
  <tr>
       <td class="tddarknormal" align="right">Bldg Num - Address</td>
       <td class="tdlight"><form:input path="user.bldgNum" size="5"  /> <form:input path="user.address1" size="20"  /></td>
       <td class="tddarknormal" align="right">Cell Phone</td>
       <td class="tdlight" colspan="3">
       		<form:input path="cellAreaCode" size="3" maxlength="3" onchange="return  validateInt(cellAreaCode);" /> 
       		<form:input path="cellPhone" size="10" maxlength="10"  onchange="return  validateInt(cellPhone);"/>
       	</td>
   </tr>
  <tr>
       <td class="tddarknormal" align="right"></td>
       <td class="tdlight"><form:input path="user.address2" size="30"  /></td>
       <td class="tddarknormal" align="right">Fax</td>
       <td class="tdlight" colspan="3">
			<form:input path="faxAreaCode" size="3" maxlength="3" onchange="return  validateInt(faxAreaCode);" /> 
			<form:input path="faxPhone" size="10" maxlength="10" onchange="return  validateInt(faxPhone);"/> 
      	<font color="red"> <form:errors path="faxAreaCode" />
       		<form:errors path="faxPhone" />
       	</font>   
       </td>
   </tr>
 	<tr>
       <td class="tddarknormal" align="right">City</td>
       <td class="tdlight"><form:input path="user.city" size="30"  /></td>
       <td class="tddarknormal" align="right"></td>
       <td class="tdlight" colspan="3">
       	</font>   
       </td>
   </tr>
     
 <tr>
       <td class="tddarknormal" align="right">State</td>
       <td class="tdlight"><form:input path="user.state" size="30"  /></td>
       <td class="tddarknormal" align="right">Emergency Contact:</td>
       <td class="tdlight" colspan="3"><form:input path="altCellAreaCode" size="3" maxlength="3" onchange="return  validateInt(altCellAreaCode);" /> 
       					  <form:input path="altCellNbr" size="10" maxlength="10"  onchange="return  validateInt(altCellNbr);" /> 
      	<font color="red"> <form:errors path="altCellAreaCode" />
       		<form:errors path="altCellNbr" />
       	</font>   
        </td>
   </tr> 

  <tr>
       <td class="tddarknormal" align="right">Zip Code</td>
       <td class="tdlight"><form:input path="user.postalCd" size="30" maxlength="10"  /></td>
      <td class="tddarknormal" align="right">Home Phone</td>
       <td class="tdlight" colspan="3"><form:input path="homePhoneAreaCode" size="3" maxlength="3" onchange="return  validateInt(homePhoneAreaCode);" /> 
       					  <form:input path="homePhoneNbr" size="10" maxlength="10" onchange="return  validateInt(homePhoneNbr);" /> 
      	<font color="red"> <form:errors path="homePhoneAreaCode" />
       		<form:errors path="homePhoneNbr" />
       	</font>
       </td>
   </tr>


    <tr>
    	  <td colspan="6">&nbsp;</td>
    </tr>
 
    <tr>
 		   <td colspan="6" align="center" bgcolor="8397B4" >
 		    <font></font>
 		   </td>
    </tr>

  <tr>
    <td colspan="6" align="right"> 
    	  <input type="submit" name="submit" value="Submit"/>   
    	  <input type="reset" value="Reset" />  
    </td>
  </tr>
  

</table>

</form:form>

