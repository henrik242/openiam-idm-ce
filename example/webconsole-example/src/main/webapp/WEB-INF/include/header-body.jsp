<script type="text/javascript">
    var notification;
</script>
<c:if test="${!empty notification}">
    <script type="text/javascript">
        notification = {
            type:"<c:out value='${notification.type}'/>",
            message:"<c:out value='${notification.text}' escapeXml='false'/>",
            closable:false,
            global:true
        };
    </script>
</c:if>

<div class="message" id="alertArea"></div>
<div id="ajaxLoading" style="display: none;">
    <span id="ajaxLoadingText"><fmt:message key='ajax.loading'/></span>
</div>