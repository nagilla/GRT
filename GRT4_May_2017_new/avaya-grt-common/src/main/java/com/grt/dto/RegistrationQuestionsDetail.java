package com.grt.dto;

import java.io.Serializable;

public class RegistrationQuestionsDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private String regId;
	private String questionKey;
	private String answerKey;
	public String getAnswerKey() {
		return answerKey;
	}
	public void setAnswerKey(String answerKey) {
		this.answerKey = answerKey;
	}
	public String getQuestionKey() {
		return questionKey;
	}
	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	
	
}
