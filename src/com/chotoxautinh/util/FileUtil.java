package com.chotoxautinh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.chotoxautinh.model.Quiz;

public class FileUtil {
	public static String readFile(File file) {
		try {
			FileReader reader = new FileReader(file);
			char[] cbuf = new char[4 * 1024];
			int read = -1;
			StringBuilder builder = new StringBuilder();
			while ((read = reader.read(cbuf)) != -1) {
				builder.append(new String(cbuf, 0, read));
			}
			reader.close();
			return builder.toString();
		} catch (IOException e) {
			return null;
		}
	}

	public static void importQuiz(File file) {
		InputStreamReader is;
		try {
			is = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(is);
			br.readLine();

			List<Quiz> quizData = XmlUtil.loadQuizDataFromFile();

			String temp = "";
			int i = 0;
			while ((temp = br.readLine()) != null) {
				quizData.add(new Quiz(temp.split("\t")[1], temp.split("\t")[2]));
				i++;
			}

			XmlUtil.saveQuizDataToFile(quizData);
			br.close();
			System.out.println("Import " + i + " quiz successfully");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		File file = new File("MillionaireDefaultUS.txt");
		importQuiz(file);
	}
}
