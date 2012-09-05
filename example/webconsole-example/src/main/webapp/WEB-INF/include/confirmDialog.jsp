    <div class="modal hide fade" id="confirmDialog" data-keyboard="false" data-backdrop="static">
      <div class="modal-header">
        <a class="close" data-dismiss="modal">&times;</a>
        <h2><fmt:message key='title.modal.confirm' /></h2>
      </div>
      <div class="modal-body">
        <div class="well">
              <fieldset>
               <span class="confirmationMessage"></span>
              </fieldset>
            </div>
      </div>
      <div class="modal-footer">
        <a href="javascript:void(0);" class="btn btn-success" id="confirmBtn"><fmt:message key='button.ok' /></a>
        <a href="javascript:void(0);" class="btn" data-dismiss="modal" id="cancelDialogBtn"><fmt:message key='button.cancel' /></a>
      </div>
    </div>


 <script>
   <%@include file="js/confirmDialog.js"%>
 </script>
    