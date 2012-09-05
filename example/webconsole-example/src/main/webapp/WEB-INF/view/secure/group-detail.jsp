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
				<form:form id="groupDetail" modelAttribute="groupModel" class="span8 form-inline">
					<fieldset>
					    <legend>ORGANIZATION DETAILS</legend>
						<html:inputField name="group.grpId" label="label.grp.id" cssClass="span4" 
						                 size="32" maxlength="32" readonly="true"/>
						
						<html:inputField name="group.grpName" label="label.grp.name" cssClass="span4" requiredField="true"
						                 errorMsg="error.groupName.required" size="40" maxlength="40"/>  
						                 
						<html:inputField name="group.description" label="label.grp.description" cssClass="span4" 
						                 size="40" maxlength="80"/>  
						     
						<html:selectField name="group.metadataTypeId" label="label.grp.type" cssClass="span4" 
						                 firstOptionValue="" firstOptionText="-Select a value" 
						                 options="${groupModel.typeList}" itemValue="metadataTypeId" itemLabel="description"/> 
                                    
						<html:inputField name="group.groupClass" label="label.grp.groupClass" cssClass="span4" 
						                 size="40" maxlength="40"/>
						                 
						<html:selectField name="group.status" label="label.grp.status" cssClass="span4" 
						                 firstOptionValue="" firstOptionText="-Select a value" 
						                 options="${groupStatusList}" itemValue="status" itemLabel="status"/>
						
						<html:inputField name="group.parentGrpId" label="label.grp.parentGrp" cssClass="span4" 
						                 size="32"  readonly="true" rowClass="parentGrpup"/>
						<c:if test="${groupModel.group.parentGrpId != null}" >
							  <a class="viewBtn" href="secure/group/edit?groupId=${groupModel.group.parentGrpId}">View</a>
						</c:if>  

						<html:selectField name="group.companyId" label="label.grp.company" cssClass="span4" 
						                 firstOptionValue="" firstOptionText="-Select a value" 
						                 options="${groupModel.orgList}" itemValue="orgId" itemLabel="organizationName"/>
						                 
						<html:inputField name="group.ownerId" label="label.grp.owner" cssClass="span4" 
						                 size="40"  maxlength="40"/>
						                 
						<html:selectField name="group.inheritFromParent" label="label.grp.inheritFromParent" cssClass="span4" 
						                 firstOptionValue="" firstOptionText="-Select a value" 
						                 options="${inheritFromParentList}" itemValue="value" itemLabel="label"/>
						                 
						 <fieldset >
                              <legend>CUSTOM ATTRIBUTES </legend>
                              <table id="customAttributeTable" class="table">
						    	<thead>
									<tr>
										<th>Name</th>
										<th>Value</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${groupModel.attributeList != null}" >
                                         <c:forEach items="${groupModel.attributeList}" var="attributeList" varStatus="attr">
                                             <tr attrIndex="${attr.index}">
                                                 <td>
                                                    <html:inputField name="attributeList[${attr.index}].name" cssClass="span12" 
						                 							size="20" label=""/>

                                                     <form:hidden path="attributeList[${attr.index}].id" />
                                                     <form:hidden path="attributeList[${attr.index}].groupId" />
                                                 </td>
                                                 <td>
                                                 	<html:inputField name="attributeList[${attr.index}].value" cssClass="span12" 
						                 							errorMsg="error.attribute.value.required" validator="validateAttribute" 
						                 							size="40" maxlength="200"  label=""/>
												 </td>
                                             </tr>
                                         </c:forEach>
                                     </c:if>
								</tbody>
    						</table>
    						<div class="span7 pull-left"><i>To remove a custom attribute, leave the name blank</i></div>
    						<div class="span3 pull-right"><button id="addAttrBtn" class="btn btn-success pull-right">Add Attribute</button></div>
                         </fieldset>                                        
                    </fieldset>
                    <div class="row-fluid">
                    	<div class="groupToolBar span7 pull-right">
                    		<button id="cancelBtn" class="btn pull-right">Cancel</button>
                    		<button type="submit" class="btn btn-success pull-right">Save</button>
                    		<button id="deleteGroup" class="btn pull-right">Delete</button>
                    		<a href="secure/group/new?parentGroupId=${groupModel.group.grpId}" id="addChildGroup" class="span4 pull-right"><i class="icon-plus"></i>New group</a>
                    	</div>
                    </div>
                    <ajax:ajaxPostForm formName="groupDetail" processedPostUrl="secure/group/save" serializator="serializeGroupForm" callback="gotoGroupList"/>
				</form:form>
				<div class="span2"></div>
			</div>
			
			<c:if test="${groupModel.childGroup != null}" >
				<div class="row-fluid">
					<div  class="span2"></div>
					<div id="groupList"class="span8">
						<fieldset >
							<legend>CHILD GROUPS </legend>
							<table class="table table-bordered table-hover">
						    	<thead>
									<tr>
										<th>Name</th>
										<th>Description</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${groupModel.childGroup}" var="group">
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
		<%@include file="../../include/confirmDialog.jsp"%>
	</div>
</body>
</html>