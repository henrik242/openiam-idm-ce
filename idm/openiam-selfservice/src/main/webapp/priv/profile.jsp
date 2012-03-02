<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<%@ page import="org.openiam.idm.srvc.auth.dto.Login" %>


<form:form commandName="profileCmd" cssClass="profile">

	<form:hidden  path="user.userId" />
	<form:hidden path="secdomain" />
	<form:hidden path="managedSysId" />
	<form:hidden path="email1Id"   />
	<form:hidden path="email2Id"   />
	<form:hidden path="email3Id"   />
		
	<form:hidden path="workPhoneId" />
	<form:hidden path="cellPhoneId" />
	<form:hidden path="faxPhoneId" />
	<form:hidden path="homePhoneIdr" />
	<form:hidden path="altCellNbrId" />
	<form:hidden path="personalNbrId" />

			<fieldset>
				<div class="block">
					<div class="wrap alt">

						<!-- column 1  -->
						
						<div class="col">
							<div class="row">
								<label for="t-1">First Name<span>*</span></label>
									<form:input path="user.firstName" size="40" maxlength="40"  />       
							</div>
							
							<div class="row">
								<label for="t-2">Primary Phone <span>*</span> </label>
								<form:input path="workAreaCode" size="3" cssClass="code"  /> 
   						<form:input path="workPhone" size="10" cssClass="phone" /> 
  	
							</div>

							<div class="row">
								<label for="t-3">Fax <span>*</span> </label>
								<form:input path="faxAreaCode" size="3" maxlength="3" cssClass="code" /> 
								<form:input path="faxPhone" size="10" maxlength="10" cssClass="phone"/> 

							</div>

							 <div class="row">
								<label for="t-4">Primary Email<span>*</span></label>
								<form:input path="email1"   />
							</div>

						
							
							</div>							

					<div class="col">
							<div class="row">
								<label for="t-7">Last Name<span>*</span></label>
								<form:input path="user.lastName"   /> 
							</div>				
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


		










