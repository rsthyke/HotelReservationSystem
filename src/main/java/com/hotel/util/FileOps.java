package com.hotel.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOps {
    
    public static void writeFile(String filename, ArrayList<String> lines) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (String line : lines) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
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
