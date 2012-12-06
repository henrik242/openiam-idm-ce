<%@ page import="org.openiam.idm.srvc.msg.dto.MessageBodyType" %>
<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--@elvariable id="mailTmplCmd" type="org.openiam.webadmin.admin.mailtmpl.MailTemplateCommand"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery-ui-1.7.2.custom.min.js'/>"></script>
<link rel="Stylesheet" type="text/css" href="<c:url value='/css/jqueryui/ui-lightness/jquery-ui-1.7.2.custom.css'/>" />

<script type="text/javascript" src="<c:url value='/scripts/jHtmlArea-0.7.5.js'/>"></script>
<link rel="Stylesheet" type="text/css" href="<c:url value='/css/jHtmlArea.css'/>" />

<script type="text/javascript">
    $(function() {
        //HTML Text Area initialization
        $("#txtDefaultHtmlArea").htmlarea();

        textAreaInit();

        $('#attachFileInpId').change(function(e) {
            $('#fakeAttachmentFileInput').val($(this).val());
        });
    });

    var textAreaInit =function() {
        var sendMsgType = $('#msgBodyType');
        if($(sendMsgType).val() == '<%=MessageBodyType.HTML %>') {
            $('div.jDefaultTextArea').css('display','none');
        } else if($(sendMsgType).val() == '<%=MessageBodyType.PLAIN %>') {
            $('div.jHtmlArea').css('display','none');
        }
    };

    var selectAttachment = function() {
        $('#attachFileInpId').click();
        return false;
    };

    var selectSendMessageType = function(obj) {
        if($(obj).val() == '<%=MessageBodyType.HTML %>') {
            $('div.jDefaultTextArea').css('display','none');
            $('div.jHtmlArea').css('display','block');
        } else if($(obj).val() == 'PLAIN') {
            $('div.jHtmlArea').css('display','none');
            $('div.jDefaultTextArea').css('display','block');
        }
    };

    var beforeSubmit = function(obj) {
        $('input#submitTypeInp').val(obj);
        var sendMsgBodyInpt = $('#sendMsgBodyId');
        var sendMsgType = $('#msgBodyType');
        if($(sendMsgType).val() == '<%=MessageBodyType.HTML %>') {
            $(sendMsgBodyInpt).val($('#txtDefaultHtmlArea').htmlarea('html'));
        } else if($(sendMsgType).val() == '<%=MessageBodyType.PLAIN %>') {
            $(sendMsgBodyInpt).val($('#txtDefaultPlainArea').val());
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
                        <h2 class="contentheading">Mail Template</h2>
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

            <form:form method="POST" id="mailTmplCmdForm" action="mailTmplDetail.cnt" commandName="mailTmplCmd" enctype="multipart/form-data">
               <c:if test="${mailTmplCmd.template.tmplId != null}"> <form:hidden path="template.tmplId"   /></c:if>
                <table width="1100pt"  class="bodyTable" height="100%"  >

                    <tr>
                        <td>
                            <fieldset class="userformSearch" >
                                <legend>ENTER SELECTION </legend>
                                <table class="fieldsetTable"  width="100%" >
                                    <tr>
                                        <td>
                                            <label for="nameInp" class="attribute">Template Name *</label>
                                        </td>
                                        <td  class="userformInput labelValue">
                                            <input type="text" id="nameInp"
                                                   name="template.name"
                                                   value="${mailTmplCmd.template.name}"/>
                                            <form:errors path="template.name" cssClass="error" /><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label for="sbjInp" class="attribute">Subject</label>
                                        </td>
                                        <td class="userformInput labelValue">
                                            <input type="text" id="sbjInp"
                                                   name="template.subject"
                                                   value="${mailTmplCmd.template.subject}"/>
                                            <form:errors path="template.subject" cssClass="error" /><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label for="attachFileInpId" class="attribute">
                                                Attachment file:
                                            </label>
                                        </td>
                                        <td class="userformInput labelValue">
                                            <input type="text" id="fakeAttachmentFileInput" readonly="true"
                                                   value="${mailTmplCmd.template.attachmentFilePath}"/>
                                            <input type="button" value="Select" style="font-size:0.8em" onclick="selectAttachment();"/>
                                            <input id="attachFileInpId" type="file" style="display:none"
                                                   name="attachmentFile"
                                                   value="${mailTmplCmd.template.attachmentFilePath}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label for="msgBodyType" class="attribute">Message Body Type</label>
                                        </td>
                                        <td class="userformInput labelValue">

                                            <form:select onchange="selectSendMessageType(this)" path="template.type" id="msgBodyType" multiple="false">
                                                <form:option value="<%=MessageBodyType.PLAIN %>" label="PLAIN"/>
                                                <form:option value="<%=MessageBodyType.HTML %>" label="HTML"/>
                                            </form:select>

                                            <form:errors path="template.type" cssClass="error" /><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label for="sendMsgBodyId" class="attribute">Message Body</label>
                                        </td>
                                        <td>
                                            <input id="sendMsgBodyId" type="hidden" name="template.body" value="" />
                                            <div class="jDefaultTextArea">
                                                <textarea id="txtDefaultPlainArea" cols="85" rows="10">${mailTmplCmd.template.body}</textarea>
                                            </div>
                                            <textarea id="txtDefaultHtmlArea" cols="85" rows="10">${mailTmplCmd.template.body}</textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="4" align ="right"  >
                                            <input id="submitTypeInp" type="hidden" name="btn" value="" />
                                            <input id="submitBtn" onclick="return beforeSubmit('submit');" type="submit" value="Submit"/>
                                            <input id="deleteBtn" onclick="return beforeSubmit('delete');" type="submit" value="Delete"/>
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
