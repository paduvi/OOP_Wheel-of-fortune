package com.chotoxautinh.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.chotoxautinh.model.HighScore;
import com.chotoxautinh.model.HighScoreListWrapper;
import com.chotoxautinh.model.Quiz;
import com.chotoxautinh.model.QuizListWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class XmlUtil {
	
	private static File getFolder() throws URISyntaxException{
		return new File(XmlUtil.class.getResource("/com/chotoxautinh/savedata/").toURI());
	}

	public static ObservableList<HighScore> loadHighScoreDataFromFile(File file) {
		ObservableList<HighScore> highScoreList = FXCollections.observableArrayList();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(HighScoreListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			HighScoreListWrapper wrapper = (HighScoreListWrapper) um.unmarshal(file);

			highScoreList.clear();
			highScoreList.addAll(wrapper.getHighScoreList());

		} catch (JAXBException e) {
			System.err.println("File not valid");
		}
		return highScoreList;
	}

	public static void saveHighScoreDataToFile(File file, List<HighScore> highScoreList) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(HighScoreListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			HighScoreListWrapper wrapper = new HighScoreListWrapper();
			wrapper.setHighScoreList(highScoreList);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static ObservableList<Quiz> loadQuizDataFromFile() {
		ObservableList<Quiz> quizList = FXCollections.observableArrayList();
		JAXBContext context;
		try {
			File file = new File(getFolder(), "quiz.xml");
			context = JAXBContext.newInstance(QuizListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			QuizListWrapper wrapper = (QuizListWrapper) um.unmarshal(file);

			quizList.clear();
			quizList.addAll(wrapper.getQuizList());

		} catch (JAXBException e) {
			System.err.println("File not valid");
		} catch (URISyntaxException e) {
			System.err.println("Folder not found");
		}
		return quizList;
	}

	public static void saveQuizDataToFile(List<Quiz> quizList) {
		JAXBContext context;
		try {
			File file = new File(getFolder(), "quiz.xml");
			context = JAXBContext.newInstance(QuizListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			QuizListWrapper wrapper = new QuizListWrapper();
			wrapper.setQuizList(quizList);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.err.println("Folder not found");
		}
	}
}
