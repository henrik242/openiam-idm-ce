<ui:composition template="/composition">
    <ui:define composition="${it}" name="menu">
        <g:render template="/menu_template" model="[selectedMenuItem: 'selfRegistration']"/>
    </ui:define>
    <ui:define composition="${it}" name="body">
        <g:form method="post" class="user-info" url="[action: 'edit.jsp', controller: 'registration']">
            <fieldset>
                <div class="block">
                    <div class="wrap alt">
                        <div class="col-1">
                            <div class="row alt">
                                <label for="t-1">First Name<span>*</span></label>
                                <g:textField id="t-1" name="firstName" value="${registrationCommand?.firstName}"/>
                                <g:hasErrors bean="${errors}" field="firstName">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="firstName" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>
                            <div class="row alt">
                                <label for="t-2">Middle<span></span></label>
                                <g:textField id="t-2" name="middleInit" value="${registrationCommand?.middleInit}"/>
                            </div>

                            <div class="row alt">
                                <label for="t-3">Last Name<span>*</span></label>
                                <g:textField id="t-3" name="lastName" value="${registrationCommand?.lastName}"/>
                                <g:hasErrors bean="${errors}" field="lastName">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="lastName" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>
                            <div class="row alt">
                                <label for="t-4">Maiden Name<span></span></label>
                                <g:textField id="t-4" name="maidenName" value="${registrationCommand?.maidenName}"/>
                            </div>

                            <div class="row alt">
                                <label for="t-5">Nickname<span></span></label>
                                <g:textField id="t-5" name="nickname" value="${registrationCommand?.nickname}"/>
                            </div>

                            <div class="row alt">
                                <label for="t-6">DOB(MM/dd/yyyy)<span></span></label>
                                <g:textField id="t-6" name="birthdate" value="${registrationCommand?.birthdate}"/>
                            </div>

                            <div class="row alt">
                                <label for="t-7">Organization<span>*</span></label>
                                <g:select id="t-7"
                                          name="companyId"
                                          from="${orgList}"
                                          value="${registrationCommand?.companyId}"
                                          noSelection="['': '-Please Select-']"
                                          optionKey="orgId"
                                          optionValue="organizationName"/>
                                <g:hasErrors bean="${errors}" field="companyId">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="companyId" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>
                            <div class="row alt">
                                <label for="t-8">Functional Title</label>
                                <g:textField id="t-8" name="title" value="${registrationCommand?.title}"/>
                            </div>

                            <div class="row alt">
                                <label for="t-9">Gender</label>
                                <g:select id="t-9"
                                          name="sex"
                                          from="${['M', 'F']}"
                                          valueMessagePrefix="registration"
                                          value="${registrationCommand?.companyId}"
                                          noSelection="['': '-Please Select-']"/>
                            </div>

                            <div class="row alt">
                                <label for="t-10">Role<span>*</span></label>
                                <g:select id="t-10"
                                          name="roleId"
                                          from="${roleList}"
                                          value="${registrationCommand?.roleId}"
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

                        <div class="col-1">
                        </div>
                    </div>
                </div>
            </fieldset>
            <h4>Contact Information</h4>
            <fieldset>
                <div class="block">
                    <div class="wrap alt">
                        <!-- Column 1 -->
                        <div class="col-1">

                            <div class="row">
                                <label for="t-11">Corporate Email <span style="color:red">*</span></label>
                                <g:textField id="t-11" name="email1" value="${registrationCommand?.email1}"/>
                                <g:hasErrors bean="${errors}" field="email1">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="email1" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>
                            <div class="row">
                                <label for="t-12">Address</label>
                                <g:textField id="t-12" name="address1" value="${registrationCommand?.address1}"/>
                            </div>

                            <div class="row">
                                <label for="t-13"></label>
                                <g:textField id="t-13" name="address2" value="${registrationCommand?.address2}"/>
                            </div>

                            <div class="row">
                                <label for="t-14">City</label>
                                <g:textField id="t-14" name="city" value="${registrationCommand?.city}"/>
                            </div>

                            <div class="row">
                                <label for="t-15">State</label>
                                <g:textField id="t-15" name="state" value="${registrationCommand?.state}"/>
                            </div>

                            <div class="row">
                                <label for="t-16">Zip</label>
                                <g:textField id="t-16" name="postalCd" value="${registrationCommand?.postalCd}"/>
                            </div>
                            <!-- Column 2 -->
                        </div>

                        <div class="col-1">
                            <div class="row">
                                <label for="t-17">Work Phone</label>
                                <g:textField id="t-17" name="workAreaCode" class="code"
                                             value="${registrationCommand?.workAreaCode}"/>
                                <g:textField name="workPhone" class="phone" value="${registrationCommand?.workPhone}"/>
                            </div>

                            <div class="row">
                                <label for="t-18">Mobile Phone</label>
                                <g:textField id="t-18" name="cellAreaCode" class="code"
                                             value="${registrationCommand?.cellAreaCode}"/>
                                <g:textField name="cellPhone" class="phone" value="${registrationCommand?.cellPhone}"/>
                            </div>

                            <div class="row">
                                <label for="t-19">Fax</label>
                                <g:textField id="t-19" name="faxAreaCode" class="code"
                                             value="${registrationCommand?.faxAreaCode}"/>
                                <g:textField name="faxPhone" class="phone" value="${registrationCommand?.faxPhone}"/>
                            </div>

                            <div class="row">
                                <label for="t-20">Emergency Contact</label>
                                <g:textField id="t-20" name="altCellAreaCode" class="code"
                                             value="${registrationCommand?.altCellAreaCode}"/>
                                <g:textField name="altCellNbr" class="phone"
                                             value="${registrationCommand?.altCellNbr}"/>
                            </div>

                            <div class="row">
                                <label for="t-21">Home Phone</label>
                                <g:textField id="t-21" name="homePhoneAreaCode" class="code"
                                             value="${registrationCommand?.homePhoneAreaCode}"/>
                                <g:textField name="homePhoneNbr" class="phone"
                                             value="${registrationCommand?.homePhoneNbr}"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="button">
                    <input type="submit" value="Submit"/>
                </div>

                <div class="button alt">
                    <input type="submit" name="_cancel" value="Cancel"/>
                </div>
            </fieldset>
        </g:form>
    </ui:define>
</ui:composition>
