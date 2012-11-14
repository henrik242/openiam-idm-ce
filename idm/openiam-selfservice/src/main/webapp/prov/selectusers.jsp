<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<script type="text/javascript">
<!--

function showUserDialog(idfield, namefield) {
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf ( "MSIE " );

    if ( msie > 0 ) {
        dialogReturnValue = window.showModalDialog("user/dialogshell.jsp",null,"dialogWidth:800px;dialogHeight:600px;");
        document.getElementById (idfield).value = dialogReturnValue.id;
        document.getElementById (nameField).value = dialogReturnValue.name;
    }else {
        var prevReturnValue = window.returnValue;
        window.returnValue = undefined;
        dialogReturnValue = window.showModalDialog("user/seluser.jsp",null,"dialogWidth:800px;dialogHeight:600px;");
        if(dialogReturnValue == undefined) {
            dialogReturnValue = window.returnValue;
        }
        window.returnValue = prevReturnValue;

        document.getElementById (idfield).value = dialogReturnValue.id;
        document.getElementById (namefield).value = dialogReturnValue.name;
    }
}



String.prototype.toProperCase = function() 
{
    return this.charAt(0).toUpperCase() + this.substring(1,this.length).toLowerCase();
}


function validateInt(fld) {
   if (isNaN(fld.value)) {
		alert(fld.name + " is not a number");
		return false;
	}
	return true;
}




//-->
</script>

<form:form commandName="changeAccessCmd"  cssClass="user-info">

    <fieldset>

        <div class="block">
            <div class="wrap alt">
                <h4>Create Request</h4>
                <!-- col 1 -->
                <div class="col">
                    <div class="row">
                        <label for="t-1"><a href="javascript:showUserDialog('userId', 'userName' );">Select User For Request</a></label>
                        <form:hidden path="userId" /><form:input path="userName" size="40" readonly="true" />
                        <form:errors path="userId" cssClass="error" />
                    </div>
                    <div class="row">
                        <label for="t-1">Select a Workflow:</label>
                        <form:select path="workflowResourceId" multiple="false">
                            <form:option value="-" label="-Please Select-"/>
                            <form:options items="${workflowList}" itemValue="resourceId" itemLabel="name"/>
                        </form:select>
                        <form:errors path="workflowResourceId" cssClass="error" />
                    </div>
                    <div class="row">
                        <label for="t-1">Start / Effective Date (MM/dd/yyyy):</label>
                        <form:input path="startDate" size="20"  />
                    </div>
                    <div class="row">
                        <label for="t-1">Reason:</label>
                        <form:textarea path="reason" cols="60" rows="4" />
                    </div>
                </div>
                <!-- col 2 -->
                <div class="col">
                    <div class="row">
                    </div>

                </div>

            </div>
        </div>
        <div class="button">
            <input type="submit" name="_cancel" value="Cancel" />
        </div>
        <div class="button">
            <input type="submit" name="_target2" value="Next"/>
        </div>
        <div class="button">
            <input type="submit" name="_target0" value="Previous"/>
        </div>
    </fieldset>

</form:form>
