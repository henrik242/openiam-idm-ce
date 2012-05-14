<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript">
    <!--

    function defaultFields() {
        var theForm = document.getElementById('newHireCmd');
        theForm.userPrincipalName.value = theForm.user.firstName.value + '.' + theForm.user.lastName.value;
    }

    String.prototype.toProperCase = function () {
        return this.charAt(0).toUpperCase() + this.substring(1, this.length).toLowerCase();
    }


    function validateInt(fld) {
        if (isNaN(fld.value)) {
            alert(fld.name + " is not a number");
            return false;
        }
        return true;
    }

    function showUserDialog(idfield, namefield) {
        var ua = window.navigator.userAgent;
        var msie = ua.indexOf("MSIE ");

        if (msie > 0) {
            dialogReturnValue = window.showModalDialog("user/dialogshell.jsp", null, "dialogWidth:670px;dialogHeight:600px;");
            document.getElementById(idfield).value = dialogReturnValue.id;
            document.getElementById(nameField).value = dialogReturnValue.name;
        } else {
            var prevReturnValue = window.returnValue;
            window.returnValue = undefined;
            dialogReturnValue = window.showModalDialog("user/seluser.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
            if(dialogReturnValue == undefined) {
                dialogReturnValue = window.returnValue;
            }
            window.returnValue = prevReturnValue;

            document.getElementById(idfield).value = dialogReturnValue.id;
            document.getElementById(namefield).value = dialogReturnValue.name;
        }
    }


    function selectChange(ctrl) {
        var src = document.getElementById(ctrl);
        var theForm = document.getElementById('profileCmd');


        selIdx = src.options.selectedIndex;
        newSel = src.options[selIdx].text;

        if (newSel == '-Please Select-') {
            theForm.bldgNbr.value = "";
            theForm.address1.value = "";
            theForm.city.value = "";
            theForm.state.value = "";
            theForm.postalCode.value = "";
        } else {

            var spltStr = newSel.split('-');

            theForm.bldgNbr.value = spltStr[1];
            theForm.address1.value = spltStr[2];
            theForm.city.value = spltStr[3];
            theForm.state.value = spltStr[4];
            theForm.postalCode.value = spltStr[5];
        }
    }

    //-->
</script>

<form:form commandName="newUserCmd" cssClass="profile">
<fieldset>
    <div class="block">
        <div class="wrap alt">
            <div class="set">
                <div class="set-wrap">
                    <label for="t-0">First Name<span>*</span></label>
                    <form:input path="user.firstName" size="40" maxlength="40"
                                onchange="defaultFields(); firstName.value = firstName.value.toProperCase();"/>
                    <p class="error"><form:errors path="user.firstName"/></p>
                </div>
                <div class="set-wrap">
                    <label for="t-1">Middle</label>
                    <form:input path="user.middleInit" size="10" maxlength="10"
                                onchange="middleName.value = middleName.value.toProperCase();"/>
                </div>
                <div class="set-wrap">
                    <label for="t-7">Last Name<span>*</span></label>
                    <form:input path="user.lastName" size="40" maxlength="40"
                                onchange="defaultFields(); lastName.value = lastName.value.toProperCase();"/>
                    <p class="error"><form:errors path="user.lastName" cssClass="error"/></p>
                </div>
            </div>

            <!-- col 1 -->
            <div class="col">
                <div class="row">
                    <label for="t-10">Nickname (AKA)</label>
                    <form:input path="user.nickname" size="20" maxlength="20"
                                onchange="nickname.value = nickname.value.toProperCase();"/>
                </div>
                <div class="row">
                    <label for="t-11">Maiden Name</label>
                    <form:input path="user.maidenName" size="20" maxlength="20"
                                onchange="maiden.value = maiden.value.toProperCase();"/>
                </div>
                <div class="row">
                    <label for="t-11">Organization</label>
                    <form:select path="user.companyId" multiple="false">
                        <form:option value="-" label="-Please Select-"/>
                        <form:options items="${orgList}" itemValue="orgId" itemLabel="organizationName"/>
                    </form:select>
                    <p class="error"><form:errors path="user.companyId"/></p>
                </div>
                <div class="row">
                    <label for="t-11">Division</label>
                    <form:select path="user.division" multiple="false">
                        <form:option value="" label="-Please Select-"/>
                        <form:options items="${divList}" itemValue="orgId" itemLabel="organizationName"/>
                    </form:select>
                </div>
                <div class="row">
                    <label for="t-11">Functional Title</label>
                    <form:input path="user.title" size="40"/>
                </div>

                <div class="row">
                    <label for="t-11">Supervisor</label>
                    <form:hidden path="supervisorId"/>
                    <form:input path="supervisorName" size="50" readonly="true"/>
                    <p class="error"><form:errors path="supervisorId"/></p>
                </div>
                <div class="row">
                    <label for="t-11">Role</label>
                    <form:select path="role" multiple="false">
                        <form:option value="-" label="-Please Select-"/>
                        <form:options items="${roleList}" itemValue="id.roleId" itemLabel="roleName"/>
                    </form:select>
                    <p class="error"><form:errors path="role"/></p>
                </div>
                <div class="row">
                    <label for="t-11">Start Date</label>
                    <form:input path="user.startDate" size="20"/>
                    <p class="error"><form:errors path="user.startDate"/></p>
                </div>
            </div>

            <!-- col 2 -->
            <div class="col">
                <div class="row">
                    <label for="t-11">Suffix</label>
                    <form:input path="user.suffix" size="20" maxlength="20"
                                onchange="suffix.value = suffix.value.toProperCase();"/>
                </div>
                <div class="row">
                    <label for="t-11">Gender</label>
                    <form:select path="user.sex">
                        <form:option value="-" label="-Please Select-"/>
                        <form:option value="M" label="Male"/>
                        <form:option value="F" label="Female"/>
                        <form:option value="D" label="Declined to State"/>
                    </form:select>
                </div>
                <div class="row">
                    <label for="t-11">Department</label>
                    <form:select path="user.deptCd" multiple="false">
                        <form:option value="-" label="-Please Select-"/>
                        <form:options items="${deptList}" itemValue="orgId" itemLabel="organizationName"/>
                    </form:select>
                </div>
                <div class="row">
                    <label for="t-11">Job Code</label>
                    <form:select path="user.jobCode">
                        <form:option value="" label="-Please Select-"/>
                        <c:forEach items="${jobCodeList}" var="jobCode">
                            <form:option value="${jobCode.id.codeGroup}" label="${jobCode.description}"/>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row">
                    <label for="t-11">Employment Type<span>*</span></label>
                    <form:select path="user.employeeType">
                        <form:option value="-" label="-Please Select-"/>
                        <c:forEach items="${userTypeList}" var="userType">
                            <form:option value="${userType.id.codeGroup}" label="${userType.description}"/>
                        </c:forEach>
                    </form:select>
                    <p class="error"><form:errors path="user.employeeType"/></p>
                </div>
                <div class="row">
                    <label for="t-11"><a href="javascript:showUserDialog('supervisorId', 'supervisorName' );">Select
                        Supervisor</a></label>
                    &nbsp;
                </div>
                <div class="row">
                    <label for="t-11">Group</label>
                    <form:select path="group" multiple="false">
                        <form:option value="" label="-Please Select-"/>
                        <form:options items="${groupList}" itemValue="grpId" itemLabel="grpName"/>
                    </form:select>
                </div>
                <div class="row">
                    <label for="t-11">Last Date</label>
                    <form:input path="user.lastDate" size="20"/>
                    <p class="error"><form:errors path="user.lastDate"/></p>
                </div>

            </div>
        </div>
    </div>


</fieldset>


<h4>Contact Information</h4>
<fieldset>
    <div class="block">
        <div class="wrap alt">
            <!-- Column 1 -->
            <div class="col">
                <div class="row">
                    <label>Corporate Email-1</label>
                    <form:input path="email1" size="40" maxlength="40"/>
                </div>

                <div class="row">
                    <label>Building Name</label>
                    <form:select path="user.locationCd" multiple="false" onchange="selectChange('locationBldg');">
                        <form:option value="-" label="-Please Select-"/>
                        <c:forEach items="${locationAry}" var="location">
                            <form:option value="${location.locationId}"
                                         label="${location.name}-${location.bldgNum}-${location.address1}-${location.city}-${location.state}-${location.postalCd} "/>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row">
                    <label>Bulding Number</label>
                    <form:input path="user.bldgNum" size="5"/>
                </div>
                <div class="row">
                    <label>Address 1</label>
                    <form:input path="user.address1" size="20"/>
                </div>
                <div class="row">
                    <label>Address 2</label>
                    <form:input path="user.address2" size="30"/>
                </div>
                <div class="row">
                    <label>City</label>
                    <form:input path="user.city" size="30"/>
                </div>
                <div class="row">
                    <label>State / Province</label>
                    <form:input path="user.state" size="30"/>
                </div>
                <div class="row">
                    <label>Zip / Postal Code</label>
                    <form:input path="user.postalCd" size="30" maxlength="10"/>
                </div>

            </div>
            <!-- Column 2 -->
            <div class="col">
                <div class="row">
                    <label>Personal Email</label>
                    <form:input path="email2" size="40" maxlength="40"/>
                </div>

                <div class="row">
                    <label for="t-13">Desk Phone</label>
                    <form:input path="workAreaCode" size="3" maxlength="3" onblur="return validateInt(workAreaCode)"
                                cssClass="code"/>
                    <form:input path="workPhone" size="10" maxlength="10" onblur="return  validateInt(workPhone)"
                                cssClass="phone"/>
                </div>
                <div class="row">
                    <label for="t-13">Mobile Phone</label>
                    <form:input path="cellAreaCode" size="3" maxlength="3" onchange="return  validateInt(cellAreaCode);"
                                cssClass="code"/>
                    <form:input path="cellPhone" size="10" maxlength="10" onchange="return  validateInt(cellPhone);"
                                cssClass="phone"/>
                </div>
                <div class="row">
                    <label for="t-13">Fax Phone</label>
                    <form:input path="faxAreaCode" size="3" maxlength="3" onchange="return  validateInt(faxAreaCode);"
                                cssClass="code"/>
                    <form:input path="faxPhone" size="10" maxlength="10" onchange="return  validateInt(faxPhone);"
                                cssClass="phone"/>
                </div>
                <div class="row">
                    <label for="t-13">Home Phone</label>
                    <form:input path="homePhoneAreaCode" size="3" maxlength="3"
                                onchange="return  validateInt(homePhoneAreaCode);" cssClass="code"/>
                    <form:input path="homePhoneNbr" size="10" maxlength="10"
                                onchange="return  validateInt(homePhoneNbr);" cssClass="phone"/>
                </div>

            </div>
        </div>
    </div>
    <div class="button">
        <input type="submit" name="_cancel" value="Cancel"/>
    </div>
    <div class="button">
        <input type="submit" name="_finish" value="Submit"/>
    </div>
</fieldset>

</form:form>

