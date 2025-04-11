package com.yalcay.camerapp;

import android.graphics.Color;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PhotoProcessor {

    private static float[] calculateHSV(int r, int g, int b) {
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        return hsv;
    }

    private static double calculateAverage(int... values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum / (double) values.length;
    }

    public static void processAndWriteToXLSX(List<PhotoData> photoDataList, File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // RGB Sheet
        Sheet rgbSheet = workbook.createSheet("RGB Data");
        createRGBSheet(rgbSheet, photoDataList);

        // HSV Sheet
        Sheet hsvSheet = workbook.createSheet("HSV Data");
        createHSVS(hsvSheet, photoDataList);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    private static void createRGBSheet(Sheet sheet, List<PhotoData> photoDataList) {
        // Header
        Row header = sheet.createRow(0);
        String[] headers = {"File Name", "R1", "R2", "R3", "G1", "G2", "G3", "B1", "B2", "B3",
                "R Avg", "G Avg", "B Avg", "R+G", "R+B", "B+G", "R/G", "R/B", "G/B",
                "R/(G+B)", "G/(R+B)", "B/(R+G)", "R+G+B", "R-G", "R-B", "G-B",
                "R/(G-B)", "G/(R-B)", "B/(R-G)", "R-G-B", "G-R-B", "B-G-R",
                "R-G+B", "G-R+B", "B-G+R", "G-B+R"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // Data
        int rowIndex = 1;
        for (PhotoData data : photoDataList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(data.getFileName());
            row.createCell(1).setCellValue(data.getR1());
            row.createCell(2).setCellValue(data.getR2());
            row.createCell(3).setCellValue(data.getR3());
            row.createCell(4).setCellValue(data.getG1());
            row.createCell(5).setCellValue(data.getG2());
            row.createCell(6).setCellValue(data.getG3());
            row.createCell(7).setCellValue(data.getB1());
            row.createCell(8).setCellValue(data.getB2());
            row.createCell(9).setCellValue(data.getB3());

            double rAvg = calculateAverage(data.getR1(), data.getR2(), data.getR3());
            double gAvg = calculateAverage(data.getG1(), data.getG2(), data.getG3());
            double bAvg = calculateAverage(data.getB1(), data.getB2(), data.getB3());
            row.createCell(10).setCellValue(rAvg);
            row.createCell(11).setCellValue(gAvg);
            row.createCell(12).setCellValue(bAvg);

            // RGB Calculations
            row.createCell(13).setCellValue(rAvg + gAvg);
            row.createCell(14).setCellValue(rAvg + bAvg);
            row.createCell(15).setCellValue(bAvg + gAvg);
            // Add other calculations similarly...
        }
    }

    private static void createHSVS(Sheet sheet, List<PhotoData> photoDataList) {
        // Header
        Row header = sheet.createRow(0);
        String[] headers = {"File Name", "H1", "H2", "H3", "S1", "S2", "S3", "V1", "V2", "V3"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // Data
        int rowIndex = 1;
        for (PhotoData data : photoDataList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(data.getFileName());

            float[] hsv1 = calculateHSV(data.getR1(), data.getG1(), data.getB1());
            float[] hsv2 = calculateHSV(data.getR2(), data.getG2(), data.getB2());
            float[] hsv3 = calculateHSV(data.getR3(), data.getG3(), data.getB3());

            row.createCell(1).setCellValue(hsv1[0]);
            row.createCell(2).setCellValue(hsv1[1]);
            row.createCell(3).setCellValue(hsv1[2]);

            row.createCell(4).setCellValue(hsv2[0]);
            row.createCell(5).setCellValue(hsv2[1]);
            row.createCell(6).setCellValue(hsv2[2]);

            row.createCell(7).setCellValue(hsv3[0]);
            row.createCell(8).setCellValue(hsv3[1]);
            row.createCell(9).setCellValue(hsv3[2]);
        }
    }
}