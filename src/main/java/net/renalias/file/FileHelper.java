package net.renalias.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class FileHelper {

	public static String readFile(String fileName, String encoding) throws IOException {
		Scanner scanner = new Scanner(new FileInputStream(fileName), "UTF-8");
		StringBuilder contents = new StringBuilder();
		while(scanner.hasNextLine()) {
			contents.append(scanner.nextLine() + "\n");
		}

        scanner.close();
		return(contents.toString());
	}

    public static String readFile(String fileName) throws IOException {
		return(FileHelper.readFile(fileName, "UTF-8"));
	}

	public static boolean isReadable(String fileName) {
		return((new File(fileName)).canRead());
	}

	public static boolean isFile(String fileName) {
		return((new File(fileName)).isFile());
	}
}
