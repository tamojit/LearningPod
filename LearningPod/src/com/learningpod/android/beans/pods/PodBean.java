package com.learningpod.android.beans.pods;

import java.util.List;

public class PodBean {

	private String title;
	private String description;
	private List<PodQuestionBean> podElements = null;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PodQuestionBean> getPodElements() {
		return podElements;
	}
	public void setPodElements(List<PodQuestionBean> podElements) {
		this.podElements = podElements;
	}
}
