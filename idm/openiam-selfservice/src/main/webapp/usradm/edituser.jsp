<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<script type="text/javascript">
<!--


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

function showSupervisorDialog(idfield, namefield) {
	var ua = window.navigator.userAgent;
    var msie = ua.indexOf ( "MSIE " );

    if ( msie > 0 ) {	
		dialogReturnValue = window.showModalDialog("user/dialogshell.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
		document.getElementById (idfield).value = dialogReturnValue.id;
		document.getElementById (nameField).value = dialogReturnValue.name;
    }else {
    	dialogReturnValue = window.showModalDialog("user/selsupervisor.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
    	document.getElementById (idfield).value = dialogReturnValue.id;
    	document.getElementById (namefield).value = dialogReturnValue.name;
    }
}


//-->
</script>
<h4>Edit User Information</h4>
<form:form commandName="editUserCmd" cssClass="user-info">

<form:hidden path="user.userId" />
<form:hidden path="addressId" />

<fieldset>
					<div class="block">
						<div class="wrap alt">
						<div class="set">
							<div class="set-wrap">
								<label for="t-0">First Name<span>*</span></label>
								<form:input path="user.firstName" size="40" maxlength="40" onchange="firstName.value = firstName.value.toProperCase();" />  
								<p class="error"><form:errors path="user.firstName" cssClass="error" /> </p>
							</div>
							<div class="set-wrap">
								<label for="t-1">Middle</label>
								<form:input path="user.middleInit" size="10" maxlength="10"  onchange="middleName.value = middleName.value.toProperCase();" />
							</div>
							<div class="set-wrap">
								<label for="t-7">Last Name<span>*</span></label>
								<form:input path="user.lastName" size="40" maxlength="40" onchange="lastName.value = lastName.value.toProperCase();" /> 
								<p class="error"><form:errors path="user.lastName" cssClass="error" />  </p>										
							</div>
						</div>
						<div class="col">
								<div class="row">
									<label for="t-10">Nickname (AKA)</label>
									<form:input path="user.nickname" size="20"  maxlength="20"  onchange="nickname.value = nickname.value.toProperCase();" />
								</div>
								<div class="row">
										<label for="t-11">Functional Title</label>
									 	<form:input path="user.title" size="40" />
								</div>	
								<div class="row">
									<label for="t-11">Maiden Name</label>
									<form:input path="user.maidenName" size="20" maxlength="20"  onchange="maiden.value = maiden.value.toProperCase();"   />
								</div>
								<div class="row">
									<label for="t-11">Organization</label>
									<form:select path="user.companyId" multiple="false">
              						<form:option value="-" label="-Please Select-"/>
              						<form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
          						</form:select>  
          						<p class="error"><form:errors path="user.companyId"/> </p> 
								</div>								
								<div class="row">
									<label for="t-11">Division</label>
									<form:select path="user.division" multiple="false">
              							<form:option value="" label="-Please Select-"/>
              							<form:options items="${divList}" itemValue="orgId" itemLabel="organizationName"/>
          							</form:select>  
								</div>							

								<div class="row">
									<label for="t-11"><a href="javascript:showSupervisorDialog('supervisorId', 'supervisorName' );">Select Supervisor</a></label>
									 <form:hidden path="supervisorId" />
           							<form:input path="supervisorName" size="40" readonly="true" />            						  
								</div>	
								<div class="row">
									<label for="t-11"><a href="javascript:showSupervisorDialog('user.alternateContactId', 'alternateContactName' );">Select Alternate Contact</a></label>
									<form:hidden path="user.alternateContactId" />
            						<form:input path="alternateContactName" size="40" readonly="true" /> 
								</div>
								
															
								<div class="row">
									<label for="t-11">Start Date</label>
									<form:input path="user.startDate" size="20" />    
            						<p class="error"><form:errors path="user.startDate" /> </p>  
								</div>	
	
								<div class="row">
									<label for="t-11">DOB(MM/dd/yyyy)</label>
									<form:input path="user.birthdate" size="20" />
            						<p class="error"><form:errors path="user.birthdate" /> </p>  
								</div>		
        	
								<div class="row">
									<label for="t-11">Status</label>
									<form:input path="user.status" size="20" readonly="true" />
								</div>	
								

						</div>
							

						<!-- col 2 -->
						<div class="col">
								<div class="row">
									<label for="t-11">Suffix</label>
									<form:input path="user.suffix" size="20"  maxlength="20"  onchange="suffix.value = suffix.value.toProperCase();" />				
								</div>

								<div class="row">
									<label for="t-11">Employment Type</label>
			            			<form:select path="user.employeeType">
	            	 					<form:option value="-" label="-Please Select-"/>
	                  					<c:forEach items="${userTypeList}" var="userType">
		                				<form:option value="${userType.id.codeGroup}" label="${userType.description}" />
		              					</c:forEach>
          								</form:select>
	
								</div>		
								
								<div class="row">
									<label for="t-11">Gender</label>
									 <form:select path="user.sex">
				  						<form:option value="-" label="-Please Select-"  />
	              						<form:option value="M" label="Male" />
				  						<form:option value="F" label="Female" />
				  						<form:option value="D" label="Declined to State" />
          								</form:select>		
								</div>	
								<div class="row">
									<label for="t-11">Department</label>
				          			 	<form:select path="user.deptCd" multiple="false">
				              				<form:option value="-" label="-Please Select-"/>
				              				<form:options items="${deptList}" itemValue="orgId" itemLabel="organizationName"/>
				          				</form:select>  
								</div>		
								<div class="row">
									<label for="t-11">Job Code</label>
			               				<form:select path="user.jobCode">
			               		 			<form:option value="" label="-Please Select-"/>
				              	  			<c:forEach items="${jobCodeList}" var="jobCode">
					               				<form:option value="${jobCode.id.codeGroup}" label="${jobCode.description}" />
					              			</c:forEach>
			          					</form:select>   
								</div>	
								<div class="row">
									<label for="t-11">Employee ID</label>
									<form:input path="user.employeeId" size="40" /> 
								</div>	
								<div class="row">
									<label for="t-11">User Classification</label>
									<form:input path="user.classification" size="40" />
								</div>		
								<div class="row">
									<label for="t-11">Last Date</label>
									<form:input path="user.lastDate" size="20" />
									<p class="error"><form:errors path="user.lastDate" /></p>
								</div>
								<div class="row">
									<label for="t-11">Show in Directory</label>
									<form:select path="user.showInSearch">
				  						<form:option value="0" label="No"  />
	              						<form:option value="1" label="Yes" />
          							</form:select>
								</div>
								

								<div class="row">
									<label for="t-11">Secondary Status</label>
									<form:input path="user.secondaryStatus" size="20" readonly="true" />
								</div>	
						</div>
							
					</div>															
				</div>										
</fieldset>
<h4>Contact Information</h4>
<fieldset>
				<div class="block">
						<div class="wrap alt">
							<div class="col-1">
								<div class="row">
									<label>Building Name / Location</label>
									 <form:select path="user.locationCd" multiple="false" >
              							<form:option value="" label="-Please Select-"/>
              							<c:forEach items="${locationAry}" var="location">
                						<form:option value="${location.locationId}" label="${location.name}" />
              							</c:forEach>
          							</form:select>  
								</div>
								<div class="row">
									<label for="t-20">Bldg Num - Address</label>
									<form:input path="user.bldgNum" size="5" cssClass="code"  /> <form:input path="user.address1" size="20"  cssClass="phone" />
								</div>
								<div class="row">
									<label for="t-20">Address 2</label>
									<form:input path="user.address2" size="30"  />
								</div>
								<div class="row">
									<label for="t-22">City</label>
									<form:input path="user.city" size="30"  />
								</div>
								<div class="row">
									<label for="t-23">State</label>
									<form:input path="user.state" size="30" cssClass="code" />
								</div>
								
								<div class="row">
									<label for="t-24">Postal Code</label>
									<form:input path="user.postalCd" size="30" maxlength="10"  />
								</div>
							</div>
							
							<div class="col-1">
								<div class="row">
									<label for="t-18">Corporate Email</label>
									<form:input path="email1" size="40" maxlength="50"  />
      							 		<form:hidden path="email1Id" />
       									<form:hidden path="email2Id" />
       									<form:hidden path="email3Id" />
								</div>
								<div class="row">
									<label for="t-19">Personal Email</label>
									<form:input path="email2" size="40" maxlength="50"  />
								</div>

							</div>

							
						</div>
					</div> 
 
 
</fieldset>

<h4>Phone Numbers</h4>
<fieldset>
			<div class="block">
						<div class="wrap alt">
							<table class="tabl">
								<tbody>
									<tr>
										<th><h5>Label</h5></th>
                            			<th><h5>Area Code</h5></th>
                            			<th><h5>Phone Number</h5></th>
                           				<th><h5>Extension</h5></th>
                           				<th><h5>Default</h5></th>
									</tr>
									<c:forEach items="${editUserCmd.phoneList}" var="phone" varStatus="ph">
									<tr>
										<td><label for="f-2">
										${editUserCmd.phoneList[ph.index].name}
											<form:hidden path="phoneList[${ph.index}].phoneId" />
                                    		<form:hidden path="phoneList[${ph.index}].parentId" />
                                    		<form:hidden path="phoneList[${ph.index}].parentType" />
                                    		<form:hidden path="phoneList[${ph.index}].name" />              
										</td>
										<td> <form:input path="phoneList[${ph.index}].areaCd" size="5" maxlength="5"  cssClass="area" />  </td>
                              			<td> <form:input path="phoneList[${ph.index}].phoneNbr" size="15" maxlength="10" cssClass="number" />  </td>
                               			<td> <form:input path="phoneList[${ph.index}].phoneExt" size="10" maxlength="5" cssClass="extension" /> </td>
                              			<td>
                                  			<form:select path="phoneList[${ph.index}].isDefault">
                                      			<form:option value="0" label="No"/>
                                      			<form:option value="1" label="Yes"/>
                                			</form:select>
                             			 </td>                            
									</tr>
									   </c:forEach>
								</tbody>
							</table> 
 					</div>															
				</div>	
</fieldset>

<h4>Custom Attributes</h4>
<fieldset>
			<div class="block">
						<div class="wrap alt">
							<table class="tabl">
								<tbody>
									<tr>
										<th><h5>Attribute Name</h5></th>
										<th>
											<h5>Attribute Value</h5>
											<p class="error"> <form:errors path="attributeList" cssClass="error" /></p>
										</th>
									</tr>
									 <c:forEach items="${editUserCmd.attributeList}" var="userAttr" varStatus="attr">
									<tr>
										<td><label for="f-2">
											${editUserCmd.attributeList[attr.index].name}
							               <c:if test="${editUserCmd.attributeList[attr.index].required == true}" >
                  							 <font color="red">*</font>
               									</c:if>
               								</label>               								
               								<form:hidden path="attributeList[${attr.index}].id" />
               								<form:hidden path="attributeList[${attr.index}].userId" />
               								<form:hidden path="attributeList[${attr.index}].name" />
              								<form:hidden path="attributeList[${attr.index}].attrGroup"  />
               								<form:hidden path="attributeList[${attr.index}].metadataElementId"  />
               								<form:hidden path="attributeList[${attr.index}].required"  />
               
										</td>
										<td><form:input path="attributeList[${attr.index}].value" size="50" maxlength="50" /></td>
									</tr>
									   </c:forEach>
								</tbody>
							</table> 
 					</div>															
				</div>	

 
 
</fieldset>





<fieldset>
 <c:if test="${user.status != 'ACTIVE'}" >
 <div class="button">
  <input type="submit" name="saveBtn" value="ACTIVE"/>
 </div>
	</c:if>	    
    <c:if test="${user.status != 'DELETED'}" >
 <div class="button">
 <input type="submit" name="saveBtn" value="DELETE"/>
 </div> 
	</c:if>
 <div class="button">
   <input type="submit" name="saveBtn" value="DISABLE"/>
</div>
 <div class="button">
  <input type="submit" name="saveBtn" value="ENABLE"/> 
 </div>
  <div class="button">
	<input type="submit" name="saveBtn" value="Submit"/>  
 </div> 
 <div class="button">
	<input type="submit" name="_cancel" value="Cancel" /> 
 </div>  	  
</fieldset>

</form:form>

