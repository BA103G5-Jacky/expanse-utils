package com.jkey.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
  public static void countLotteryNumberOccurrences(List<List<Integer>> historyNumbersList, Integer numForStat) {
    // Limit historyNumbersList to the first 100 sets
    List<List<Integer>> limitedHistoryNumbersList = historyNumbersList.subList(0, Math.min(numForStat, historyNumbersList.size()));

    Map<Integer, Integer> numberOccurrences = new HashMap<>();

    // Count occurrences
    for (List<Integer> numbers : limitedHistoryNumbersList) {
      for (Integer number : numbers) {
        numberOccurrences.put(number, numberOccurrences.getOrDefault(number, 0) + 1);
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
    System.out.println("Lottery Number Occurrences:");
    for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
      System.out.println("Number " + entry.getKey() + ": " + entry.getValue() + " times" + " 開出機率: " + (entry.getValue() / (double) limitedHistoryNumbersList.size()));
    }
  }
}
