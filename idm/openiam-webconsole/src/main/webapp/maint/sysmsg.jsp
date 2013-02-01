<%@ page import="org.openiam.idm.srvc.msg.dto.MessageBodyType" %>
<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--@elvariable id="sysMsgCmd" type="org.openiam.webadmin.admin.sysmsg.SysMsgCommand"--%>
<%--@elvariable id="mailTemplates" type="java.util.List<org.openiam.idm.srvc.msg.dto.MailTemplateDto>"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>


<script type="text/javascript">
    $(function() {

        $('#sourceFileInpId').change(function(e) {
            $('#fakeProviderFileInput').val($(this).val());
        });
        if(${sysMsgCmd.templateMethod != 0}) {
            $('#uplBtn').attr('disabled','disabled');
        }
        if(${sysMsgCmd.templateMethod != 1}) {
            $('#mailTemplateId').attr('disabled','disabled');
        }
        if(${sysMsgCmd.templateMethod != 0 && sysMsgCmd.templateMethod != 1}){
            $('#submitBtn').attr('disabled','disabled');
        }

    });

    var selectSourceFile = function() {
        $('#sourceFileInpId').click();
        return false;
    };

    var beforeSubmit = function(obj) {
        var selectedRadio = $("input[name='templateMethod']:checked").val();
        var validationResult = true;
      /*  if(selectedRadio == '1') {
            if($('#mailTemplateId').val() == '') {
               validationResult = false;
               alert('Please select mail template.');
               return validationResult;
            }
        }
        if(selectedRadio == '0') {
            if($('#sourceFileInpId').val() == '') {
               validationResult = false;
               alert('Please enter script file.');
               return validationResult;
            }
        }
        if($('#nameInp').val() == '') {
            validationResult = false;
            alert('Please fill name field.');
            return validationResult;
        }*/
        if(validationResult) {
            $('input#submitTypeInp').val(obj);
        }
        return validationResult;
    };

    var selectTemplateMethodRadio = function(obj) {
        if($(obj).val() == '0') {
            $('#mailTemplateId').attr('disabled','disabled');
            $('#uplBtn').removeAttr('disabled');
            if($('#submitBtn').attr('disabled') !== undefined) {
                $('#submitBtn').removeAttr('disabled');
            }
        } if($(obj).val() == '1') {
            $('#uplBtn').attr('disabled','disabled');
            $('#mailTemplateId').removeAttr('disabled');
            if($('#submitBtn').attr('disabled') !== undefined) {
                $('#submitBtn').removeAttr('disabled');
            }
        }
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

                <form:form method="POST" id="sysMsgCmdForm" action="sysMessageDetail.cnt" commandName="sysMsgCmd" enctype="multipart/form-data">

                    <form:hidden path="msg.msgId" />

                    <table width="1100pt"  class="bodyTable" height="100%"  >

                        <tr>
                            <td>
                                <fieldset class="userformSearch" >
                                <legend>SETTINGS</legend>
                                <table class="fieldsetTable"  width="100%" >
                                    <tr>
                                        <td>
                                            <label for="nameInp" class="attribute">Msg Name *</label>
                                        </td>
                                        <td  class="userformInput labelValue">
                                            <input type="text" id="nameInp"
                                                   name="msg.name"
                                                   <c:if test="${sysMsgCmd.msg.msgId != null && sysMsgCmd.msg.type == 'SYSTEM'}">readonly="readonly"</c:if>
                                                   value="${sysMsgCmd.msg.name}"/>
                                            <form:errors path="msg.name" cssClass="error" /><br>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td><label for="scriptRadio" class="attribute"><form:radiobutton onchange="selectTemplateMethodRadio(this);" path="templateMethod" id="scriptRadio" value="0" /> Message Provider </label></td>
                                        <td class="userformInput labelValue">

                                            <input type="text" id="fakeProviderFileInput" readonly="true"
                                                   value="${sysMsgCmd.msg.providerScriptName}"/>
                                            <input id="uplBtn" type="button" value="Select" style="font-size:0.8em" onclick="selectSourceFile();"/>
                                            <input id="sourceFileInpId" type="file" style="display:none"
                                                   name="providerScriptFile" value="${sysMsgCmd.msg.providerScriptName}">

                                            <form:errors path="msg.providerScriptName" cssClass="error" /><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="templateRadio" class="attribute"><form:radiobutton onchange="selectTemplateMethodRadio(this);" path="templateMethod" id="templateRadio" value="1" /> Mail Template </label></td>
                                        <td class="userformInput labelValue">
                                            <form:select path="selectedTemplateId" id="mailTemplateId">
                                                <form:option value="" label="-Please Select-"/>
                                                <form:options items="${mailTemplates}" itemValue="tmplId" itemLabel="name"/>
                                            </form:select>
                                            <form:errors path="selectedTemplateId" cssClass="error" /><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="tddark">Start Date</td>
                                        <td class="tdlightnormal" ><form:input path="msg.startDate" size="20" /></td>
                                    </tr>
                                    <tr>
                                        <td class="tddark">End Date</td>
                                        <td class="tdlightnormal" ><form:input path="msg.endDate" size="20" /></td>
                                    </tr>
                                    <tr>
                                        <td colspan="4" align ="right"  >
                                            <input id="submitTypeInp" type="hidden" name="btn" value="" />
                                            <input id="submitBtn" onclick="return beforeSubmit('save');" type="submit" value="Submit"/>
                                            <c:if test="${sysMsgCmd.msg.msgId != null && sysMsgCmd.msg.type != 'SYSTEM'}"><input id="deleteBtn" onclick="return beforeSubmit('delete');" type="submit" value="Delete"/></c:if>
                                            <input id="cancelBtn" type="submit" name="_cancel" value="Cancel" />
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
