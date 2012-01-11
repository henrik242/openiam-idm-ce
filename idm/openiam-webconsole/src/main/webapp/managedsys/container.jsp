<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<% 
	String connectorPanel = (String)request.getAttribute("connectorPanel");
	if (connectorPanel == null || connectorPanel.length() ==0) {
		connectorPanel = "/managedsys/sysconnection.jsp";
	}
	
%>
<jsp:include page="<%=connectorPanel %>" flush="true" />

 
        
