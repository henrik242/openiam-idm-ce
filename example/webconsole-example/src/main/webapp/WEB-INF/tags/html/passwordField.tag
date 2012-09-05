<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="requiredField" required="false"  rtexprvalue="true" %>
<%@ attribute name="errorMsg" required="false"  rtexprvalue="true" %>
<%@ attribute name="validator" required="false"  rtexprvalue="true" %>
<%@ attribute name="size" required="false"  rtexprvalue="true" %>
<%@ attribute name="maxlength" required="false"  rtexprvalue="true" %>
<%@ attribute name="readonly" required="false"  rtexprvalue="true" %>
<%@ attribute name="rowClass" required="false"  rtexprvalue="true" %>

<c:set var="inputClass" value="${cssClass} " />
<c:if test='${requiredField==true}'>
	<c:set var="inputClass" value="${inputClass} required" />	
</c:if>
<c:set var="msg" value="" />
<c:if test='${errorMsg!=null && errorMsg!=""}'>
  <c:set var="msg"> <fmt:message key="${errorMsg}"/> </c:set>    
</c:if>

<div class="control-group ${rowClass}" id="${name}ControlWrapper">
<c:if test='${label!=null && label!=""}'>
	<label class="span4 control-label <c:if test='${requiredField==true}'>required</c:if>" ><fmt:message key='${label}' />:</label>
</c:if>	
	<div class="controls">
		<form:password path="${name}" class="${inputClass}" errorMsg="${msg}" validator="${validator}"
		            size="${size}" maxlength="${maxlength}" readonly="${readonly}" />
		<span class="help-inline"><form:errors path="${name}" cssClass="formError"/></span>
	</div>
</div>
