package com.learningpod.android.beans.questions;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class QuestionChoiceBean implements Serializable {

	@XStreamAsAttribute
	private String correct;
	@XStreamAsAttribute
	private String choiceId;
	@XStreamAlias("p")
	private String choiceBody;
	public String getCorrect() {
		return correct;
	}
	public void setCorrect(String correct) {
		this.correct = correct;
	}
	public String getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}
	public String getChoiceBody() {
		return choiceBody;
	}
	public void setChoiceBody(String choiceBody) {
		this.choiceBody = choiceBody;
	}
	
	
}
