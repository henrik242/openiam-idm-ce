<ui:composition template="/composition">
    <ui:define composition="${it}" name="menu">
        <g:render template="/menu_template" model="[selectedMenuItem: 'newhire']"/>
    </ui:define>
    <ui:define composition="${it}" name="body">
        <div class="block alt">
            <div class="wrap alt">
                <h5>SELECT TYPE OF USER</h5>
                <g:form method="post" class="type-user" url="[action: 'seltype.jsp', controller: 'newhire']">
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
                            <input type="submit" name="_cancel" value="Cancel"/>
                        </div>

                        <div class="button">
                            <input type="submit" name="_target1" value="Next"/>
                        </div>

                    </fieldset>

                </g:form>
            </div>
        </div>
    </ui:define>
</ui:composition>