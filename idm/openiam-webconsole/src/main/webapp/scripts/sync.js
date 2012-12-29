function onSelectSyncSource() {
	var value = $("#syncConfig\\.synchAdapter").val();
	$('#soUser').show();
	var list = $('#soList');
	list.hide();
	if (value == "") {
		$(".commonRow").show();
		$(".csvRow").hide();
		$(".ldapRow").hide();
		$(".dbRow").hide();
		list.val = "USER";
	} else if (value == "RDBMS") {
		$(".commonRow").show();
		$(".csvRow").hide();
		$(".ldapRow").hide();
		$(".dbRow").show();
		list.val("USER");
	} else if (value == "CSV") {
		$('#soUser').hide();
		$('#soList').show();
		$(".commonRow").show();
		$(".csvRow").show();
		$(".ldapRow").hide();
		$(".dbRow").hide();
	} else if (value == "LDAP") {
		$(".commonRow").show();
		$(".csvRow").hide();
		$(".ldapRow").show();
		$(".dbRow").hide();
		list.val("USER");
	} else if (value == "WS") {
		$(".commonRow").show();
		$(".csvRow").hide();
		$(".ldapRow").hide();
		$(".dbRow").hide();
		list.val("USER");
	} else if (value == "CUSTOM") {
		$(".commonRow").show();
		$(".csvRow").show();
		$(".ldapRow").show();
		$(".dbRow").show();
		list.val("USER");
	}
}

