<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<form:form commandName="changeUserResource" cssClass="profile">
    <fieldset>
        <div class="block">
            <div class="wrap alt">
                <p>Request Role Membership</p>
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
                        <label for="t-2">Resource / Application:</label>
                            <form:select path="resourceId" multiple="false">
                                <form:option value="" label="-Please Select-"/>
                                <form:options items="${resourceList}" itemValue="resourceId" itemLabel="name"/>
                            </form:select>
                            <p><form:errors path="resourceId"/></p>
                    </div>
                </div>
                <div class="col">

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
