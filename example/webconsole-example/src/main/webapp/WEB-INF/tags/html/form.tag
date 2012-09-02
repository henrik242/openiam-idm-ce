<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="modelAttribute" required="true" rtexprvalue="true" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="formUrl" required="false" rtexprvalue="true" %>
<%@ attribute name="formClass" required="false" rtexprvalue="true" %>
<spring:url value="${formUrl}" var="processedFormUrl" />
<form:form modelAttribute="${modelAttribute}" id="${id}" action="${processedFormUrl}" class="${formClass}">
		<fieldset>
	       <jsp:doBody />
	    </fieldset>
</form:form>