package com.chotoxautinh.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement(name = "quiz")
public class Quiz {
	private final StringProperty question;
	private final StringProperty answer;

	public Quiz() {
		this(null, null);
	}

	public Quiz(String question, String answer) {
		this.question = new SimpleStringProperty(question);
		this.answer = new SimpleStringProperty(answer);
	}

	@XmlElement(name = "question", required = true)
	public String getQuestion() {
        return question.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public StringProperty questionProperty() {
        return question;
    }

	@XmlElement(name = "answer", required = true)
	public String getAnswer() {
        return answer.get();
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public StringProperty answerProperty() {
        return answer;
    }
}
