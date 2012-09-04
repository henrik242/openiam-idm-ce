<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="requiredField" required="false"  rtexprvalue="true" %>
<%@ attribute name="errorMsg" required="false"  rtexprvalue="true" %>
<%@ attribute name="validator" required="false"  rtexprvalue="true" %>
<%@ attribute name="firstOptionValue" required="false"  rtexprvalue="true" %>
<%@ attribute name="firstOptionText" required="false"  rtexprvalue="true" %>
<%@ attribute name="options" required="true"  rtexprvalue="true" type="java.lang.Object" %>
<%@ attribute name="itemValue" required="true"  rtexprvalue="true" %>
<%@ attribute name="itemLabel" required="true"  rtexprvalue="true" %>

<c:set var="inputClass" value="${cssClass} " />
<c:if test='${requiredField==true}'>
	<c:set var="inputClass" value="${inputClass} required" />	
</c:if>
<c:set var="msg" value="" />
<c:if test='${errorMsg!=null && errorMsg!=""}'>
  <c:set var="msg"> <fmt:message key="${errorMsg}"/> </c:set>    
</c:if>

<div class="control-group" id="${name}ControlWrapper">
	<label class="span4 control-label <c:if test='${requiredField==true}'>required</c:if>" ><fmt:message key='${label}' />:</label>
	<div class="controls">
		<form:select path="${name}">
  			<form:option value="${firstOptionValue}"  label="${firstOptionText}" />
<%--   			<c:forEach items="${options}" var="opt"> --%>
<%--   				<form:option value="${opt[itemValue]}"  label="${opt[itemLabel]}" /> --%>
<%--   			</c:forEach> --%>
  			<form:options items="${options}" itemValue="${itemValue}" itemLabel="${itemLabel}"  />
        </form:select>

		<span class="help-inline"><form:errors path="${name}" cssClass="formError"/></span>
	</div>
</div>
