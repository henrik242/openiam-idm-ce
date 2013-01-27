<ui:composition template="/composition">
    <ui:define composition="${it}" name="menu">
        <g:render template="/priv_menu_template" model="[selectedMenuItem: 'NEWHIRE']"/>
    </ui:define>
    <ui:define composition="${it}" name="body">
    <script type="text/javascript">
        var submitPressed = function(src) {
           var action = jQuery(src).attr('name');
           jQuery('#submitFld').val(action);
           return true;
        }

        var showUserDialog = function(idfield, namefield) {
            var ua = window.navigator.userAgent;
            var msie = ua.indexOf ( "MSIE " );

            if ( msie > 0 ) {
                dialogReturnValue = window.showModalDialog("dialogshell.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
                document.getElementById (idfield).value = dialogReturnValue.id;
                document.getElementById (namefield).value = dialogReturnValue.name;
            }else {
                var prevReturnValue = window.returnValue;
                window.returnValue = undefined;
                dialogReturnValue = window.showModalDialog("seluser.jsp",null,"dialogWidth:670px;dialogHeight:600px;");
                if(dialogReturnValue == undefined) {
                    dialogReturnValue = window.returnValue;
                }
                window.returnValue = prevReturnValue;

                document.getElementById (idfield).value = dialogReturnValue.id;
                document.getElementById (namefield).value = dialogReturnValue.name;
            }
        }
    </script>

                <g:form method="post" class="user-info" url="[action: 'save.jsp', controller: 'priv/newhire']">
                <g:hiddenField name="metadataTypeId" value="${newHireCommand.metadataTypeId}"/>
                <g:hiddenField id="submitFld" name="submitAction" value="_cancel" />

                    <fieldset>
                        <div class="block">
                            <div class="wrap alt">
                                <div class="col">
                                    <div class="row">
                                        <label for="t-0">First Name<span>*</span></label>
                                        <g:textField id="t-0" name="firstName" size="40" maxlength="40" value="${newHireCommand?.firstName}"/>
                                        <g:hasErrors bean="${errors}" field="firstName">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="firstName" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>

                                    <div class="row">
                                        <label for="t-1">Middle</label>
                                        <g:textField id="t-1" name="middleName" size="40" maxlength="40" value="${newHireCommand?.middleName}"/>
                                    </div>
                                    <div class="row">
                                        <label for="t-1">Last Name<span>*</span></label>
                                        <g:textField id="t-1" name="lastName" size="40" maxlength="40" value="${newHireCommand?.lastName}"/>
                                        <g:hasErrors bean="${errors}" field="lastName">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="lastName" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <label for="t-2">Organization<span>*</span></label>
                                        <g:select id="t-2"
                                                  name="companyId"
                                                  from="${orgList}"
                                                  value="${newHireCommand?.companyId}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="orgId"
                                                  optionValue="organizationName"/>
                                        <g:hasErrors bean="${errors}" field="companyId">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="companyId" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>
                                    <div class="row">
                                        <label for="t-3">Organizational Title</label>
                                        <g:textField id="t-3" name="title" size="40" maxlength="40" value="${newHireCommand?.title}"/>
                                    </div>
                                    <div class="row">
                                        <label for="supervisorName"><a href="javascript:showUserDialog('supervisorId', 'supervisorName' );">Supervisor Id<span style="color:red">*</span></a></label>
                                        <g:hiddenField name="supervisorId" />
                                        <g:textField id="supervisorName" name="supervisorName" size="50" readonly="true" value="${newHireCommand?.supervisorName}"/>

                                        <g:hasErrors bean="${errors}" field="supervisorId">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="supervisorId" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>

                                    <div class="row">
                                        <label for="t-5">Start Date (MM/dd/yyyy)<span></span></label>
                                        <g:textField id="t-5" name="startDate" size="20" value="${newHireCommand?.startDate}"/>
                                    </div>
                                    <div class="row">
                                        <label for="t-6">Role<span>*</span></label>
                                        <g:select id="t-6"
                                                  name="roleId"
                                                  from="${roleList}"
                                                  value="${newHireCommand?.roleId}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="value"
                                                  optionValue="label"/>
                                        <g:hasErrors bean="${errors}" field="roleId">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="roleId" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>
                                </div>

                                <div class="col">
                                    <div class="row">
                                        <label for="t-7">Department<span></span></label>
                                        <g:select id="t-7"
                                                  name="deptCd"
                                                  from="${deptList}"
                                                  value="${newHireCommand?.deptCd}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="orgId"
                                                  optionValue="organizationName"/>
                                        <g:hasErrors bean="${errors}" field="deptCd">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="deptCd" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>

                                    <div class="row">
                                        <label for="t-8">Job Classification<span></span></label>
                                        <g:select id="t-8"
                                                  name="jobCode"
                                                  from="${jobCodeList}"
                                                  value="${newHireCommand?.jobCode}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="value"
                                                  optionValue="label"/>
                                        <g:hasErrors bean="${errors}" field="jobCode">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="jobCode" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>
                                    <div class="row">
                                        <label for="t-9">Employment Type<span></span></label>
                                        <g:select id="t-9"
                                                  name="employeeType"
                                                  from="${userTypeList}"
                                                  value="${newHireCommand?.employeeType}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="value"
                                                  optionValue="label"/>
                                        <g:hasErrors bean="${errors}" field="employeeType">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="employeeType" as="list"/>
                                            </div>
                                        </g:hasErrors>

                                    </div>
                                    <div class="row">
                                        <label for="t-10">Last Date (MM/dd/yyyy)<span></span></label>
                                        <g:textField id="t-10" name="lastDate" size="20" value="${newHireCommand?.lastDate}"/>
                                    </div>

                                    <div class="row">
                                        <label for="t-11">Group<span></span></label>
                                        <g:select id="t-11"
                                                  name="groupId"
                                                  from="${groupList}"
                                                  value="${newHireCommand?.groupId}"
                                                  noSelection="['': '-Please Select-']"
                                                  optionKey="grpId"
                                                  optionValue="grpName"/>
                                        <g:hasErrors bean="${errors}" field="groupId">
                                            <div class="error">
                                                <g:renderErrors bean="${errors}" field="groupId" as="list"/>
                                            </div>
                                        </g:hasErrors>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <table>
                                            <g:each var="userAttr" in="${attributeList}">
                                                <tr>
                                                    <td class="tddarknormal" align="right">${userAttr?.name}
                                                        <g:if test="${userAttr.required == true}" >
                                                            <span style="color:red">*</span>
                                                        </g:if>
                                                        <g:hiddenField name="metaAttrId" value="${userAttr?.metadataElementId}" />
                                                        <g:hiddenField name="metaAttrName" value="${userAttr?.name}" />
                                                        <g:hiddenField name="metaAttrRequired" value="${userAttr?.required}" />
                                                    </td>
                                                    <td class="tdlightnormal" colspan="5">
                                                        <g:textField name="metaAttrValue" size="50" value="${userAttr?.value}"/>
                                                    </td>
                                                </tr>
                                            </g:each>

                                            <g:hasErrors bean="${errors}" field="metaAttrValue">
                                                <tr>
                                                    <td></td>
                                                    <td>
                                                        <div class="error">
                                                            <g:renderErrors bean="${errors}" field="metaAttrValue"
                                                                            as="list"/>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </g:hasErrors>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                <h4>Contact Information</h4>
                <fieldset>
                    <div class="block">
                        <div class="wrap alt">
                            <div class="col-1">
                                <div class="row">
                                    <label for="t-12">Location<span></span></label>
                                    <g:select id="t-12"
                                              name="locationCd"
                                              from="${locationArr}"
                                              value="${newHireCommand?.locationCd}"
                                              noSelection="['': '-Please Select-']"
                                              optionKey="value"
                                              optionValue="label"
                                              onchange="selectChange('locationBldg');" />
                                    <g:hasErrors bean="${errors}" field="locationCd">
                                        <div class="error">
                                            <g:renderErrors bean="${errors}" field="locationCd" as="list"/>
                                        </div>
                                    </g:hasErrors>
                                </div>

                                <div class="row">
                                    <label for="t-13">Building Nbr - Address</label>
                                    <g:textField id="t-13" class="code" name="bldgNum" size="5" maxlength="5" value="${newHireCommand?.bldgNum}"/>
                                    <g:textField class="phone" name="address1" size="20" maxlength="30" value="${newHireCommand?.address1}"/>
                                </div>

                                <div class="row">
                                    <label for="t-14">Address 2</label>
                                    <g:textField id="t-14" name="address2" size="30" maxlength="30" value="${newHireCommand?.address2}"/>
                                </div>

                                <div class="row">
                                    <label for="t-15">City</label>
                                    <g:textField id="t-15" name="city" size="30" maxlength="30" value="${newHireCommand?.city}"/>
                                </div>

                                <div class="row">
                                    <label for="t-16">State</label>
                                    <g:textField id="t-16" name="state" size="30" maxlength="30" value="${newHireCommand?.state}"/>
                                </div>

                                <div class="row">
                                    <label for="t-17">Postal Code</label>
                                    <g:textField id="t-17" name="postalCd" size="30" maxlength="30" value="${newHireCommand?.postalCd}"/>
                                </div>

                            </div>

                            <div class="col-1">

                                <div class="row">
                                    <label for="t-18">Corporate Email<span style="color:red">*</span></label>
                                    <g:textField id="t-18" name="email1" size="40" maxlength="40" value="${newHireCommand?.email1}"/>
                                    <g:hasErrors bean="${errors}" field="email1">
                                        <div class="error">
                                            <g:renderErrors bean="${errors}" field="email1" as="list"/>
                                        </div>
                                    </g:hasErrors>
                               </div>

                                <div class="row">
                                    <label for="t-19">Office Phone</label>
                                    <g:textField id="t-19" onblur="return validateInt(workAreaCode)" name="workAreaCode" class="code" size="3" maxlength="3" value="${newHireCommand?.workAreaCode}"/>
                                    <g:textField onblur="return validateInt(workPhone)" name="workPhone" class="phone" size="10" maxlength="10" value="${newHireCommand?.workPhone}"/>

                                    <g:hasErrors bean="${errors}" field="workPhone">
                                        <div class="error">
                                            <g:renderErrors bean="${errors}" field="workPhone" as="list"/>
                                        </div>
                                    </g:hasErrors>
                                </div>

                                <div class="row">
                                    <label for="t-20">Mobile Phone</label>
                                    <g:textField id="t-20" onblur="return validateInt(cellAreaCode)" name="cellAreaCode" class="code" size="3" maxlength="3" value="${newHireCommand?.cellAreaCode}"/>
                                    <g:textField onblur="return validateInt(cellPhone)" name="cellPhone" class="phone" size="10" maxlength="10" value="${newHireCommand?.cellPhone}"/>
                                </div>

                                <div class="row">
                                    <label for="t-21">Fax</label>
                                    <g:textField id="t-21" onblur="return validateInt(faxAreaCode)" name="faxAreaCode" class="code" size="3" maxlength="3" value="${newHireCommand?.faxAreaCode}"/>
                                    <g:textField onblur="return validateInt(faxPhone)" name="faxPhone" class="phone" size="10" maxlength="10" value="${newHireCommand?.faxPhone}"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="button">
                        <input type="submit" name="_finish" value="Submit" onclick="return submitPressed(this)"/>
                    </div>

                    <div class="button alt">
                        <input type="submit" name="_prev" value="Prev" onclick="return submitPressed(this)"/>
                    </div>

                    <div class="button alt">
                        <input type="submit" name="_cancel" value="Cancel" onclick="return submitPressed(this)"/>
                    </div>

                </fieldset>


                </g:form>

    </ui:define>
</ui:composition>