暫存 job step

```
//  // 建立 Job
//  @Bean
//  public Job importReportFormJob(JobRepository jobRepository, Step importReportFormStep) {
//    return new JobBuilder("importReportFormJob", jobRepository)
//        .incrementer(new RunIdIncrementer())
//        .start(importReportFormStep)
//        .build();
//  }
//
//
//  // 建立 Step
//  @Bean
//  public Step importReportFormStep(JobRepository jobRepository,
//      ItemWriter<ReportForm> writer) {
//    return new StepBuilder("importReportFormStep", jobRepository)
//        .<ReportForm, ReportReportFormGroup>chunk(100, rawDataTransactionManager)
//        .reader(itemReader())
//        .processor(reportFormGroupItemProcessor())
//        .writer(itemWriter())
//        .build();
//  }
```



暫存 writer

```
//  // 建立 itemWriter
//  @Bean
//  public ItemWriter<ReportReportFormGroup> itemWriter() {
//    return items -> {
//      for (ReportReportFormGroup item : items) {
//        System.out.println(item.toString());
//      }
//    };
//  }
```



暫存 reader-processor-writer

```
//  // ===============================================================================================
//  // 建立 問卷 reader
//  @Bean
//  public ItemReader<ReportForm> itemReader() {
//    return new JdbcCursorItemReaderBuilder<ReportForm>()
//        .name("reportFormItemReader")
//        .dataSource(rawDataDataSource)
//        .sql("SELECT * FROM dbt_dsp.csrhr_report_form order by report_ref")
//        .rowMapper(reportFormRowMapper())
//        .build();
//  }
//
//  // 建立 rowMapper，建立數據庫查詢結果與 ReportForm 之間的映射
//  @Bean
//  public ReportFormRowMapper reportFormRowMapper(){
//    return new ReportFormRowMapper();
//  }
//
//  // 建立 processor
//  @Bean
//  public ReportFormGroupProcessor reportFormGroupItemProcessor(){
//    return new ReportFormGroupProcessor();
//  }
//
//  // 建立 itemWriter
//  @Bean
//  public ItemWriter<ReportReportFormGroup> itemWriter() {
//    return items -> {
//      for (ReportReportFormGroup item : items) {
//        System.out.println(item.toString());
//      }
//    };
//  }
//  // ===============================================================================================
```



temp for step2 grouping data

```
// Step2 讀取上一個 step 的結果，
@Bean
@StepScope

// Step2 建立 問卷問題分群處理 processor(應該回一次處理一份問卷)
@Bean
public ItemProcessor<List<ReportForm>, List<ReportReportFormGroup>> groupQuestionsProcessor(){
  return new ItemProcessor<List<ReportForm>, List<ReportReportFormGroup>>() {
    @Override
    public List<ReportReportFormGroup> process(List<ReportForm> reportFormList) {
      List<ReportReportFormGroup> reportReportFormGroupList = new ArrayList<>();

      System.out.println(">>>groupQuestionsProcessor start<<<");
      // TODO-Jacky: 打印出這邊拿到的 reportFormList
      for (ReportForm dto : reportFormList) {
        System.out.println(dto);
      }

      // TODO-Jacky: 先 hard code 一下，之後要改成從資料庫讀取轉換
      // [在這裡實作分群邏輯]
      // 將每分問卷的題目進行分群，回傳分群結果
      reportReportFormGroupList.add(new ReportReportFormGroup().setReportFormId(1).setReportId(1).setReportFormIds("1,2,3"));
      reportReportFormGroupList.add(new ReportReportFormGroup().setReportFormId(1).setReportId(1).setReportFormIds("4,5,6,7"));
      reportReportFormGroupList.add(new ReportReportFormGroup().setReportFormId(1).setReportId(1).setReportFormIds("8,9,10"));

      System.out.println(">>>groupQuestionsProcessor end<<<");
      return reportReportFormGroupList;
    }
  };
}

// Step2 建立 分群結果 writer
@Bean
public ItemWriter<List<ReportReportFormGroup>> groupQuestionsWriter(){
  return new JdbcBatchItemWriterBuilder<List<ReportReportFormGroup>>()
      .dataSource(edpCustomDataSource)
      .sql("INSERT INTO report_report_form_group (report_id, question_set_id, report_form_ids) "
          + "VALUES (:reportId, :reportFormId, :reportFormIds)")
      .beanMapped()
      .build();
}
```



#### reportQuestionReadProcessor() 歷史紀錄

```
 // 建立 processor 根據問卷讀入相關問題，並進行題目分群
  @Bean
  public ItemProcessor<Report, List<EdpCustomMain>> reportQuestionReadProcessor(){
    // TODO-Jacky: >>>>>>>>>>>>> 改呼叫 Repository 方式呼叫，底下邏輯等待刪除
//    EntityManager entityManager = rawDataEntityManagerFactory.createEntityManager();
//    System.err.println(">>>>>>>>>entityManager = " + entityManager);

    return report -> {

      // TODO-Jacky: >>>>>>>>>>>>> 改呼叫 Repository 方式呼叫，底下邏輯等待刪除
//      // 根據問卷讀入相關問題
//      TypedQuery<ReportForm> query = entityManager
//              .createQuery("SELECT rf FROM ReportForm rf WHERE rf.reportRef = :reportId",
//                  ReportForm.class);
//      query.setParameter("reportId", report.getId());
//      List<ReportForm> reportFormList = query.getResultList(); // 取得問卷題目
      // TODO-Jacky: <<<<<<<<<<<<<<<<<
//      List<ReportForm> reportFormList = reportFormRepository.findByReportRef(report.getId()); // 由問卷 ID 取得問卷題目


      EdpTransferDto edpTransferDto = new EdpTransferDto(rawDataTransferService);
      edpTransferDto.setReportFormMap(rawDataTransferService.convertReportsToMap(List.of(report)));

      // 3. 資料轉至 edp_custom 格式

      return  rawDataTransferService.reportFormMapToEdpCustomMain(edpTransferDto); // return List<EdpCustomMain>

    };
```