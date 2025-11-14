/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group7.hotelease.Utils;

/**
 *
 * @author lapid
 */
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {
    private static final String DATA_PATH = "src/main/java/your/package/Data/";

    public static List<String[]> readCSV(String filename) {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(DATA_PATH + filename))) {
            data = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void writeCSV(String filename, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATA_PATH + filename))) {
            writer.writeAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendCSV(String filename, String[] data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATA_PATH + filename, true))) {
            writer.writeNext(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
