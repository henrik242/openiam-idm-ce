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
        </script>
        <div class="block alt">
            <div class="wrap alt">
                <h5>SELECT TYPE OF USER</h5>
                <g:form method="post" class="type-user" url="[action: 'edit.jsp', controller: 'priv/newhire']">
                    <g:hiddenField id="submitFld" name="submitAction" value="_cancel" />
                    <g:hiddenField name="firstName" value="${newHireCommand.firstName}" />
                    <g:hiddenField name="lastName" value="${newHireCommand.lastName}" />
                    <g:hiddenField name="middleName" value="${newHireCommand.middleName}" />
                    <g:hiddenField name="companyId" value="${newHireCommand.companyId}" />
                    <g:hiddenField name="title" value="${newHireCommand.title}" />
                    <g:hiddenField name="supervisorId" value="${newHireCommand.supervisorId}" />
                    <g:hiddenField name="supervisorName" value="${newHireCommand.supervisorName}" />
                    <g:hiddenField name="roleId" value="${newHireCommand.roleId}" />
                    <g:hiddenField name="deptCd" value="${newHireCommand.deptCd}" />
                    <g:hiddenField name="jobCode" value="${newHireCommand.jobCode}" />
                    <g:hiddenField name="employeeType" value="${newHireCommand.employeeType}" />
                    <g:hiddenField name="lastDate" value="${newHireCommand.lastDate}" />
                    <g:hiddenField name="groupId" value="${newHireCommand.groupId}" />
                    <g:hiddenField name="locationCd" value="${newHireCommand.locationCd}" />
                    <g:hiddenField name="bldgNum" value="${newHireCommand.bldgNum}" />
                    <g:hiddenField name="address1" value="${newHireCommand.address1}" />
                    <g:hiddenField name="address2" value="${newHireCommand.address2}" />
                    <g:hiddenField name="city" value="${newHireCommand.city}" />
                    <g:hiddenField name="state" value="${newHireCommand.state}" />
                    <g:hiddenField name="postalCd" value="${newHireCommand.postalCd}" />
                    <g:hiddenField name="email1" value="${newHireCommand.email1}" />
                    <g:hiddenField name="workAreaCode" value="${newHireCommand.workAreaCode}" />
                    <g:hiddenField name="workPhone" value="${newHireCommand.workPhone}" />
                    <g:hiddenField name="cellAreaCode" value="${newHireCommand.cellAreaCode}" />
                    <g:hiddenField name="cellPhone" value="${newHireCommand.cellPhone}" />
                    <g:hiddenField name="faxAreaCode" value="${newHireCommand.faxAreaCode}" />
                    <g:hiddenField name="faxPhone" value="${newHireCommand.faxPhone}" />
                    <g:hiddenField name="startDate" value="${newHireCommand.startDate}" />

                    <fieldset>
                        <div class="row alt">
                            <label for="t-10">Role<span>*</span></label>
                            <g:select id="t-10"
                                      name="metadataTypeId"
                                      from="${metadataTypeAry}"
                                      value="${newHireCommand?.metadataTypeId}"
                                      noSelection="['': '-Please Select-']"
                                      optionKey="metadataTypeId"
                                      optionValue="description"/>
                            <g:hasErrors bean="${errors}" field="metadataTypeId">
                                <div class="error">
                                    <g:renderErrors bean="${errors}" field="metadataTypeId" as="list"/>
                                </div>
                            </g:hasErrors>
                        </div>

                        <div class="button">
                            <input type="submit" name="_cancel" value="Cancel" onclick="return submitPressed(this)"/>
                        </div>

                        <div class="button">
                            <input type="submit" name="_target1" value="Next" onclick="return submitPressed(this)"/>
                        </div>

                    </fieldset>

                </g:form>
            </div>
        </div>
    </ui:define>
</ui:composition>