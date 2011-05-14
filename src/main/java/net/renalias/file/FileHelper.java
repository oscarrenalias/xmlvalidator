package net.renalias.file;

import java.io.*;
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

	public static boolean isWritable(String fileName) {
		return((new File(fileName)).canWrite());
	}

	public static boolean exists(String fileName) {
		return((new File(fileName)).exists());
	}

	public static void write(String absolutePath, String text) throws IOException {
		FileWriter fstream = new FileWriter(absolutePath);
        BufferedWriter out = new BufferedWriter(fstream);
    	out.write(text);
    	out.close();
	}
}
