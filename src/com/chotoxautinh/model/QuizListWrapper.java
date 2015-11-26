package com.chotoxautinh.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "quizList")
public class QuizListWrapper {
	private List<Quiz> quizList;

	@XmlElement(name = "quiz")
	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void setQuizList(List<Quiz> quizList) {
		this.quizList = quizList;
	}
}
