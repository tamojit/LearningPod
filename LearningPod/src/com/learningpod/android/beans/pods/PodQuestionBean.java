package com.learningpod.android.beans.pods;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class PodQuestionBean {

	@XStreamAsAttribute
	private String itemId;
	private List<PodQuestionBean> explanations = null;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public List<PodQuestionBean> getArr() {
		return explanations;
	}
	public void setArr(List<PodQuestionBean> explanations) {
		this.explanations = explanations;
	}
	
	
}
