<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>




<% 
	String msg = (String)request.getAttribute("msg");
%>

<form:form commandName="pswdChangeCmd">
	<form:hidden path="domainId"    />
		
<% if (msg != null ) { %>
<label><%=msg %><span>*</span></label>
<% }  %>
	
	<label>Login ID:<span>*</span></label>
	<div class="text">
		<form:input path="principal" size="30"  maxlength="30" readonly="true"   />
	</div>
						
	<label>New Password:<span>*</span></label>
						<div class="text">
	    	<form:password path="password" size="30"  maxlength="30"   /><br>
    	 <form:errors path="password" cssClass="error" />
						</div>
  <label>Confirm New Password:<span>*</span></label>
						<div class="text">
       <form:password path="confPassword" size="30"  maxlength="30"   /><br>
        <form:errors path="confPassword" cssClass="error" />
						</div>
    <div class="button">
        <input type="submit" value="Save" name="btnSave">
    </div>

    <div class="button">
		<input type="submit" name="_cancel" value="Cancel"  />
	</div>

	

</form:form> 




