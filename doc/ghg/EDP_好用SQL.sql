-- 利用 report id 查詢 report_form 表格，並且同時 join topic, topic_dropdown, unit 表格
SELECT 
    -- m.report_ref, 
    m.id, 
    m.topic_id, 
    t1."des_TC" AS question, 
    t2."name" as 下拉選單,
    -- m.answer_type_ref,
    m.value,
    t3."name" as 單位,
    m.value_text
FROM 
    dbt_dsp.csrhr_report_form m
LEFT JOIN 
    dbt_dsp.csrhr_topic t1 
    ON m.topic_id = t1.id 
LEFT JOIN 
    dbt_dsp.csrhr_topic_dropdown t2 
    ON m.value_text = cast(t2.id as text)
LEFT JOIN 
    dbt_dsp.csrhr_unit t3
    ON m.unit_id = t3.id
WHERE 
    m.report_ref = 14357
ORDER BY 
    m.id;