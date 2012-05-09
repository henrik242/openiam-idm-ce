<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<div class="block">
    <div class="wrap">

        <form:form commandName="pswdChangeCmd">
            <form:hidden path="perId" />
            <form:hidden path="domainId"    />

            <fieldset>
                <label for="t-1">Reset Password for ${pswdChangeCmd.firstName} ${pswdChangeCmd.lastName}</label>
                <label for="t-1">Login ID</label>
                <form:input path="principal" size="30"  maxlength="30" readonly="true"   />
                <label for="t-1"> New Password<span>*</span></label>
                <form:password path="password" size="30"  maxlength="30"   />
                <p><form:errors path="password" cssClass="error" /></p>
                <label for="t-1">Confirm New Password<span>*</span></label>
                <form:password path="confPassword" size="30"  maxlength="30"   />
                <p><form:errors path="confPassword" cssClass="error" /></p>

                <div class="button">
                    <input type="reset" value="Cancel" />
                </div>
                <div class="button">
                    <input type="submit" value="Save" />
                </div>
            </fieldset>

        </form:form>
    </div>
</div>


