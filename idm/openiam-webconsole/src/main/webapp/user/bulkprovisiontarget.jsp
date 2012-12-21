<%@ page import="org.openiam.idm.srvc.synch.dto.BulkMigrationConfig" %>
<%@ page import="org.openiam.webadmin.user.BulkProvisioningCommand" %>
<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--@elvariable id="users" type="java.util.List<org.openiam.idm.srvc.user.dto.User>"--%>
<%--@elvariable id="bulkProvisionCmd" type="org.openiam.webadmin.user.BulkProvisioningCommand"--%>
<%--@elvariable id="roleList" type="java.util.List<org.openiam.idm.srvc.role.dto.Role>"--%>
<%--@elvariable id="resourceList" type="java.util.List<org.openiam.idm.srvc.res.dto.Resource>"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>

<script type="text/javascript">
    $(function() {
        //SEnd Email radio inputs
        $('#submitBtn').attr('disabled','disabled');
        //Reset password radio inputs
        $('#newPassword').attr('disabled','disabled');
        //Modify radio inputs
        $('#operationSelector').attr('disabled','disabled');
        $('#targetRoleSelector').attr('disabled','disabled');
        $('#targetResourceSelector').attr('disabled','disabled');
        $('#sendMsgSelector').attr('disabled','disabled');
    });

    var selectRadio = function(obj) {
        var selectedRadio = $("input[name='actionType']:checked").val();
        if(selectedRadio == 'SEND_EMAIL') {
            $('#sendMsgSelector').removeAttr('disabled');
            //disable other radio inputs
            $('#newPassword').attr('disabled','disabled');
            $('#operationSelector').attr('disabled','disabled');
            $('#targetRoleSelector').attr('disabled','disabled');
            $('#targetResourceSelector').attr('disabled','disabled');
        } else {
            if(selectedRadio == 'RESET_PASSWORD') {
                //enable Reset password radio inputs
                $('#newPassword').removeAttr('disabled');
                //disable other
                $('#sendMsgSelector').attr('disabled','disabled');
                $('#operationSelector').attr('disabled','disabled');
                $('#targetRoleSelector').attr('disabled','disabled');
                $('#targetResourceSelector').attr('disabled','disabled');

            } else if(selectedRadio == 'MODIFY_ACCESS') {
                //enable Modify radio inputs
                $('#operationSelector').removeAttr('disabled');
                $('#targetRoleSelector').removeAttr('disabled');
                $('#targetResourceSelector').removeAttr('disabled');
                //disable other
                $('#sendMsgSelector').attr('disabled','disabled');
                $('#newPassword').attr('disabled','disabled');
            }

        }
        $('#submitBtn').removeAttr('disabled');
    };

    var beforeSubmit = function() {
        if($("input[name='actionType']:checked").val() == 'SEND_EMAIL') {
        //TODO check selected Notification is not Empty
        }
        return true;
    };
</script>

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

                                    <td><label for="recordsLbl" class="attribute"> Selection Criteria will impact:</label></td>
                                    <td class="error" id="recordsLbl">
                                        ${bulkProvisionCmd.resultSetSize} <c:if test="${bulkProvisionCmd.resultSetSize == 100}"><span>+</span></c:if>  record(s)
                                    </td>
                                </tr>

                                <tr>

                                    <td><label for="rstPasswdRadio" class="attribute">
                                        <form:radiobutton onchange="selectRadio(this);" path="actionType" id="rstPasswdRadio" value="<%=BulkMigrationConfig.ACTION_RESET_PASSWORD %>" /> Reset Password</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:password path="newPassword" size="30" maxlength="30"/>
                                        <form:errors path="actionType" cssClass="error" /><br>
                                        <form:errors path="newPassword" cssClass="error" />

                                    </td>
                                </tr>

                                <tr>

                                    <td><label for="modifyAccessRadio" class="attribute"><form:radiobutton onchange="selectRadio(this);" path="actionType" id="modifyAccessRadio" value="<%=BulkMigrationConfig.ACTION_MODIFY_ACCESS %>" /> Modify User Access</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">

                                    </td>
                                </tr>

                                <tr>

                                    <td><label for="operationSelector" class="attribute">Operation</label></td>
                                    <td  class="userformInput labelValue">
                                        <form:select path="operation" id="operationSelector" multiple="false">
                                            <form:option value="ADD" label="ADD"/>
                                            <form:option value="REMOVE" label="REMOVE"/>
                                        </form:select>

                                    </td>
                                </tr>

                                <tr>
                                    <td><label for="targetRoleSelector" class="attribute">Target Role</label></td>
                                    <td  class="userformInput labelValue">
                                        <form:select path="targetRole" id="targetRoleSelector" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <c:forEach items="${roleList}" var="role">
                                                <form:option value="${role.id.serviceId}*${role.id.roleId}" label="${role.id.serviceId}-${role.roleName}" />
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="targetResource" cssClass="error" />
                                    </td>


                                </tr>


                                <tr>
                                    <td><label for="targetResourceSelector" class="attribute">Target System</label></td>
                                    <td  class="userformInput" for="username" class="labelValue">
                                        <form:select path="targetResource" id="targetResourceSelector" multiple="false">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${resourceList}" itemValue="resourceId" itemLabel="name"/>
                                        </form:select>
                                    </td>


                                </tr>
                                <tr>

                                    <td>
                                        <label for="sendEmailRadio" class="attribute"/>
                                        <form:radiobutton onchange="selectRadio(this);" id="sendEmailRadio" path="actionType" value="<%=BulkMigrationConfig.ACTION_SEND_EMAIL %>" /> &nbsp;Send Email
                                    </td>
                                    <td class="userformInput labelValue">
                                        <form:select onchange="selectSendMessageType(this)" path="selectedNotificationName" id="sendMsgSelector">
                                            <form:option value="" label="-Please Select-"/>
                                            <form:options items="${bulkProvisionCmd.notifications}" itemValue="name" itemLabel="name"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4" align ="right"  >
                                        <input type="submit" name="_target0" value="Previous"/>
                                        <input id="submitBtn" onclick="return beforeSubmit();" type="submit" name="_finish" value="Submit"/>
                                        <input type="submit" name="_cancel" value="Cancel" />
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                        </td>

                    </tr>
                </table>
                <table width="1100pt"  class="bodyTable" height="200px" >
                    <tr>
                        <td>
                            <fieldset class="userformSearch" >
                                <legend>USERS </legend>
                                <table class="fieldsetTable"  width="100%" >
                                    <tr>
                                        <th>Id</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <!--th>Actions</th-->
                                    </tr>
                                </table>
                                <div style="width: 100%;max-height:200px;overflow: auto;">
                                    <table class="fieldsetTable"  width="100%" >
                                        <c:forEach var="usr" items="${bulkProvisionCmd.users}">
                                            <tr id="${usr.userId}">
                                                <input type="hidden" name="selectedUserIds" value="${usr.userId}" />
                                                <td>${usr.userId}</td>
                                                <td>${usr.firstName} ${usr.lastName}</td>
                                                <td>${usr.email}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
