<%@ page import="org.openiam.idm.srvc.synch.dto.BulkMigrationConfig" %>
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
                        <h2 class="contentheading">Select Operation</h2>
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
                            <legend>ENTER SELECTION </legend>
                            <table class="fieldsetTable"  width="100%" >

                                <tr>

                                    <td><label for="username" class="attribute"> <form:radiobutton path="actionType" value="<%=BulkMigrationConfig.ACTION_RESET_PASSWORD %>" /> Reset Password</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:errors path="actionType" cssClass="error" />
                                        <form:password path="newPassword" />
                                        <form:errors path="newPassword" cssClass="error" />

                                    </td>
                                </tr>

                                <tr>

                                    <td><label for="username" class="attribute"><form:radiobutton path="actionType" value="<%=BulkMigrationConfig.ACTION_MODIFY_ACCESS %>" /> Modify User Access</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">

                                    </td>
                                </tr>

                                <tr>

                                    <td><label for="username" class="attribute">Operation</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="operation" multiple="false">
                                            <form:option value="ADD" label="ADD"/>
                                            <form:option value="REMOVE" label="REMOVE"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">Target Role</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="targetRole" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <c:forEach items="${roleList}" var="role">
                                                <form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.id.serviceId}-${role.roleName}" />
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="targetResource" cssClass="error" />
                                    </td>


                                </tr>


                                <tr>
                                    <td><label for="username" class="attribute">Target System</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="targetResource" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${resourceList}" itemValue="resourceId" itemLabel="name"/>
                                        </form:select>
                                    </td>


                                </tr>


                                <tr>
                                    <td colspan="4" align ="right"  >
                                        <input type="submit" name="_target0" value="Previous"/>
                                        <input type="submit" name="_finish" value="Submit"/>
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
