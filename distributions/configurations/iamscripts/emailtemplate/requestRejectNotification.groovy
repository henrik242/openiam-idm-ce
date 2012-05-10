
def String requestId = req.getNotificationParam("REQUEST_ID").valueObj
def String requestReason = req.getNotificationParam("REQUEST_REASON").valueObj
def String requestor = req.getNotificationParam("REQUESTOR").valueObj
def String targetUser = req.getNotificationParam("TARGET_USER").valueObj


def String to;
try {
	if (user != null) {
  		to = user.firstName + " " + user.lastName;
	}else {
		to = targetUser;
	}
}catch(groovy.lang.MissingPropertyException e) {
	to = targetUser;
}


emailStr = "Dear " + to + ": \n\n" +
 	"This is to notify you that the request summarized below was rejected. \n\n" + 
 	"\n\n" +
 	"Request ID: " + requestId + " \n" +
 	"Request Type: " + requestReason + " \n" +
 	"Approver: " + requestor + " \n" +
 	"For: " + targetUser
 	 	
 	
output=emailStr