package com.hotel.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

//A utility class to help with reading and writing files.
//This class handles the low-level file operations.
public class FileOps {
    /**
     * Writes a list of strings to a file.
     * Each string in the list becomes a new line in the file.
     * * @param filename The name/path of the file.
     * @param lines The data to write.
     */
    public static void writeFile(String filename, ArrayList<String> lines) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (String line : lines) {
                writer.write(line + "\n");
            }
            writer.close();// Always close the file to save changes
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Reads a file and returns the content as a list of strings.
     * * @param filename The name/path of the file to read.
     * @return An ArrayList containing lines of the file.
     */
    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            File file = new File(filename);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }
}
