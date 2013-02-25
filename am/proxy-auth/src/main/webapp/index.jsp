<%@ page import="java.util.ResourceBundle" %>
<%

    ResourceBundle res = ResourceBundle.getBundle("datasource");
    String HTTPS_ONLY = res.getString("HTTPS_ONLY");

System.out.println("index.jsp called.");


    session.invalidate(); // removes everythin

    String reqUrl =  request.getParameter("requrl") ;
    
    System.out.println("Requested URL=" + reqUrl);

    if (reqUrl != null && reqUrl.length() > 0) {

        if ("true".equalsIgnoreCase(HTTPS_ONLY)) {
            // update the requrl to ony use https

            reqUrl =  reqUrl.replace("http:", "https:");
        }


        request.getSession().setAttribute("requrl", reqUrl);


    }

    RequestDispatcher rd = request.getRequestDispatcher("/login.selfserve");
    rd.forward(request,response);


%>

