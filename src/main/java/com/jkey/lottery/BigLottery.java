package com.jkey.lottery;

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

public class BigLottery {
  // 讀取 txt 位置
  // [!!注意!!] 記得更新歷史訊息
  private static final String NUMBERS_PATH = "src/main/resources/big_history_numbers.txt"; // 539 歷史開獎號碼
  private static final String NUMBERS_OUTPUT_PATH = "src/main/resources/big_lottery_numbers_result.txt"; // 539 歷史開獎號碼
  private static int SET_SIZE = 6; // 一組號碼有幾個
  private static final int REF_ISSUE_NUM = 10; // 要參考之前幾組開獎號碼
  private static final int FIVETHREENINE_NUM = 49; // 有幾顆彩球
  private static int LOTTERY_SET_NUM = 4; // 想要產生出的組數
  private static int NONE_HIT_NUM = 2; // 未開出的選擇數量
  private static int HISTORY_NUM_FOR_STAT = 100; // 要參考之前幾組開獎號碼的開出次數與機率組數


  public static void genLotteryNumbersSets() {
    System.out.println("大樂透獎號產生: ================================================================================");
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

    System.out.println();
    System.out.println("前 " + REF_ISSUE_NUM + " 期未開出的獎號: " + nonHitNumbers);
    System.out.println("前 " + REF_ISSUE_NUM + " 期有開出獎號的數量: " + nonHitNumbers.size());
    System.out.println("前 " + REF_ISSUE_NUM + " 期有開出的獎號: " + distinctNumbers);
    System.out.println("前 " + REF_ISSUE_NUM + " 期有開出獎號的數量: " + distinctNumbers.size());
    System.out.println("");
    System.out.println(
        "隨機產出 " + LOTTERY_SET_NUM + " 組號碼(未開出的隨機挑 " + NONE_HIT_NUM + " 個，已開出的隨機挑 "
            + (SET_SIZE - NONE_HIT_NUM) + " 個)");

    List<List<Integer>> lotteryNumbersSets = new ArrayList<>();

    for (int i = 0; i < LOTTERY_SET_NUM; i++) {
      // 再 distinctNumbers 隨機取兩個數字，nonHitNumbers 隨機取三個數字
      List<Integer> lotteryNumbers = new ArrayList<>();
      for (int j = 0; j < SET_SIZE; j++) {
        if (j < NONE_HIT_NUM) {
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

      System.out.println((i + 1) + ": " + lotteryNumbers);

      lotteryNumbersSets.add(lotteryNumbers);
    }

    List<Integer> lotteryNumbersDistinct = new ArrayList<>();

    for (List<Integer> lotteryNumberSet : lotteryNumbersSets) {
//      System.out.println(lotteryNumberSet);
      {
        // loop lotteryNumberSet
        for (Integer lotteryNumber : lotteryNumberSet) {
          if (!lotteryNumbersDistinct.contains(lotteryNumber)) {
            lotteryNumbersDistinct.add(lotteryNumber);
          }
        }
      }
    }
    lotteryNumbersDistinct.sort(Integer::compareTo);
    System.out.println("隨機選出樂透號碼數字" + lotteryNumbersDistinct);
    System.out.println("=============================================================================================");


// 把結果寫到檔案
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(
            NUMBERS_OUTPUT_PATH),
            StandardCharsets.UTF_8))) {
      for (int i = 0; i < LOTTERY_SET_NUM; i++) {
        writer.write((i + 1) + ": " + lotteryNumbersSets.get(i));
        writer.newLine();
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }

    // 計算歷史開獎號碼出現次數
    Utils.countLotteryNumberOccurrences(historyNumbersList, HISTORY_NUM_FOR_STAT);


  }

  public static LocalDate convertStringToDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateString, formatter);
  }
}
