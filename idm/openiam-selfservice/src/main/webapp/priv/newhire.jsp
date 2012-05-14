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


function validateInt(fld) {
   if (isNaN(fld.value)) {
		alert(fld.name + " is not a number");
		return false;
	}
	return true;
}

function showUserDialog(idfield, namefield) {
	var ua = window.navigator.userAgent;
    var msie = ua.indexOf ( "MSIE " );

    if ( msie > 0 ) {	
		dialogReturnValue = window.showModalDialog("user/dialogshell.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
		document.getElementById (idfield).value = dialogReturnValue.id;
		document.getElementById (nameField).value = dialogReturnValue.name;
    }else {
        var prevReturnValue = window.returnValue;
        window.returnValue = undefined;
        dialogReturnValue = window.showModalDialog("user/seluser.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
        if(dialogReturnValue == undefined) {
            dialogReturnValue = window.returnValue;
        }
        window.returnValue = prevReturnValue;

    	document.getElementById (idfield).value = dialogReturnValue.id;
    	document.getElementById (namefield).value = dialogReturnValue.name;
    }
}


function selectChange(ctrl) {
		var src = document.getElementById(ctrl);
		var theForm = document.getElementById ('profileCmd');
	
		
    
	   selIdx = src.options.selectedIndex;
	   newSel = src.options[selIdx].text;

	   if (newSel == '-Please Select-') {
		   theForm.bldgNbr.value = ""; 
		   theForm.address1.value = ""; 
		   theForm.city.value = ""; 
		   theForm.state.value = ""; 
		   theForm.postalCode.value = "";
	   }else {
	   
		   var spltStr = newSel.split('-');
	
		   theForm.bldgNbr.value = spltStr[1]; 
		   theForm.address1.value = spltStr[2]; 
		   theForm.city.value = spltStr[3]; 
		   theForm.state.value = spltStr[4]; 
		   theForm.postalCode.value = spltStr[5];
	   } 
	}

//-->
</script>
	
<form:form commandName="newHireCmd" cssClass="profile">
	<h4>New User Detail</h4>
	<fieldset>
					<div class="block">
						<div class="wrap alt">
						<div class="set">
							<div class="set-wrap">
								<label for="t-0">First Name<span>*</span></label>
								<form:input path="user.firstName" size="40" maxlength="40" onchange="defaultFields(); firstName.value = firstName.value.toProperCase();" /> 
								<p class="error"><form:errors path="user.firstName" /> </p>
							</div>
							<div class="set-wrap">
								<label for="t-1">Middle</label>
								<form:input path="user.middleInit" size="10" maxlength="10"  onchange="middleName.value = middleName.value.toProperCase();" />
							</div>
							<div class="set-wrap">
								<label for="t-7">Last Name<span>*</span></label>
								<form:input path="user.lastName" size="40" maxlength="40" onchange="defaultFields(); lastName.value = lastName.value.toProperCase();" />
									<p class="error"><form:errors path="user.lastName"/> </p> 
							</div>
						</div>
						<div class="col">
							<div class="row">
									<label>Organization<span>*</span></label>
									   <form:select path="user.companyId" multiple="false">
              				<form:option value="-" label="-Please Select-"/>
              				<form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
          					</form:select> 
          					<p class="error"><form:errors path="user.companyId" />    </p>  
								</div>
								<div class="row">
									<label>Organizational Title</label>
									<form:input path="user.title" size="40" maxlength="40" /> 

								</div>
								<div class="row">
									<label><a href="javascript:showUserDialog('supervisorId', 'supervisorName' );">Select Supervisor</a></label>
									<form:hidden path="supervisorId" />
            			<form:input path="supervisorName" size="50" readonly="true" /> 
									<p class="error"><form:errors path="supervisorId"/> </p>  
								</div>

								<div class="row">
									<label>Start Date</label>
									<form:input path="user.startDate" size="20" />
									<p class="error"><form:errors path="user.startDate" /> </p>  
								</div>
								<div class="row">
									<label>Role</label>
						      <form:select path="role" multiple="false">
              			<form:option value="-" label="-Please Select-"/>
              			<c:forEach items="${roleList}" var="role">
                			<form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.id.serviceId}-${role.roleName}" />
              			</c:forEach>
          				</form:select> 
          				<p class="error"><form:errors path="role" />   </p>  
          
								</div> 
								


						</div>
						<div class="col">
							<div class="row">
									<label>Department</label>
									     <form:select path="user.deptCd" multiple="false">
              					<form:option value="-" label="-Please Select-"/>
              					<form:options items="${deptList}" itemValue="orgId" itemLabel="organizationName"/>
          						</form:select>       
							</div>
							<div class="row">
									<label>Job Classification</label>
 									<form:select path="user.jobCode">
               		 	<form:option value="" label="-Please Select-"/>
	              	  	<c:forEach items="${jobCodeList}" var="jobCode">
		                	<form:option value="${jobCode.id.codeGroup}" label="${jobCode.description}" />
		              	</c:forEach>
          				</form:select>      
          		</div>
							<div class="row">
									<label>Employment Type</label>
 									<form:select path="user.employeeType">
	            	  <form:option value="-" label="-Please Select-"/>
	                  <c:forEach items="${userTypeList}" var="userType">
		                <form:option value="${userType.id.codeGroup}" label="${userType.description}" />
		              </c:forEach>
          				</form:select>   
          		</div>						
								<div class="row">
									<label>Last Date</label>
									<form:input path="user.lastDate" size="20" />
									<p class="error"><form:errors path="user.lastDate" /> </p>  
								</div>

								<div class="row">
									<label>Group</label>
										<form:select path="group" multiple="false">
              			<form:option value="" label="-Please Select-"/>
              			<form:options items="${groupList}" itemValue="grpId" itemLabel="grpName"/>
          				</form:select> 
          
								</div> 
						</div>
						<table>
							<c:forEach items="${newHireCmd.attributeList}" var="userAttr" varStatus="attr">
		  
				<tr>
				
					<td class="tddarknormal" align="right">${newHireCmd.attributeList[attr.index].name}
                        <c:if test="${newHireCmd.attributeList[attr.index].required == true}" >
                            <font color="red">*</font>
                        </c:if>
						<form:hidden path="attributeList[${attr.index}].id" />
						<form:hidden path="attributeList[${attr.index}].userId" />
						<form:hidden path="attributeList[${attr.index}].name" />						
						<form:hidden path="attributeList[${attr.index}].attrGroup"  />
					 	<form:hidden path="attributeList[${attr.index}].metadataElementId"  />
                        <form:hidden path="attributeList[${attr.index}].required"  />
					</td>
					<td class="tdlightnormal" colspan="5"><form:input path="attributeList[${attr.index}].value" size="50" maxlength="50" />
					</td>


				</tr>
				
			</c:forEach>
		</table>
			
						</div>
					</div>
				</fieldset>
				<h4>Contact Information</h4>
				<fieldset>
					<div class="block">
						<div class="wrap alt">
							<div class="col">
								
								<div class="row">
									<label>Location</label>
									<form:select path="user.locationCd" multiple="false" onchange="selectChange('locationBldg');">
              		<form:option value="-" label="-Please Select-"/>
              		<c:forEach items="${locationAry}" var="location">
                		<form:option value="${location.locationId}" label="${location.name}-${location.bldgNum}-${location.address1}-${location.city}-${location.state}-${location.postalCd} " />
              		</c:forEach>
          			</form:select>  
                 
								</div> 
	
								<div class="row">
									<label>Building Nbr - Address</label>
									<form:input path="user.bldgNum" size="5" maxlength="5" cssClass="code" /> 
										<form:input path="user.address1" size="20" maxlength="30" cssClass="phone" />
                 
								</div> 
								<div class="row">
									<label>Address 2</label>
									<form:input path="user.address2" size="30" maxlength="30"  />
                 
								</div> 								
								<div class="row">
									<label>City</label>
									<form:input path="user.city" size="30"  maxlength="30" />
                 
								</div>
								<div class="row">
									<label>State</label>
									<form:input path="user.state" size="30" maxlength="30"  />
                 
								</div>
								<div class="row">
									<label>Postal Code</label>
									<form:input path="user.postalCd" size="30" maxlength="10"  />
                 
								</div>
																
							</div>
							<div class="col">
							
							<div class="row">
									<label>Corporate Email</label>
									<form:input path="email1" size="40" maxlength="40"  />
								</div> 
							
							<div class="row">
									<label>Office Phone</label>
									<form:input path="workAreaCode" size="3"  maxlength="3" onblur="return validateInt(workAreaCode)" cssClass="code"  /> 
       						<form:input path="workPhone" size="10" maxlength="10" onblur="return  validateInt(workPhone)" cssClass="phone" /> 
       	          <p class="error"><form:errors path="workPhone" /></p> 
							</div> 
							<div class="row">
									<label>Mobile Phone</label>
									<form:input path="cellAreaCode" size="3" maxlength="3" onchange="return  validateInt(cellAreaCode);"cssClass="code" /> 
       						<form:input path="cellPhone" size="10" maxlength="10"  onchange="return  validateInt(cellPhone);" cssClass="phone" />
								</div>	
							<div class="row">
									<label>Fax</label>
										<form:input path="faxAreaCode" size="3" maxlength="3" onchange="return  validateInt(faxAreaCode);"  cssClass="code" /> 
										<form:input path="faxPhone" size="10" maxlength="10" onchange="return  validateInt(faxPhone);" cssClass="phone"/> 
								</div>
							</div>								
						</div>
					</div>
						

				<div class="button alt">
						<input type="submit" name="_cancel" value="Cancel" />  
				</div>

				<div class="button">
						<input type="submit" name="_finish" value="Submit"/>
				</div>
				<div class="button">
						 <input type="submit" name="_target0" value="Prev"/>
				</div>					
					
	</fieldset>

</form:form>
