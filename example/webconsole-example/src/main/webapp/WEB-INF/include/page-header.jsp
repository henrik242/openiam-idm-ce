<div class="header">
	<div class="header-container">
		<div id="logoContainer">
		</div>
		<c:if test="${sessionScope.userSession!=null}">
			<div class="navbar">
		        <div class="navbar-inner">
		          <a class="btn btn-navbar" data-toggle="collapse" data-target=".header-collapse">
		            <span class="icon-bar"></span> <span class="icon-bar"></span>
		          </a>
		          <div class="nav-collapse header-collapse">
		            <ul class="nav">
		                <li class="active"><a href="secure/group/list">Group</a></li>
		            </ul>
		          </div>
		        </div>
	      	</div>
	    </c:if>  	
	</div>
</div>