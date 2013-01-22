<ui:composition template="/composition">
    <ui:define composition="${it}" name="menu">
        <g:render template="menu_template" model="[selectedMenuItem: 'confirmation']"/>
    </ui:define>
    <ui:define composition="${it}" name="body">
        <P>Information has been successfully saved.</P>
    </ui:define>
</ui:composition>