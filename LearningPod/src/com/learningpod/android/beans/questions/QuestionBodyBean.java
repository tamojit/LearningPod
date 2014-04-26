package com.learningpod.android.beans.questions;

import java.io.Serializable;

public class QuestionBodyBean implements Serializable{

	public String getQuestionBodyStr() {
		return questionBodyStr;
	}
	public void setQuestionBodyStr(String questionBodyStr) {
		this.questionBodyStr = questionBodyStr;
	}
	public String getQuestionImage() {
		return questionImage;
	}
	public void setQuestionImage(String questionImage) {
		this.questionImage = questionImage;
	}
	private String questionBodyStr;
	private String questionImage;
}
