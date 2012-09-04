<!DOCTYPE html>
<%@page pageEncoding="UTF-8" session="true"%>

<html>
<head>
<title>WebConsole Example</title>
<%@include file="../include/header.jsp"%>
<script type="text/javascript" src="${resourceServerUrl}/js/group/group.js"></script>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>
	<%@include file="../include/header-body.jsp"%>
	<div id="openiam-container" class="container-fluid">
		<div class="container-inner">
			<%@include file="../include/page-header.jsp"%>
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
						
						<html:inputField name="group.grpName" label="label.grp.name" cssClass="span4" 
						                 size="40" maxlength="40"/>  
						                 
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
						
						<html:inputField name="group.parentGrpId" label="label.grp.groupClass" cssClass="span4" 
						                 size="32"  readonly="true"/>
						<c:if test="${groupModel.group.parentGrpId != null}" >
							  <a href="group/edit?groupId=${groupModel.group.parentGrpId}">View</a>
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
						                 							size="40" maxlength="200"  label=""/>
												 </td>
                                             </tr>
                                         </c:forEach>
                                     </c:if>
								</tbody>
    						</table>
    						<div class="span2 pull-right"><button id="addAttrBtn" class="btn btn-success pull-right">Add Attribute</button></div>
                         </fieldset>                                        
                    </fieldset>
                    <div class="row-fluid">
                    	<div class="span2 pull-right"><button type="submit" class="btn btn-success pull-right">Save</button></div>
                    </div>
                    <ajax:ajaxPostForm formName="groupDetail" processedPostUrl="group/save" callback="gotoGroupList"/>
				</form:form>
				<div class="span2"></div>
			</div>
		</div>
		<%@include file="../include/footer.jsp"%>
	</div>
</body>
</html>