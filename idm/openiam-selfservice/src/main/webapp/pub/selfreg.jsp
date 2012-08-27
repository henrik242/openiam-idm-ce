<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<form:form commandName="selfRegCmd" cssClass="user-info">
				<fieldset>
					<div class="block">
						<div class="wrap alt">

							<!-- column 1  -->
							
							<div class="col-1">
								<div class="row alt">
									<label for="t-1">First Name<span>*</span></label>
									<form:input path="user.firstName" size="40" maxlength="40" />
								</div>
								<p class="error"><form:errors path="user.firstName" /></p>
								<div class="row alt">
									<label for="t-2">Middle<span></span></label>
									<form:input path="user.middleInit" size="10" maxlength="10"  />
								</div>
								<div class="row alt">
									<label for="t-1">Last Name<span>*</span></label>
									<form:input path="user.lastName" size="40" maxlength="40" />
								</div>
								<p class="error"><form:errors path="user.lastName" /></p>
                                <div class="row">
									<label for="t-4">Maiden Name<span></span></label>
									<form:input path="user.maidenName" size="20"  maxlength="20" />
								</div>
                                
								<div class="row alt">
									<label for="t-1">Nickname<span></span></label>
									<form:input path="user.nickname" size="20" maxlength="20" />
								</div>
								<div class="row alt">
									<label for="t-1">DOB(MM/dd/yyyy)</label>
									<form:input path="user.birthdate" size="20"/>
								</div>
                                <p class="error"><form:errors path="user.birthdate" /></p>
							      
							    <div class="row alt">
									<label for="t-1">Organization<span>*</span></label>
									<form:select path="user.companyId" multiple="false">
										<form:option value="" label="-Please Select-"/>
									    <form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
									</form:select>
								</div>
	                            <p class="error"><form:errors path="user.companyId" /></p>
								<div class="row alt">
									<label for="t-1">Functional Title</label>
									<form:input path="user.title" size="40" maxlength="40" />
								</div>
	                            <p class="error"><form:errors path="user.title" /></p>
                                <div class="row">
									<label for="t-4">Gender</label>
									<form:select path="user.sex">
									  <form:option value="-" label="-Please Select-"  />
									  <form:option value="M" label="Male" />
									  <form:option value="F" label="Female" />
									</form:select>
								</div>
								<p class="error"><form:errors path="user.sex" /></p>
								<div class="row alt">
									<label>Role<span>*</span></label>
				          <form:select path="role" multiple="false">
								    <c:forEach items="${roleList}" var="role">				
								        <c:if test="${role.provisionObjName != 'NO'}" >				
								            <form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.roleName}" />				
								        </c:if>
								    </c:forEach>
				          </form:select> 
          				<p><form:errors path="role"/></p>        		
								</div>
                               
								</div>							
		
							
							<!-- column 2  -->
							
							<div class="col-1">
								
															

							</div>
						</div>
					</div>
				</fieldset>
			
			
			
    
   	
				<h4>Contact Information</h4>
				<fieldset>
					<div class="block">
						<div class="wrap alt">
							
							<!-- Column 1 -->

                            <div class="col-1">
								
								<div class="row">
									<label for="t-13">Corporate Email <font color="red">*</font></label>
									<form:input path="email1" size="40" maxlength="40"  />

								</div>
                                <p class="error"><form:errors path="email1" /></p>
								<div class="row">
									<label for="t-14" >Address</label>
									<form:input path="user.address1" size="20"  />
								</div>
								<div class="row">
									<label for="t-15"> </label>
									<form:input path="user.address2" size="30"  />
								</div>

								<div class="row">
									<label for="t-16">City</label>
									<form:input path="user.city" size="30"  />
								</div>
								<div class="row">
									<label for="t-17">State</label>
									<form:input path="user.state" size="30"  />
								</div>								
								<div class="row">
									<label for="t-18">Zip</label>
									<form:input path="user.postalCd" size="30" maxlength="10"  />
								</div>	
							</div>
							
							
							<!-- Column 2 -->

                            <div class="col-1">
								<div class="row">
									<label for="t-19">Work Phone</label>
									<form:input path="workAreaCode" size="3"  maxlength="3" cssClass="code"  /> 
       						        <form:input path="workPhone" size="10" maxlength="10" onblur="return  validateInt(workPhone)" cssClass="phone" />
								</div>
                                <p class="error"><form:errors path="workAreaCode" /></p>
								<div class="row">
									<label for="t-21">Mobile Phone</label>									
									<form:input path="cellAreaCode" size="3" maxlength="3" cssClass="code" /> 
       						<form:input path="cellPhone" size="10" maxlength="10" cssClass="phone"/>								
								</div>
	
							   <div class="row">
									<label for="t-22">Fax</label>									
									<form:input path="faxAreaCode" size="3" maxlength="3" cssClass="code" /> 
       								<form:input path="faxPhone" size="10" maxlength="10" cssClass="phone"/>								
								</div>
							<div class="row">
								<label for="t-22">Emergency Contact</label>									
								<form:input path="altCellAreaCode" size="3" maxlength="3" cssClass="code" /> 
	       						<form:input path="altCellNbr" size="10" maxlength="10" cssClass="phone"/>								
							</div>
							<p class="error"><form:errors path="altCellAreaCode" /></p>
							<div class="row">
								<label for="t-22">Home Phone</label>									
								<form:input path="homePhoneAreaCode" size="3" maxlength="3" cssClass="code" /> 
		       					<form:input path="homePhoneNbr" size="10" maxlength="10" cssClass="phone"/>								
							</div>
							<p class="error"><form:errors path="homePhoneAreaCode" /></p>
										
			
							</div>
						</div>
					</div>
					<div class="button alt">
                        <input type="submit" name="_cancel" value="Cancel" />
					</div>
					<div class="button">
						<input type="submit" value="Submit" />
					</div>
				</fieldset>
			</form:form>

