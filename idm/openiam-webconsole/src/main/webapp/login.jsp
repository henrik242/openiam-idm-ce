<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<form:form commandName="loginCmd" autocomplete="off" >

    <form:hidden path="clientIP"   />

<table border="0" width="600" align="center">
  <% if (request.getAttribute("message") != null) { %>
   <tr>
 		<td colspan="2" align="center" class="msg" >
 		  <%=request.getAttribute("message") %>
 		</td>
  </tr> 
  <% } %>
    
			<table border="0" align="center"   height="100%" width="600">
				<tr>
					<td align="center" height="100%" class="loginBodyTable">
					   <fieldset class="userform">
							<legend>LOGIN TO OPENIAM IDENTITY MANAGER</legend>
							<table class="fieldsetTable" width="600">

<!--
<tr>
      <td class="tddark" align="right">
         Select PO:
      </td>
      <td class="tdlight">
            <form:select path="po" multiple="false">
                <form:option value="" label="-Please Select-"/>
                <form:option value="BGHS" label="Baycrest Geriatric Hospital System"/>
                <form:option value="CAMH" label="Centre for Addiction and Mental Health"/>
                <form:option value="CGMH" label="Collingwood General and Marine Hospital"/>
                <form:option value="CVH" label="Credit Valley Hospital"/>
                <form:option value="GBGH" label="Georgian Bay General Hospital"/>
                <form:option value="HBKR" label="Holland Bloorview Kids Rehabilitation Hospital"/>
                <form:option value="HHCC" label="Headwaters Health Care Centre"/>
                <form:option value="HHS" label="Halton Healthcare Services"/>
                <form:option value="HRRH" label="Humber River Regional Hospital"/>
                <form:option value="MAHC" label="Muskoka Algonquin Healthcare"/>
                <form:option value="MSH" label="Mount Sinai Hospital"/>
                <form:option value="OSMH" label="Orillia Soldier's Memorial Hospital"/>
                <form:option value="RVH" label="Royal Victoria Hospital"/>
                <form:option value="SICKKIDS" label="Hospital for Sick Children"/>
                <form:option value="SJHC" label="St. Joseph's Health Centre"/>
                <form:option value="THC" label="Trillium Health Centre"/>
                <form:option value="TRI" label="Toronto Rehabilitation Institute "/>
                <form:option value="UHN" label="University Health Network "/>
                <form:option value="WCH" label="Women's College Hospital"/>
                <form:option value="WOHS" label="William Osler Health System "/>
                <form:option value="WPHC" label="West Park Healthcare Centre"/>
            </form:select>
      </td>
  </tr>
  -->

			 		 						 <tr>
   										 <td><label for="username" >User Principal Name:<font color="red">*</font></label>
   								 </td>
								    <td class="userformInput"><form:input path="principal" size="40" maxlength="40"  autocomplete="off" />
								     <br><form:errors path="principal" cssClass="error" />
								    </td>
  								</tr>
									  <tr>
									    <td><label for="username" > Password:<font color="red">*</font></label>
									    </td>
									    <td class="userformInput"><form:password path="password" size="40" maxlength="40" /><br>
									    <form:errors path="password" cssClass="error" /></td>
									  </tr> 
			
								</table>
						</fieldset>
					</td>
			   </tr>
				<tr>
					   <td  width="70%" class="buttonRow" align="right"><input type="Submit" class="Submit" value="Login" /></td>
					   <td ></td>
				  </tr>
				   </table>
				   </td>
			  </tr>
		</table>
  
</table>
</form:form>



