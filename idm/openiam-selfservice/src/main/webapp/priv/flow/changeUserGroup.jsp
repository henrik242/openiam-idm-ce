<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


    <form:form commandName="changeUserGroup" cssClass="profile">
        <fieldset>
            <div class="block">
                <div class="wrap alt">
                    <p>Request Group Membership</p>
                    <!-- column 1  -->
                    <div class="col">
                        <div class="row">
                            <label for="t-1">Operation:<span>*</span></label>
                                <form:select path="operation" multiple="false">
                                    <form:option value="ADD" label="ADD"/>
                                    <form:option value="REMOVE" label="REMOVE"/>
                                </form:select>
                        </div>
                        <div class="row">
                            <label for="t-3">Group Membership: </label>
                            <form:select path="groupId" multiple="false">
                                <form:option value="" label="-Please Select-"/>
                                <form:options items="${groupList}" itemValue="grpId" itemLabel="grpName"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="col">
                        <!-- show the users current group membership -->
                        <p> ${changeUserGroup.selectedUser.firstName} ${changeUserGroup.selectedUser.lastName} is currently a member of the following groups:</p>
                        <c:forEach items="${changeUserGroup.currentGroupMemberships}" var="grp" varStatus="grpStatus">
                        <div class="row">
                            <label for="t-3">${grp.grpName}</label>
                        </div>

                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="button">
                <input type="submit" value="Save" name="btnSave">
            </div>

            <div class="button">
                <input type="submit" name="_cancel" value="Cancel" />
            </div>

        </fieldset>
    </form:form>
