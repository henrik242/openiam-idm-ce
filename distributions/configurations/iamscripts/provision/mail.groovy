if (user.email != null && user.email.length() > 0) {
	output=user.email
}else {

	output=user.firstName + "." + user.lastName + "@openiam.org"
}