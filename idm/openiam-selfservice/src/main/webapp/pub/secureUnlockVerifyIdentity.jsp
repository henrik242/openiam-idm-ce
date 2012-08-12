<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

		<div class="block">
				<div class="wrap">

                    <c:if test="${verifyIdCmd.message != NULL}" >
                        <div class="text-wrap">
                            <p>${verifyIdCmd.message}</p>
                        </div>
                    </c:if>

                    <c:if test="${verifyIdCmd.message == NULL}" >

                        <div class="text-wrap">
                            <p>Please answer ${verifyIdCmd.requiredCorrect} of ${verifyIdCmd.questionCount} questions correctly.</p>
                        </div>

                            <form:form commandName="verifyIdCmd" cssClass="distinction">
                            <fieldset>
                                <p><form:errors path="domainId"/> </p>
                                <c:forEach items="${verifyIdCmd.answerList}" var="identityAnswer" varStatus="answer">

                                <label for="t-1">Question: ${identityAnswer.questionText}<span>*</span></label>
                                    <form:hidden path="answerList[${answer.index}].identityQuestionId"/>
                                    <form:input path="answerList[${answer.index}].questionAnswer" size="60"/>
                                </c:forEach>

                                <div class="button">
                                    <input type="submit" name="_cancel" value="Cancel" />
                                </div>
                                <div class="button">
                                    <input type="submit" name="_target1" value="Next"/>
                                </div>
                            </fieldset>
                            </form:form>

                      </c:if>
				</div>
			</div>

