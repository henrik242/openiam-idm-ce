<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.ResourceBundle" %>

<%
    ResourceBundle res = ResourceBundle.getBundle("datasource");
    String url = res.getString("UNLOCK_URL");

    String mode =  (String)request.getAttribute("mode");
    if (mode != null) {
        if ("2".equalsIgnoreCase(mode)) {
            // password change was cancelled - make sure that we expire the openiam cookie as well.

            Cookie[] cookieAry =  request.getCookies();
            if (cookieAry != null) {
                for (int i=0; i < cookieAry.length; i++) {
                    Cookie c = cookieAry[i];

                    System.out.println("- cookie value: " + c.getName() + " " + c.getDomain() + " " + c.getPath() + " " + c.getValue());

                    if (c.getName().equalsIgnoreCase("IAM_PARAM")) {
                        c.setMaxAge(0);
                        c.setPath("/");
                        response.addCookie(c);
                    }

                }
            }
        }
    }


%>

        <form:form commandName="loginCmd">
                                          <form:hidden path="clientIP" />
                                                <fieldset>

                                                        <label for="t-1">Enter Login ID:<span>*</span></label>
                                                         <form:input path="principal" />
                                                        <p><form:errors path="principal" /></p>
                                                        <p><form:errors path="password"  /></p>
                                                        <label for="t-2">Enter Password: <span>*</span></label>
                                                        <form:password path="password" autocomplete="off" />

                                                        <div class="button">
                                                                <input class="diff" type="reset" value="Reset" />
                                                        </div>
                                                        <div class="button">
                                                                <input class="diff" type="submit" value="Login" />
                                                        </div>
                                                        <div class="button">
                                                                <input class="diff" type="submit" value="Unlock Account" onclick="location.href='<%=url%>';return false;" />
                                                        </div>
                                                </fieldset>
                                        </form:form>






