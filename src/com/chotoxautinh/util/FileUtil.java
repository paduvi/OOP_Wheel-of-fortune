package com.chotoxautinh.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
	public static String readFile(File file) {
		try {
			FileReader reader;
			reader = new FileReader(file);
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

	public static void main(String[] args) {
		String admin = readFile(new File("resources/user/admin.properties"));
		System.out.println("User: " + admin.split("\n")[0]);
		System.out.println("Password: " + admin.split("\n")[1]);
	}
}
