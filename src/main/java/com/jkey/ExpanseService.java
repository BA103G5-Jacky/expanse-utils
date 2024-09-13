package com.jkey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExpanseService {
  // 讀取 txt 位置
  private static final String INPUT_PATH = "./src/main/resources/input.txt";

  public static void reformatAndExportToExcel() {

    // 參數
    List<String> listStr = new ArrayList<String>();
    List<ExpanseDetailVO> family = new ArrayList<ExpanseDetailVO>();
    List<ExpanseDetailVO> emily = new ArrayList<ExpanseDetailVO>();
    List<ExpanseDetailVO> jacky = new ArrayList<ExpanseDetailVO>();

    int role = -1; // 角色 0: 家庭公帳, 1: Emily, 2: Jacky

    String dateTemp = null;

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(INPUT_PATH), "UTF-8"))) {
      String line;

      while ((line = reader.readLine()) != null) {

        if (line.matches("\\*\\s\\d{1,2}/\\d{1,2}/\\d{4}\\s+\\d+")) {
          dateTemp = line.trim().substring(1).trim();
          listStr.add(dateTemp); // <---------------------------------------------------
        } else if (line.contains("* 無") || line.contains("* 家庭公帳") || line.contains("* Emily")
            || line.contains("* Jacky")) {
        } else {
          String[] parts = line.split("\\s+");
          switch (role) {
            case 0:
              family.add(new ExpanseDetailVO(dateTemp, parts[2], parts[3],
                  parts.length > 4 ? StringUtils.capitalize(parts[4]) : null));
              break;
            case 1:
              emily.add(new ExpanseDetailVO(dateTemp, parts[2], parts[3],
                  parts.length > 4 ? StringUtils.capitalize(parts[4]) : null));
              break;
            case 2:
              jacky.add(new ExpanseDetailVO(dateTemp, parts[2], parts[3],
                  parts.length > 4 ? StringUtils.capitalize(parts[4]) : null));
              break;
            default:
              break;
          }
        }

        if (StringUtils.contains(line, "* 家庭公帳")) {
          role = 0;
        }

        if (StringUtils.contains(line, "* Emily")) {
          role = 1;
        }

        if (StringUtils.contains(line, "* Jacky")) {
          role = 2;
        }

      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }

    // 寫出 txt
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream("./src/main/resources/output.txt"), "UTF-8"))) {
      writer.write("家庭公帳");
      writer.newLine();

      for (ExpanseDetailVO detail : family) {

        String whoPay = detail.getWhoPay() != null ? detail.getWhoPay() : "";
        writer.write(
            detail.getDate().split("\\s+")[0] + " " + detail.getItem() + " " + detail.getExpanse()
                + " " + whoPay);
        writer.newLine();
      }

      writer.write("Emily");
      writer.newLine();

      for (ExpanseDetailVO detail : emily) {

        String whoPay = detail.getWhoPay() != null ? detail.getWhoPay() : "";
        writer.write(
            detail.getDate().split("\\s+")[0] + " " + detail.getItem() + " " + detail.getExpanse()
                + " " + whoPay);
        writer.newLine();
      }

      writer.write("Jacky");
      writer.newLine();

      for (ExpanseDetailVO detail : jacky) {

        String whoPay = detail.getWhoPay() != null ? detail.getWhoPay() : "";
        writer.write(
            detail.getDate().split("\\s+")[0] + " " + detail.getItem() + " " + detail.getExpanse()
                + " " + whoPay);
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }

    Workbook workbook = new XSSFWorkbook();
    Sheet familySheet = workbook.createSheet("family");

    String[][] familyData = ExpanseDetailVO.convertTo2DArray(family);

    for (int i = 0; i < familyData.length; i++) {
      Row row = familySheet.createRow(i);
      for (int j = 0; j < familyData[i].length; j++) {
        Cell cell = row.createCell(j);
        cell.setCellValue(familyData[i][j]);
      }
    }

    Sheet emilySheet = workbook.createSheet("emily");

    String[][] emiliyData = ExpanseDetailVO.convertTo2DArray(emily);

    for (int i = 0; i < emiliyData.length; i++) {
      Row row = emilySheet.createRow(i);
      for (int j = 0; j < emiliyData[i].length; j++) {
        Cell cell = row.createCell(j);
        cell.setCellValue(emiliyData[i][j]);
      }
    }

    Sheet jackySheet = workbook.createSheet("jacky");

    String[][] jackyData = ExpanseDetailVO.convertTo2DArray(jacky);

    for (int i = 0; i < jackyData.length; i++) {
      Row row = jackySheet.createRow(i);
      for (int j = 0; j < jackyData[i].length; j++) {
        Cell cell = row.createCell(j);
        cell.setCellValue(jackyData[i][j]);
      }
    }

    try (FileOutputStream fileOut = new FileOutputStream("./src/main/resources/output.xlsx")) {
      workbook.write(fileOut);
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }

    System.out.println("Convert to Excel Done!");
  }
}
