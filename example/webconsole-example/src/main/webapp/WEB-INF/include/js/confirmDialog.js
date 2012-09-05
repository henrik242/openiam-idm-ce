var confirmClickHandler;
var cancelClickHandler;

$(document).ready(function() {
  
  $("#confirmDialog #confirmBtn").click(function(){
    if(confirmClickHandler)
      confirmClickHandler();
  });
  $("#confirmDialog #cancelDialogBtn").click(function(){
     if(isBusy())
      resetProcessingEvent();
    if(cancelClickHandler)
      cancelClickHandler();
  });
  
  $("#confirmDialog").on('hidden', function () {
    confirmClickHandler=null;
    cancelClickHandler=null;
  });
});

function openConfirmDialog(msg, confirmHandler, cancelHandler){
  $('#confirmDialog .confirmationMessage').text(msg);
  confirmClickHandler = confirmHandler;
  cancelClickHandler=cancelHandler;
  $('#confirmDialog').modal('show');    
}

function  closeConfirmDialog(){
  $('#confirmDialog .confirmationMessage').text("");
  $('#confirmDialog').modal('hide');
}