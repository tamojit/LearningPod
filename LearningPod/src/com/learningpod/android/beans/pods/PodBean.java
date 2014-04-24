package com.learningpod.android.beans.pods;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.learningpod.android.parser.DescriptionConverter;
public class PodBean implements Serializable{

	private String title;
	//@XStreamConverter(DescriptionConverter.class)
	private String description;	
	private String scorable;
	private String userId;
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
