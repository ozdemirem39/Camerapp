package com.yalcay.camerapp;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportUtils {

    public static void saveToExcel(String fileName, int[] rgb, float[] hsv) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Color Data");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Red");
        header.createCell(1).setCellValue("Green");
        header.createCell(2).setCellValue("Blue");
        header.createCell(3).setCellValue("Hue");
        header.createCell(4).setCellValue("Saturation");
        header.createCell(5).setCellValue("Value");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(rgb[0]);
        dataRow.createCell(1).setCellValue(rgb[1]);
        dataRow.createCell(2).setCellValue(rgb[2]);
        dataRow.createCell(3).setCellValue(hsv[0]);
        dataRow.createCell(4).setCellValue(hsv[1]);
        dataRow.createCell(5).setCellValue(hsv[2]);

        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}