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
      data[i][3] = ExpanseDetailVO.getTypeByNameMatching(detail.getItem());
      data[i][4] = detail.getWhoPay();
    }
    return data;
  }

  public static String getTypeByNameMatching(String item) {

    if(item.contains("薪") || item.contains("獎金") || item.contains("利息") || item.contains("股利") || item.contains("津貼")){
      return "(此筆為收入紀錄)";
    }
    else if(item.contains("早餐") || item.contains("午餐") || item.contains("晚餐")){
      return "生活開銷";
    }
//    else if(item.contains("")){
//      return "生活用品";
//    }
//    else if(item.contains("")){
//      return "社交";
//    }
    else if(item.contains("計程車") || item.contains("火車") || item.contains("高鐵") || item.contains("悠遊卡加值")){
      return "交通";
    }
    else if(item.contains("衣") || item.contains("鞋")){
      return "服裝";
    }
    else if(item.contains("票") || item.contains("電影") || item.contains("演唱會") || item.contains("娛樂") || item.contains("spotify") || item.contains("netflix")){
      return "娛樂";
    }
    else if(item.contains("所得稅")){
      return "稅金";
    }
//    else if(item.contains("")){
//      return "現金儲蓄";
//    }
    else if(item.contains("紅包") || item.contains("請客") || item.contains("禮金")){
      return "特別費";
    }
    else if(item.contains("書") || item.contains("課程") || item.contains("訂閱")){
      return "技能投資";
    }
//    else if(item.contains("")){
//      return "理財投資";
//    }
    else if(item.contains("掛號")|| item.contains("眼科") || item.contains("看診")){
      return "醫療保健運動";
    }
//    else if(item.contains("")){
//      return "旅遊";
//    }
//    else if(item.contains("")){
//      return "線上服務";
//    }
    else if(item.contains("奉獻")){
      return "奉獻";
    }
//    else if(item.contains("")){
//      return "婚禮籌備";
//    }
    else if(item.contains("保養品") || item.contains("眉筆")){
      return "保養化妝品";
    }
//    else if(item.contains("")){
//      return "自習場地費";
//    }
    else if(item.contains("房租")){
      return "房租";
    }
    else if(item.contains("回台南")){
      return "返鄉交通";
    }
    else if(item.contains("機油") || item.contains("齒輪油")){
      return "機車養護";
    }
    else if(item.contains("0050") || item.contains("0056")){
      return "存股";
    }
    else if(item.contains("剪頭髮") || item.contains("剪髮")){
      return "剪髮";
    }
//    else if(item.contains("")){
//      return "儲蓄險";
//    }
    else if(item.contains("保險")){
      return "醫療壽產險";
    }
    else if(item.contains("機車保險")){
      return "汽機車產險";
    }
    else if(item.contains("停車") && !item.contains("機車停車")){
      return "汽車停車費";
    }
    else if(item.contains("汽車保養")){
      return "汽車養護";
    }
    else if(item.contains("加油")){
      return "汽車加油";
    }
    else if(item.contains("牌照稅") || item.contains("燃料稅")){
      return "汽車稅金";
    }
//    else if(item.contains("")){
//      return "公宅裝潢費";
//    }
    else if(item.contains("產檢")){
      return "孕婦費用";
    }
    else if(item.contains("天樂") || item.contains("奶粉") || item.contains("寶寶") || item.contains("奶瓶") || item.contains("尿布")){
      return "育兒相關";
    }
    else if(item.contains("公帳")){
      return "匯入公帳費";
    }
    else {
      return "生活開銷";
    }

  }
}
