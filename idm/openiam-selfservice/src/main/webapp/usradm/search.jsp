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
	
	List secondaryStatusList = (List)session.getAttribute("secondaryStatusList");
	pageContext.setAttribute("secondaryStatusList",secondaryStatusList);
	
	//List groupList = (List)session.getAttribute("groupList");
	List groupList = (List)request.getAttribute("groupList");
	pageContext.setAttribute("groupList",groupList);
	List roleList = (List)request.getAttribute("roleList");
	pageContext.setAttribute("roleList",roleList);
	String msg = (String)request.getAttribute("msg");
	
	List elementList = (List)request.getAttribute("elementList");
	pageContext.setAttribute("elementList",elementList);

    List orgList = (List)session.getAttribute("orgList");
    pageContext.setAttribute("orgList",orgList);
	
%>
   <html:form action = "/idman/userSearch.do?action=search" styleClass="user-info" >

        <fieldset>
            <div class="block">
                <div class="wrap alt">
                    <div class="col-1">
                       <div class="row alt">
                            <label for="t-1">Last Name</label>
                            <input name="lastName" type="text" size="30" maxlength="30" />
                        </div>
                        <p class="info">'%' for wildcard searches</p>
                        <div class="row">
                            <label>Organization</label>
                            <html:select property="companyName">
                                <html:options collection="orgList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="row">
                            <label>Group</label>
                            <html:select property="group">
                                <html:options collection="groupList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="row">
                            <label>Role</label>
                            <html:select property="role">
                                <html:options collection="roleList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="row">
                            <label>Extended Attributes</label>
                            <html:select property="attributeName">
                                <html:options collection="elementList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                    <div class="col-1">
                         <div class="row alt">
                            <label for="t-7">Email</label>
                            <input name="email" type="text" id="email" size="20" maxlength="50" />
                           </div>
                         <p class="info">'%' for wildcard searches</p>
                        <div class="row alt">
                            <label for="t-14">Phone</label>
                            <html:text property="areaCode" size="3" maxlength="3" styleClass="code" />
                            <html:text property="phoneNumber" size="10" maxlength="10" styleClass="phone" />
                        </div>
                        <p class="info">(AreaCode- Phone)</p>
                        <div class="row">
                            <label>User Status</label>
                            <html:select property="status">
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="row">
                            <label>Secondary Status</label>
                            <html:select property="secondaryStatus">
                                <html:options collection="secondaryStatusList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="row alt">
                            <html:text property="attributeValue" size="40" maxlength="40" styleClass="long"/>
                        </div>
                        <p class="info">'%' for wildcard searches</p>
                    </div>

                    <div class="button">
                        <input type="submit" value="Submit" />
                    </div>
                    <div class="button">
                        <input type="submit" value="Reset" />
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>
