package fr.rappa;

public class Log {
	
	public static void info(String message) {
		System.out.println("[INFO] : " + message);
	}
	
	public static void err(String message) {
		System.out.println("[ERROR] : " + message);
	}
	
	public static void warn(String message) {
		System.out.println("[WARNING] : " + message);
	}
	
	public static void info(String message, int line) {
		System.out.println("[INFO] << Line " + line + " >> : " + message);
	}
	
	public static void warn(String message, int line) {
		System.out.println("[WARNING] << Line " + line + " >> : " + message);
	}
	
	public static void err(String message, int line) {
		System.out.println("[ERROR] << Line " + line + " >> : " + message);
	}
}
