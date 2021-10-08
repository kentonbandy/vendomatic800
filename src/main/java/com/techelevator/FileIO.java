package com.techelevator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIO {
    private static File readFile = new File("vendingmachine.csv");
    private static File logFile = new File("Log.txt");
    private static File salesReportDir = new File("src/main/resources/");

    private static List<String[]> csvLines = new ArrayList<>();


    public static void load() {
        try (Scanner dataInput = new Scanner(readFile)) {
            while (dataInput.hasNextLine()) {
                csvLines.add(dataInput.nextLine().split("\\|"));
            }
        } catch (FileNotFoundException e) {
            Printer.println("Source file not found!");
        }
    }

    public static List<String[]> getCsvLines() {
        return csvLines;
    }

    public static void appendLog(String action, double leftAmount, double rightAmount) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
            writer.println(dateTime.format(format) + " " + action + " $" + leftAmount + " " + rightAmount);
        } catch (FileNotFoundException e) {
            Printer.println("Couldn't write to log file!");
        }
    }

}
