package com.avaya.grt.mappers;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RegistrationQuestions implements Serializable{
	private static final long serialVersionUID = 1L;
	private String regId;
	private String questionKey;
	private String answerKey;
	private SiteRegistration siteRegistration;
	
	public SiteRegistration getSiteRegistration() {
		return siteRegistration;
	}
	public void setSiteRegistration(SiteRegistration siteRegistration) {
		this.siteRegistration = siteRegistration;
	}
	public RegistrationQuestions(){
		
	}
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
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRegId())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("regId", getRegId())
			.toString();
	}
}
