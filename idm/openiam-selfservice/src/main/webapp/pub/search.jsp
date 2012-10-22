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

<html:form action = "/pub/directory.do?method=search" styleClass="user-info">
	<fieldset>
            <div class="block">
                <div class="wrap alt">
                    <div class="col-1">
						<div class="row alt">
                            <label for="t-1">Last Name</label>
                            <input name="lastName" type="text" size="30" maxlength="30" />
                      </div>
                      <p class="info">'%' for wildcard searches</p>
                     	<div class="row alt">
                            <label for="t-1">Department</label>
                            <html:select property="dept" >
	        					<html:options collection="orgList" property="value" labelProperty="label"/>
	     					</html:select>
                      </div>                      
								</div>
                
                <div class="col-1">
 											<div class="row alt">
                            <label for="t-2">First Name</label>
                            <input name="firstName" type="text" id="firstName" size="30" maxlength="40" />
                      </div>               
 											<div class="row alt">
                            <label for="t-2">Phone Number</label>
                            <input name="phone_areaCd" type="text" size="8" class="code" /> 
                            <input name="phone_nbr" type="text" size="18" maxlength="30" class="phone" />
                      </div>
                </div>             	
                <div class="button">
                    <input type="submit" name="Submit" value="Lookup">

                </div>
                <div class="button">
                    <input type="reset" name="Reset" value="Reset">
                </div>


                </div>
            </div>
        </fieldset>
    </html:form>

<jsp:include page="searchresult.jsp" flush="true" />


