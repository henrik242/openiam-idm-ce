function onSelectSyncSource() {
	var value = $("#syncConfig\\.synchAdapter").val();
	$('#soUser').show();
	var list = $('#soList');
	$(".common-row").show();
	$(".csv-row").hide();
	$(".ldap-row").hide();
	$(".db-row").hide();
	$(".ws-row").hide();
	list.hide();
	list.val("USER");
	if (value == "RDBMS") {
		$(".common-row").show();
		$(".db-row").show();
	} else if (value == "CSV") {
		$('#soUser').hide();
		$('#soList').show();
		$(".csv-row").show();
	} else if (value == "LDAP") {
		$(".ldap-row").show();
	} else if (value == "WS") {
		$(".ws-row").show();
	} else if (value == "CUSTOM") {
		$(".common-row").show();
		$(".csv-row").show();
		$(".ldap-row").show();
		$(".db-row").show();
		$(".ws-row").show();
		list.val("USER");
	}
}

