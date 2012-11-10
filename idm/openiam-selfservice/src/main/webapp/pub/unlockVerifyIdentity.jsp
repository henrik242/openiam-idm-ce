<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

		<div class="block">
				<div class="wrap">
					
					<div class="text-wrap">
						<p>Please answer ${unlockUserCmd.requiredCorrect} of ${unlockUserCmd.questionCount} questions correctly.</p>
					</div>
					
						<form:form commandName="unlockUserCmd" cssClass="distinction">
						<fieldset>
							<p><form:errors path="domainId"/> </p>
							<c:forEach items="${unlockUserCmd.answerList}" var="identityAnswer" varStatus="answer">
								
							<label for="t-1">Question: ${identityAnswer.questionText}<span>*</span></label>
								<form:hidden path="answerList[${answer.index}].identityQuestionId"/>
								<form:input path="answerList[${answer.index}].questionAnswer" size="60"/>
							</c:forEach>   
							

							<div class="button">
								<input type="submit" name="_target2" value="Next"/>   
							</div>
							<div class="button">
								<input type="submit" name="_target0" value="Previous"/>   
							</div>
                            <div class="button">
                                <input type="submit" name="_cancel" value="Cancel" />
                            </div>

						</fieldset>
					</form:form>
				</div>
			</div>

