package com.jkey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FiveThreeNineLottery {
  // 讀取 txt 位置
  // [!!注意!!] 記得更新歷史訊息
  private static final String NUMBERS_PATH = "C:\\Users\\jacky.chiu\\Documents\\proj\\expanse-utils\\src\\main\\resources\\539_history_numbers.txt"; // 539 歷史開獎號碼
  private static int SET_SIZE = 5; // 一組號碼有幾個
  private static final int REF_ISSUE_NUM = 10; // 要參考之前幾組開獎號碼
  private static final int FIVETHREENINE_NUM = 39; // 有幾顆彩球
  private static int LOTTERY_SET_NUM = 4; // 想要產生出的組數

  public static void genLotteryNumbersSets() {

    // 變數初始化設置
    List<List<Integer>> historyNumbersList = new ArrayList<>();

    // [START] 讀取資料 -----------------------------------------------------------------------------
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(NUMBERS_PATH), StandardCharsets.UTF_8))) {
      // 變數初始化設置
      String line;

      // 逐行讀取文字
      while ((line = reader.readLine()) != null) {
        // 透過 ":" 分割文字
        String[] parts = line.split(":");
        Long issueNum = Long.parseLong(parts[0].trim()); // 期數
        LocalDate date = convertStringToDate(parts[1].trim()); // 日期
        // 數字裝載進入 List 中
        List<Integer> numbers = new ArrayList<>();
        String[] numbersStrArr = parts[2].split(",");
        for (String numberStr : numbersStrArr) {
          numbers.add(Integer.parseInt(numberStr.trim()));
        }
        // 裝載進入 Map 中
        historyNumbersList.add(numbers);
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }
    // === [END]

    // [START] 產生號碼組合 -----------------------------------------------------------------------------

    // 拿前 ? 筆資料，distinct 有哪一些號碼
    List<Integer> distinctNumbers = new ArrayList<>();
    for (int i = 0; i < REF_ISSUE_NUM; i++) {
      List<Integer> numbers = historyNumbersList.get(i);
      for (Integer number : numbers) {
        if (!distinctNumbers.contains(number)) {
          distinctNumbers.add(number);
        }
      }
    }

    // 排序已開過獎的號碼
    distinctNumbers.sort(Integer::compareTo);

    // 取得 1 ~ 39 的數字，但不包含前十組開獎的數字
    List<Integer> nonHitNumbers = new ArrayList<>();
    for (int i = 1; i <= FIVETHREENINE_NUM; i++) {
      if (!distinctNumbers.contains(i)) {
        nonHitNumbers.add(i);
      }
    }

    List<List<Integer>> lotteryNumbersSets = new ArrayList<>();

    for (int i = 0; i < LOTTERY_SET_NUM; i++) {
      // 再 distinctNumbers 隨機取兩個數字，nonHitNumbers 隨機取三個數字
      List<Integer> lotteryNumbers = new ArrayList<>();
      for (int j = 0; j < SET_SIZE; j++) {
        if (j < 2) {
          int randomIndex = (int) (Math.random() * nonHitNumbers.size());
          if (lotteryNumbers.contains(nonHitNumbers.get(randomIndex))) {
            j--;
            continue;
          }
          lotteryNumbers.add(nonHitNumbers.get(randomIndex));
        } else {
          int randomIndex = (int) (Math.random() * distinctNumbers.size());
          if (lotteryNumbers.contains(distinctNumbers.get(randomIndex))) {
            j--;
            continue;
          }
          lotteryNumbers.add(distinctNumbers.get(randomIndex));
        }
      }
      lotteryNumbers.sort(Integer::compareTo);

      System.out.println((i+1) + ": " + lotteryNumbers);

      lotteryNumbersSets.add(lotteryNumbers);
    }

// 把結果寫到檔案
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream("C:\\Users\\jacky.chiu\\Documents\\proj\\expanse-utils\\src\\main\\resources\\539_lottery_numbers_result.txt"), StandardCharsets.UTF_8))) {
      for (int i = 0; i < LOTTERY_SET_NUM; i++) {
        writer.write((i+1) + ": " + lotteryNumbersSets.get(i));
        writer.newLine();
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }

  }

  public static LocalDate convertStringToDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateString, formatter);
  }
}
