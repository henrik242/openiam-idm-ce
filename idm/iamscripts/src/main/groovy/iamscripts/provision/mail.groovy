
println("mail.groovy called.");

/*println("User objects: ");
println("User: email=" + user.emailAddresses);
println("User: phone =" + user.phones);
println("User: address =" + user.addresses);
println("User: attributes =" + user.userAttributes);

*/
if (user.email != null && user.email.length() > 0) {
	output=user.email
}else {

	output=user.firstName + "." + user.lastName + "@openiam.org"
}