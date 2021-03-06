package com.techelevator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileIO {
    private static final File readFile = new File("vendingmachine.csv");
    private static final File logFile = new File("Log.txt");
    private static final File persistentSalesReportFile = new File("src/main/resources/salesreport.txt");
    private static final String salesReportDir = "src/main/sales_reports/";
    private static List<String[]> csvLines = new ArrayList<>();

    /**
     * creates a List of String arrays as a structured data version of the csv database file to be used for populating Sales Report and Inventory
     */
    public static void loadCsv() {
        csvLines = new ArrayList<>();
        try (Scanner dataInput = new Scanner(readFile)) {
            while (dataInput.hasNextLine()) {
                csvLines.add(dataInput.nextLine().split("\\|"));
            }
        } catch (FileNotFoundException e) {
            Printer.println("Source file not found!");
        }
    }

    /**
     * @return a Map of item names as keys and total quantity sold as values
     */
    public static Map<String,Integer> loadSalesReport() {
        Map<String,Integer> salesReportMap = new HashMap<>();
        try (Scanner reader = new Scanner(persistentSalesReportFile)) {
            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split("\\|");
                if (line.length < 2) continue;
                try {
                    int quantity = Integer.parseInt(line[1]);
                    salesReportMap.put(line[0], quantity);
                } catch (NumberFormatException ignored) {}
            }
        } catch (FileNotFoundException e) {
            return salesReportMap;
        }
        return salesReportMap;
    }

    public static String loadSalesReportTotal() {
        try (Scanner fileData = new Scanner(persistentSalesReportFile)) {
            while (fileData.hasNextLine()) {
                if (fileData.nextLine().equals("")) break;
            }
            return fileData.nextLine();
        } catch (FileNotFoundException e) {
            return "0.00";
        }
    }

    public static boolean writeSalesReport(boolean isPersistent) {
        File file;
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy_HH-mm-ss");
        if (isPersistent) file = persistentSalesReportFile;
        else file = new File(salesReportDir + "SR_" + dateTime.format(format) + ".csv");
        File dir = new File(salesReportDir);
        if (!dir.exists()) dir.mkdir();
        try (PrintWriter writer = new PrintWriter(file)) {
            Map<String,Integer> map = SalesReport.getRunningSalesMap();
            for (String key : map.keySet()) {
                writer.println(key + "|" + map.get(key));
            }
            writer.println();
            writer.println(SalesReport.getTotalSales());
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * @param action keyword String for the action being taken
     * @param leftAmount first monetary value
     * @param rightAmount second monetary value
     */
    public static void appendLog(String action, String leftAmount, String rightAmount) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
            writer.println(dateTime.format(format) + " " + action + " $" + leftAmount + " $" + rightAmount);
        } catch (FileNotFoundException e) {
            Printer.println("Couldn't write to log file!");
        }
    }

    public static List<String[]> getCsvLines() {
        return csvLines;
    }
}
