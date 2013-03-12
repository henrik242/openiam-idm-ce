<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



<table class="resource alt">
	<tbody>
		<tr class="caption">
			<th>Application Name</th>
			<th>Description</th>
		</tr>

          <c:forEach items="${resourceList}" var="res" >

                <tr>
                        <td>
                            <a href="${res.URL}">${res.name}</a>
                        </td>
                        <td>
                            ${res.description}
                        </td>
                </tr>
				</c:forEach>

<tbody>
</table>
