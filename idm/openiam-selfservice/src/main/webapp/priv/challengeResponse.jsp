<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<div class="block">
		<div class="wrap">
			
			<div class="text-wrap">
				<p>Answers to the challenge response questions will be used to verify your identity if you forget your password					
				</p>
			</div>
			<form:form commandName="identityQuestCmd" cssClass="alt">
				
				<fieldset>
					<p><form:errors path="principal" /></p>
					<c:forEach items="${identityQuestCmd.answerList}" var="identityAnswer" varStatus="answer">
					 <form:select path="answerList[${answer.index}].identityQuestionId">
		  						<form:option value=""  label="-Select a value-" />
		  						<form:options items="${identityQuestCmd.questionList}" itemValue="identityQuestionId" itemLabel="questionText"  />
	 							</form:select>
	 				<form:input path="answerList[${answer.index}].questionAnswer" size="60"/>
				<form:hidden path="answerList[${answer.index}].objectState"/>

					</c:forEach>


                    <div class="button">
                        <input type="submit" name="save" value="Submit"/>
                    </div>

					<div class="button">
						 <input type="submit" name="_cancel" value="Cancel" />
					</div>


				</fieldset>
			</form:form>
		</div>
	</div>