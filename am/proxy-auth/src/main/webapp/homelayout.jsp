<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/all.css" type="text/css"/>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
    <script src="<%= request.getContextPath() %>/js/cufon-yui.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/myriad-pro.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/calibri.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.main.js"></script>
    <title>OpenIAM IAM v2.3.1</title>

    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">

</head>
<script type="text/javascript">
    <!--
    function getCookie(check_name)
    {
        var a_all_cookies = document.cookie.split( ';' );
        var a_temp_cookie = '';
        var cookie_name = '';
        var cookie_value = '';
        var b_cookie_found = false; // set boolean t/f default f

        for ( i = 0; i < a_all_cookies.length; i++ )
        {
            // now we'll split apart each name=value pair
            a_temp_cookie = a_all_cookies[i].split( '=' );

            // and trim left/right whitespace while we're at it
            cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');

            // if the extracted name matches passed check_name
            if ( cookie_name == check_name )
            {
                b_cookie_found = true;
                // we need to handle case where cookie has no value but exists (no = sign, that is):
                if ( a_temp_cookie.length > 1 )
                {
                    cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );
                }
                // note that in cases where cookie is initialized but no value, null is returned
                return cookie_value;
                break;
            }
            a_temp_cookie = null;
            cookie_name = '';
        }
        if ( !b_cookie_found )
        {
            return null;
        }
    }


    function setPoFromCookie() {
        var cookieValue = getCookie("IAM_DEFAULT_PO");
        var po = document.getElementById("po");
        po.value = cookieValue;

    }


    //-->
</script>

<%
    String userId = (String)session.getAttribute("userId");
    String bodypage = request.getParameter("bodyjsp");
    if (bodypage == null) {
        bodypage = (String)request.getAttribute("bodyjsp");
    }

    String login = (String)session.getAttribute("login");

    String url = (String)session.getAttribute("logoUrl");
    String title = (String)session.getAttribute("title");
    String welcomePageUrl = (String)session.getAttribute("welcomePageUrl");

    if (url == null) {
        url = (String)request.getAttribute("logoUrl");
        title = (String)request.getAttribute("title");
    }

%>


<body>
<div id="wrapper">
    <div id="header">
        <h1 class="logo"><a href="#">OpenIAM</a></h1>
    </div>
    <div id="main">
        <div id="content" class="otherness">
            <div class="block">
                <div class="wrap">
                    <jsp:include page="<%=bodypage%>" />
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

