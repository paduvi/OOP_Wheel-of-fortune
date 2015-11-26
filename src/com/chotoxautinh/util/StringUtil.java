package com.chotoxautinh.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.chotoxautinh.model.Block;

public class StringUtil {
	
	private static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
	}

	private static String removeSpecialChar(String s) {
		if (s == null || s.isEmpty())
			return s;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isLetterOrDigit(s.charAt(i)) || Character.isWhitespace(s.charAt(i)) || s.charAt(i) == '.') {
				builder.append(s.charAt(i));
			}
		}
		return builder.toString();
	}
	
	private static String removeDuplicateWhiteSpace(String s){
		return s.trim().replaceAll(" +", " ");
	}

	public static List<Block> toBlockList(String s){
		String temp = removeDuplicateWhiteSpace(removeSpecialChar(unAccent(s))).toUpperCase();
		List<Block> blockList = new ArrayList<>();
		for (int i = 0; i < temp.length(); i++) {
			blockList.add(new Block(temp.charAt(i)));
		}
		return blockList;
	}
	
	public static void main(String[] args) {
		System.out.println(removeDuplicateWhiteSpace(removeSpecialChar(unAccent("....     ."))));
	}
}
