function renderGroupList(data){
	var table = $("#groupList").find("tbody");
	table.empty();
	if(data && data.length>0){
		$.each(data, function(index, element) {
			var row="<tr><td><a href='group/edit?groupId="+element.grpId+"'>"+element.grpName+"</a></td>"
							+"<td>"+((element.description)?element.description:"")
							+"</td><td>"+((element.status)?element.status:"")+"</td></tr>";
			table.append(row);
		});
	}
}
function gotoGroupList(){
	window.location.href="secure/group/list";
}

function serializeGroupForm(){
	var group={};
	var groupModel={};
	var attributeList=[];
	
	 group.grpId=$("#group\\.grpId").val();
   group.grpName=$("#group\\.grpName").val();
   group.description=$("#group\\.description").val();
   group.metadataTypeId=$("#group\\.metadataTypeId").val();
   group.groupClass=$("#group\\.groupClass").val();
   group.status=$("#group\\.status").val();
   group.parentGrpId=$("#group\\.parentGrpId").val();
   group.companyId=$("#group\\.companyId").val();
   group.ownerId=$("#group\\.ownerId").val();
   group.inheritFromParent=$("#group\\.inheritFromParent").val()=='1'?true:false;
   
   $("#customAttributeTable tr[attrindex]").each(function(index) {
     var attr={};
     var fTd = $(this).find('td:first');
     var lTd = $(this).find('td:last');
     attr.name=fTd.find("input[type=text]").val();
     attr.id=fTd.find("input[type=hidden]:first").val();
     attr.groupId=fTd.find("input[type=hidden]:last").val();
     attr.value=lTd.find("input[type=text]").val();
     attributeList.push(attr);
   });
	 
   groupModel.group=group;
   groupModel.attributeList=attributeList;
	return groupModel;
}

function validateAttribute(control){
	var grName = $(control).parents("tr:first").children('td:first').find('input[type=text]:first').val();
	var alertOption = {message : "",type : 'error', delay : null};
	if(grName && grName!="**ENTER NAME**" && !$(control).val()){
		 if ($(control).attr("errorMsg")) {
         alertOption.message = $(control).attr("errorMsg");
     }
     alertOption.elementSelector = "#" + $(control).attr("id");
     showNotification(alertOption);
		return false; 
	}
	return true;
}
function deleteGroup(){
	var grpId=$("#group\\.grpId").val();
	$.postJSON("secure/group/delete/"+grpId,null, function(response){
		if(response && !hasError(response.notifications)){
			gotoGroupList();
		}
	});
}

$(document).ready(function(){
	$(".viewBtn").each(function(index) {
		var ctrl = $(this).prev().find('.controls');
		$(this).appendTo(ctrl);
	});
	$("#addAttrBtn").on("click", function(event){
		var attrRows = $("#customAttributeTable tbody tr");
		var tbody = $("#customAttributeTable tbody");
		var lastIdx = 0;
		if(attrRows && attrRows.length>0)
			lastIdx=parseInt($(attrRows[attrRows.length-1]).attr("attrindex"))+1;
		
		var html = "<tr attrIndex='"+lastIdx+"'>"
               +"<td>"
							 +"  <div class='control-group' id='attributeList["+lastIdx+"].nameControlWrapper'>"
							 +"    <div class='controls'>"
		           +"      <input id='attributeList"+lastIdx+".name' name='attributeList["+lastIdx+"].name' class='span12' value='**ENTER NAME**' size='20' type='text'>"
		           +"      <span class='help-inline'></span>"
	             +"    </div>"
               +"  </div>"
               +"  <input id='attributeList"+lastIdx+".id' name='attributeList["+lastIdx+"].id' value='NEW' type='hidden'>"
               +"  <input id='attributeList"+lastIdx+".groupId' name='attributeList["+lastIdx+"].groupId' value='' type='hidden'>"
							 +"</td>"
               +"<td>"
               +"  <div class='control-group' id='attributeList["+lastIdx+"].valueControlWrapper'>"
               +"  	 <div class='controls''>"
               +"  		 <input id='attributeList"+lastIdx+".value' name='attributeList["+lastIdx+"].value' class='span12' value='' size='40' maxlength='200' type='text'>"
               +"  		 <span class='help-inline'></span>"
               +"  	 </div>"
               +"  </div>"
               +"</td>"
               +"</tr>";
		
		tbody.append(html);	
			
		return stopEventPropagation(event);
	});
	
	$("#deleteGroup").on("click", function(event){
		openConfirmDialog("The group will be deleted. Are you sure?", deleteGroup);
		return stopEventPropagation(event);
	});
	$("#cancelBtn").on("click", function(event){
		gotoGroupList();
		return stopEventPropagation(event);
	});
});
