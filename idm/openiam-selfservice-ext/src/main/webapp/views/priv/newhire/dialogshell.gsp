<%

    ResourceBundle res = ResourceBundle.getBundle("securityconf");

    String SELFSERVICE_BASE_URL =  res.getString("SELFSERVICE_BASE_URL");
    String SELFSERVICE_EXT_BASE_URL =  res.getString("SELFSERVICE_EXT_BASE_URL");
    String SELFSERVICE_EXT_CONTEXT =  res.getString("SELFSERVICE_EXT_CONTEXT");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");

    String selfserviceExt =  SELFSERVICE_EXT_BASE_URL + "/" + SELFSERVICE_EXT_CONTEXT;
    String selfservice =  SELFSERVICE_BASE_URL + "/" + SELFSERVICE_CONTEXT;

%>

<head>

    <base target="_self" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'diamelleapp.css')}" />

    <r:layoutResources />

</head>

<body>

<iframe src ="selsupervisor.jsp" width="100%" height="600">
    <p>Your browser does not support iframes.</p>
</iframe>

</body>
