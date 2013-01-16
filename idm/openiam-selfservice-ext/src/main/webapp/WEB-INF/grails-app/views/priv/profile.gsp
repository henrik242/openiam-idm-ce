<ui:composition template="/composition">
    <ui:define composition="${it}" name="menu">
        <g:render template="/menu_template" model="[selectedMenuItem: 'profile']"/>
    </ui:define>
    <ui:define composition="${it}" name="body">
        <g:form method="post" class="profile" url="[action: 'edit.jsp', controller: 'profile']">
            <fieldset>
                <div class="block">
                    <div class="wrap alt">

                        <!-- column 1  -->

                        <div class="col">
                            <div class="row">
                                <label for="t-1">First Name<span>*</span></label>
                                <g:textField id="t-1" name="firstName" size="40" maxlength="40" value="${profileCommand?.firstName}"/>
                                <g:hasErrors bean="${errors}" field="firstName">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="firstName" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>

                            <div class="row">
                                <label for="t-2">Primary Phone </label>
                                <g:textField id="t-2" name="workAreaCode" size="3" class="code" value="${profileCommand?.workAreaCode}"/>
                                <g:textField name="workPhone" size="10" class="phone" value="${profileCommand?.workPhone}"/>
                            </div>

                            <div class="row">
                                <label for="t-3">Fax </label>
                                <g:textField id="t-3" name="faxAreaCode" size="3" class="code" value="${profileCommand?.faxAreaCode}"/>
                                <g:textField name="faxPhone" size="10" class="phone" value="${profileCommand?.faxPhone}"/>
                            </div>

                            <div class="row">
                                <label for="t-4">Primary Email<span>*</span></label>
                                <g:textField id="t-4" name="email1" value="${profileCommand?.email1}"/>
                                <g:hasErrors bean="${errors}" field="firstName">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="firstName" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>



                        </div>

                        <div class="col">
                            <div class="row">
                                <label for="t-7">Last Name<span>*</span></label>
                                <g:textField id="t-7" name="lastName" value="${profileCommand?.lastName}"/>
                                <g:hasErrors bean="${errors}" field="firstName">
                                    <div class="error">
                                        <g:renderErrors bean="${errors}" field="firstName" as="list"/>
                                    </div>
                                </g:hasErrors>
                            </div>
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

        </g:form>
    </ui:define>
</ui:composition>