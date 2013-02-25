<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<table  width="600pt" >
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading">Associate Menus to Role: ${roleid}</h2>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <% if (request.getAttribute("msg") != null) { %>
    <tr>
        <td colspan="2" align="center" class="msg" >
            <%=request.getAttribute("msg") %>
        </td>
    </tr>
    <% } %>

    <tr>
        <td>

            <form:form commandName="roleMenuCmd">
                <form:hidden path="roleId" />
                <form:hidden path="domainId" />

                <table width="600pt">
                    <tr>
                        <td align="center" height="100%">
                            <fieldset class="userform" >
                                <legend>SELECT BASE MENU</legend>

                                <table class="fieldsetTable"  width="600pt" >

                                    <tr>
                                        <td class="plaintext">Select Group:</td>
                                        <td>

                                            <form:select path="menuId" multiple="false">
                                                <form:option value="" label="-Please Select-"/>
                                                <form:options items="${rootMenuList}" itemValue="id.menuId" itemLabel="menuName"/>
                                            </form:select>

                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                    </tr>
                    <tr class="buttonRow">
                        <td align="right"> <input type="submit" name="formBtn" value="Search"> <input type="submit" name="_cancel" value="Cancel" />  </td>
                    </tr>
                </table>
            </form:form>

            <table width="600pt">
                <tr>
                    <td align="center" height="100%">
                        <fieldset class="userform" >
                            <legend>ASSOCIATE MENUS TO A ROLE</legend>


                            <table class="resourceTable" cellspacing="2" cellpadding="2" width="600pt">
                                <tr class="header">
                                    <th colspan="2">MENU Name</th>
                                    <th></th>

                                </tr>
                                <c:if test="${menuList != null}" >
                                    <c:forEach items="${menuList}" var="m">

                                        <tr class="plaintext">
                                            <td class="tableEntry">
                                                <c:choose>
                                                    <c:when test="${m.selected}">
                                                        <img src="images/checkbox.png">
                                                    </c:when>
                                                    <c:otherwise>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                            <td class="tableEntry"> ${m.menuName}</td>
                                            <td class="tableEntry">
                                                <c:choose>
                                                    <c:when test="${!m.selected}">
                                                        <a href="updateRoleMembership.cnt?objtype=MENU&action=ADD&role=${roleid}&domain=${domainid}&objid=${m.id.menuId}&parentMenu=${parentMenu}">ADD to Role</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="updateRoleMembership.cnt?objtype=MENU&action=DEL&role=${roleid}&domain=${domainid}&objid=${m.id.menuId}&parentMenu=${parentMenu}">REMOVE from Role</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                    </c:forEach>
                                </c:if>

                            </table>
                        </fieldset>
                    </td>
                </tr>
            </table>


        </td>
    </tr>
</table>
</div>
