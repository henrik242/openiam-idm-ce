
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%
    session.invalidate();
%>

<script type="text/javascript">
    <!--
    url = '<%= request.getContextPath() %>' + '/pub/finallogout.jsp';
    window.location.href =  url;
    //-->
</script>

<table border="0" width="40%" align="center">
    <tr>
        <td>You have successfully logged out</td>
    </tr>
    <tr>
        <td>For your security we recommend you close your browser.</td>
    </tr>

</table>




