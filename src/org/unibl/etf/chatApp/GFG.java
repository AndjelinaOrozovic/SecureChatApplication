package org.unibl.etf.chatApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GFG {

	public static boolean onlyDigits(String str) {

		String regex = "[0-9]+";
		Pattern p = Pattern.compile(regex);

		if (str == null) {
			return false;
		}
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean onlyCharacters(String str) {
		String regex = "[A-Za-z]+";
		Pattern p = Pattern.compile(regex);

		if (str == null) {
			return false;
		}

		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isSql_inj(String str) {
		String test = str.toLowerCase();
		String data = "' ` and \" from where create exec insert select delete table set update drop count * % chr mid master truncate char declare ; or - + , = # /";
		String[] split = data.split(" ");
		for (int i = 0; i < split.length; i++) {
			if (test.indexOf(split[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isXSS_attack(String str) {
		String test = str.toLowerCase();
		String data = "<script </script> <% %> eval function alert";
		String[] split = data.split(" ");
		for (int i = 0; i < split.length; i++) {
			if (test.indexOf(split[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static void writeToFile(String log) {
		try {
			String path = "C:\\Users\\andje\\eclipse-workspace\\ChatApplication\\logs.txt";
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String text = dtf.format(now) + " --> " + log + "\n";
			File logs = new File(path);
			if (logs.exists()) {
				Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
			} else {
				logs.createNewFile();
				Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
			}
			System.out.println("Successfully writen!");
		} catch (IOException e) {
			System.out.println("An error occured!");
			e.printStackTrace();
		}
	}

}
