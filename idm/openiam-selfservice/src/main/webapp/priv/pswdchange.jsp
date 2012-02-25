<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<% 
	String msg = (String)request.getAttribute("msg");
%>

			<div class="block">
				<div class="wrap">
					<form:form commandName="pswdChangeCmd">					
	<form:hidden path="domainId"    />
	<% if (msg != null ) { %>
			<p><%=msg %></p>
	<% }  %>
	
						
						<fieldset>
							<label for="t-1">Login ID:<span>*</span></label>
							<form:input path="principal" size="30"  maxlength="30" readonly="true"   />
							<label for="t-2">New Password:<span>*</span></label>
							<form:password path="password" size="30"  maxlength="30"   />
							<p> <form:errors path="password" /></p>
							<label for="t-3">Confirm New Password:<span>*</span></label>
							<form:password path="confPassword" size="30"  maxlength="30"   />
								<p><form:errors path="confPassword" cssClass="error" /></p>
							<div class="button">
								<input type="submit" name="_cancel" value="Cancel" />
							</div>
							<div class="button">
								<input type="submit" value="Save" name="btnSave"> 
							</div>
						</fieldset>
					</form:form> 
				</div>
			</div>