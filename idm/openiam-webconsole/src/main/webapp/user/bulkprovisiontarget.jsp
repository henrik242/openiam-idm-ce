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
                        <h2 class="contentheading">Select Target System</h2>
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

                                    <td><label for="username" class="attribute">Operation</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="operation" multiple="false">
                                            <form:option value="ADD" label="ADD"/>
                                            <form:option value="REMOVE" label="REMOVE"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="username" class="attribute">Target System</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="division" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${divList}" itemValue="orgId" itemLabel="organizationName"/>
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
