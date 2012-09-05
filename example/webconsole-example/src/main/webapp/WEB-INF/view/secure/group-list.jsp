<!DOCTYPE html>
<%@page pageEncoding="UTF-8" session="true"%>

<html>
<head>
<title>WebConsole Example</title>
<%@include file="../../include/header.jsp"%>
<script type="text/javascript" src="${resourceServerUrl}/js/group/group.js"></script>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>
	<%@include file="../../include/header-body.jsp"%>
	<div id="openiam-container" class="container-fluid">
		<div class="container-inner">
			<%@include file="../../include/page-header.jsp"%>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span8 pageTitle" ><h2>Group Manager</h2></div>
				<div class="span2"></div>
			</div>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span8">
					<form id="groupFilterForm" class="form-inline span8" method="post">
						<fieldset>
						    <legend>FILTER GROUP LIST</legend>
							<div class="control-group" id="${name}ControlWrapper">
								<label class="span4 control-label" >Filter by:</label>
								<label class="span3 control-label" >Group Name:</label>
								<div class="controls">
									<input type="text" class="span4" id="grpName" name="grpName"/>
								</div>
							</div>
							<div class="span12 hLine"></div>
							<div class="span3  "><a href="secure/group/new"><i class="icon-plus"></i>New group</a></div>
							<div class="span2 pull-right"><button type="submit" class="btn btn-success pull-right">Filter</button></div>
	                    </fieldset>
	                   <ajax:ajaxPostForm formName="groupFilterForm" processedPostUrl="secure/group/apply-filter" callback="renderGroupList"/>
					</form>
				</div>
				<div class="span2"></div>
			</div>
	<c:if test="${searchResult != null}" >		
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span8 " id="groupList">
					<fieldset>
					    <legend>FILTER GROUP LIST</legend>
						    <table class="table table-bordered table-hover">
						    	<thead>
									<tr>
										<th>Name</th>
										<th>Description</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${searchResult}" var="group">
										<tr>
											<td><a href="secure/group/edit?groupId=${group.grpId}">${group.grpName}</a></td>
											<td>${group.description}</td>
											<td>${group.status}</td>
										</tr>
									</c:forEach>
								</tbody>
    						</table>
                    </fieldset>
				</div>
				<div class="span2"></div>
			</div>
	</c:if>		
		</div>
		<%@include file="../../include/footer.jsp"%>
	</div>
</body>
</html>