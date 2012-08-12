package org.openiam.selfsrvc.pswd;

import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;

import java.io.Serializable;
import java.util.List;

/**
 * Command object for the SecureIdVerification
 * @author suneet
 *
 */
public class SecureIdVerificationCommand implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 5190211705748357722L;

	protected String password;
	protected String confPassword;
	protected String domainId;
	protected String principal;
	protected String submit;
	protected List<UserIdentityAnswer> answerList;
	protected List<IdentityQuestion> questionList;
	protected int questionCount;
	protected int requiredCorrect;
    protected String message;


	public SecureIdVerificationCommand() {
		super();

	}

	public SecureIdVerificationCommand(String principal,
                                       String domainId, String password,
                                       String confPassword) {
		super();
		this.confPassword = confPassword;
		this.domainId = domainId;
		this.password = password;
		this.principal = principal;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfPassword() {
		return confPassword;
	}
	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getSubmit() {
		return submit;
	}
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	
	public List<UserIdentityAnswer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<UserIdentityAnswer> answerList) {
		this.answerList = answerList;
	}

	public List<IdentityQuestion> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<IdentityQuestion> questionList) {
		this.questionList = questionList;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public int getRequiredCorrect() {
		return requiredCorrect;
	}

	public void setRequiredCorrect(int requiredCorrect) {
		this.requiredCorrect = requiredCorrect;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
