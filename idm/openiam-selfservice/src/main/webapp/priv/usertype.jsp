<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<div class="block alt">
<div class="wrap alt">
<h5>SELECT TYPE OF USER</h5>
<form:form commandName="newHireCmd" cssClass="type-user" >
	<fieldset>
						<form:select path="user.metadataTypeId" multiple="false">
              <form:option value="" label="-Please Select-"/>
              <form:options items="${metadataTypeAry}" itemValue="metadataTypeId" itemLabel="description"/>
             </form:select>
             <p class="error"><form:errors path="user.metadataTypeId" cssClass="error" /></p>

<div class="button">
	 <input type="submit" name="_cancel" value="Cancel" />
</div>
<div class="button">
	<input type="submit" name="_target1" value="Next"/>
</div>

</fieldset>
</form:form>
</div>
</div>