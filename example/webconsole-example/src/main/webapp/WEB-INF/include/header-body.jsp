<script type="text/javascript">
    var notification;
</script>
<c:if test="${notifications!=null}">
  <c:forEach var="item" items="${notifications}">
    <script type="text/javascript">
      notification = {
        type : "<c:out value='${item.type}'/>",
        message : "<c:out value='${item.text}' escapeXml='false'/>",
        closable : false,
        delay: 5000,
        global : true
      };
    </script>
  </c:forEach>
</c:if>  

<div class="message" id="alertArea"></div>
<div id="ajaxLoading" style="display: none;">
    <span id="ajaxLoadingText"><fmt:message key='ajax.loading'/></span>
</div>