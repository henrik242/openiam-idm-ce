<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form:form commandName="exampleCmd" cssClass="profile">

    <fieldset>
        <div class="block">
            <div class="wrap alt">

                <!-- column 1  -->

                <div class="col">
                    <div class="row">
                        <label for="t-1">Field 1<span>*</span></label>
                        <form:input path="field1" size="40" maxlength="40" readonly="${exampleCmd.readOnly}" />
                    </div>
                    <div class="row">
                        <label for="t-1">Field 2<span>*</span></label>
                        <form:input path="field2" size="40" maxlength="40" readonly="${exampleCmd.readOnly}" />
                    </div>

                    <div class="row">
                        <label for="t-1">Field 3<span>*</span></label>
                        <form:input path="field3" size="40" maxlength="40" readonly="${exampleCmd.readOnly}" />
                    </div>


                </div>

                <div class="col">

                </div>
            </div>
        </div>
        <div class="button">
            <input type="submit" value="Submit" />
        </div>

        <div class="button alt">
            <input type="submit" name="_cancel" value="Cancel" />
        </div>

    </fieldset>

</form:form>


