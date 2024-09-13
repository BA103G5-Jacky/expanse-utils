package com.jkey;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class ExpanseDetailVO {

  String date;
  String item;
  String expanse;
  String whoPay;

  public static String[][] convertTo2DArray(List<ExpanseDetailVO> details) {
    String[][] data = new String[details.size()][5];
    for (int i = 0; i < details.size(); i++) {
      ExpanseDetailVO detail = details.get(i);
      data[i][0] = detail.getDate().split("\\s+")[0];
      data[i][1] = detail.getItem();
      data[i][2] = detail.getExpanse();
      data[i][3] = "生活開銷";
      data[i][4] = detail.getWhoPay();
    }
    return data;
  }
}
