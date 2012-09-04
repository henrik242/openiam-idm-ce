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
	window.location.href="group/list";
}
$(document).ready(function(){
	
	$("#addAttrBtn").on("click", function(event){
		var attrRows = $("#customAttributeTable tbody tr");
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
		
		attrRows.parents("tbody:first").append(html);	
			
		return stopEventPropagation(event);
	});
	
});
