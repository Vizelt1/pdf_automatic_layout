package fr.rappa.utils;

public class TextUtil {

	public static String replaceSpecialChar(String str) {
		String newStr = str;
		newStr.replace("�", "\\u233");
		return newStr;
	}
	
}
