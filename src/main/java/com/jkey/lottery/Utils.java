package com.jkey.lottery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
  public static void countLotteryNumberOccurrences(List<List<Integer>> historyNumbersList, Integer numForStat, Integer totalNum) {
    // Limit historyNumbersList to the first numForStat sets
    List<List<Integer>> limitedHistoryNumbersList = historyNumbersList.subList(0, Math.min(numForStat, historyNumbersList.size()));

    Map<Integer, Integer> numberOccurrences = new HashMap<>();

    List<Integer> hitNumbers = new ArrayList<>();

    // Count occurrences
    for (List<Integer> numbers : limitedHistoryNumbersList) {
      for (Integer number : numbers) {
        numberOccurrences.put(number, numberOccurrences.getOrDefault(number, 0) + 1);
        if (!hitNumbers.contains(number)) {
          hitNumbers.add(number);
        }
      }
    }


    // Sort the map by value in descending order
    // 1. 提取 EntrySet 並轉換為 List
    List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(numberOccurrences.entrySet());

    // 2. 排序，按值降序
    entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

    // 3. 轉換為 LinkedHashMap 保留排序結果
    Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<Integer, Integer> entry : entryList) {
      sortedMap.put(entry.getKey(), entry.getValue());

    }

    // Print results
    System.out.println("Lottery Number Occurrences(前 " + Math.min(numForStat, limitedHistoryNumbersList.size()) + " 組):");
    for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue() + " 次" + ", 開出機率: " + new BigDecimal(entry.getValue() /
                                                                                                        (double) limitedHistoryNumbersList.size()).setScale(3, RoundingMode.HALF_UP).doubleValue());

    }

    // print 0 次的號碼
    for(int i = 0; i < totalNum; i++) {
      if (!hitNumbers.contains(i + 1)) {
        System.out.println((i + 1) + ": 0 次" + ", 開出機率: 0.0");
      }
    }
  }
}
