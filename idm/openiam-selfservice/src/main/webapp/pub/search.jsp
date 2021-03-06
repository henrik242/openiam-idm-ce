<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%
	List statusList = (List)session.getAttribute("statusList");
	pageContext.setAttribute("statusList",statusList);
	List groupList = (List)session.getAttribute("groupList");
	pageContext.setAttribute("groupList",groupList);
	
	List orgList = (List)session.getAttribute("orgList");
	if (orgList == null)
		orgList = new ArrayList();
	pageContext.setAttribute("orgList", orgList);

	List roleList = (List)session.getAttribute("roleList");
	if (roleList == null)
		roleList = new ArrayList();
	pageContext.setAttribute("roleList", roleList);
	
%>

<br>

<html:form action = "/pub/directory.do?method=search">
  <table width="90%" border="0" cellspacing="2" cellpadding="1" align="center">
   <tr>
        <td class="title" colspan="6">Directory Lookup</td>
   </tr>
   <tr>
 		<td colspan="6" align="center" bgcolor="8397B4" >
 		  <font></font>
 		</td>
  </tr>
	<tr>
	  <td>&nbsp;</td>
  </tr>
             	
   <tr>
         <td class="tddark" align="right">Last Name:</td>
         <td class="tdlight" colspan="3">
             <input name="lastName" type="text" size="30" maxlength="40" />
         </td>         

   </tr>
   <tr>
         <td class="tddark" align="right">First Name:</td>
         <td class="tdlight" colspan="3">
             <input name="firstName" type="text" id="firstName" size="30" maxlength="40" />
         </td>         

   </tr>
   <tr>
         <td class="tddark" align="right">Phone Number (Desk Phone):</td>
         <td class="tdlight" colspan="3">
         	 <input name="phone_areaCd" type="text" size="8" />
             <input name="phone_nbr" type="text" size="18" maxlength="30" />
         </td>         
   </tr>
      
   <tr>
         <td class="tddark" align="right">Department / Agency</td>
         <td class="tdlight" colspan="3">
	         <html:select property="dept" >
	        	<html:options collection="orgList" property="value" labelProperty="label"/>
	     	</html:select> 
         </td>         
   </tr>
      
   
  <tr>
	  <td>&nbsp;</td>
 </tr>
 
  <tr>
 		<td colspan="4" align="center" bgcolor="8397B4" >
 		  <font></font>
 		</td>
  </tr>
    <tr>
          <td colspan="6" align ="right"  >
              <input type="submit" name="Submit" value="Lookup">&nbsp;
              <input type="reset" name="Reset" value="Cancel">
          </td>
   </tr>
 
  
    </table>
</html:form>


<jsp:include page="searchresult.jsp" flush="true" />


