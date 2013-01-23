<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<table  width="1100pt">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading">Select User Selection Criteria</h2>
                    </td>
                    <td class="tabnav" >
                    </td>
                    <td></td>
                </tr>
            </table>
        </td>
    </tr>


    <tr>
        <td>

            <form:form commandName="bulkProvisionCmd">


                <table width="1100pt"  class="bodyTable" height="100%"  >


                    <tr>
                        <td>    <fieldset class="userformSearch" >
                            <legend>ENTER SELECTION CRITERIA</legend>
                            <table class="fieldsetTable"  width="100%" >

                                <tr>
                                    <td><label for="username" class="attribute">Last Name</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:input path="lastName" size="35" maxlength="40"  />
                                        <form:errors path="lastName" cssClass="error" />
                                    </td>

                                    <td><label for="username" class="attribute">Organization</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="companyId" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">Division</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="division" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${divList}" itemValue="orgId" itemLabel="organizationName"/>
                                        </form:select>
                                    </td>

                                    <td><label for="username" class="attribute">Department</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="deptId" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${deptList}" itemValue="orgId" itemLabel="organizationName"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">Role</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="role" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <c:forEach items="${roleList}" var="role">
                                                <form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.id.serviceId}-${role.roleName}" />
                                            </c:forEach>
                                        </form:select>
                                    </td>

                                    <td></td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">User Status</label></td>
                                    <td class="userformInput" for="username" class="labelValue" colspan="3">
                                        <form:select path="userStatus" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:option value="ACTIVE" label="ACTIVE"/>
                                            <form:option value="APPROVAL_DECLINED" label="APPROVAL_DECLINED"/>
                                            <form:option value="DELETED" label="DELETED"/>
                                            <form:option value="INACTIVE" label="INACTIVE"/>
                                            <form:option value="LEAVE" label="LEAVE"/>
                                            <form:option value="PENDING_START_DATE" label="PENDING_START_DATE"/>
                                            <form:option value="PENDING_APPROVAL" label="PENDING_APPROVAL"/>
                                            <form:option value="PENDING_USER_VALIDATION" label="PENDING_USER_VALIDATION"/>
                                            <form:option value="PENDING_INITIAL_LOGIN" label="PENDING_INITIAL_LOGIN"/>
                                            <form:option value="RETIRED" label="RETIRED"/>
                                            <form:option value="TERMINATE" label="TERMINATE"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">Extended Attributes</label></td>
                                    <td class="userformInput" for="username" class="labelValue" colspan="3">
                                        <form:select path="attributeName" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <c:forEach items="${elementList}" var="element">
                                                <form:option value="${element.metadataElementId} " label="${element.metadataTypeId}->${element.attributeName}" />
                                            </c:forEach>
                                        </form:select>

                                        <form:input path="attributeValue" size="35" maxlength="40"  />


                                    </td>
                                </tr>
                                <tr>
                                    <td><label for="username" class="attribute">Last Login</label></td>
                                    <td  class="userformInput" for="username" class="labelValue" colspan="3">
                                    <form:select path="dateOperation" multiple="false">
                                            <form:option value="=" label="=" />
                                            <form:option value="<>" label="<>"/>
                                            <form:option value="<" label="<" />
                                            <form:option value="<=" label="<="/>
                                            <form:option value=">" label=">"/>
                                            <form:option value=">=" label=">="/>
                                            <form:option value="IS NULL" label="= NULL"/>
                                        </form:select>
                                        <form:input path="lastLoginDate" size="35" maxlength="40"  />(MM/dd/yyyy)
                                        <form:errors path="lastLoginDate" cssClass="error" />
                                    </td>

                                </tr>

                                <tr>
                                    <td colspan="4" align ="right"  >
                                        <input type="submit" name="_target1" value="Next"/>
                                        <input type="submit" name="_cancel" value="Cancel" />
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                        </td>

                    </tr>
                </table>

            </form:form>
        </td>
    </tr>
</table>
