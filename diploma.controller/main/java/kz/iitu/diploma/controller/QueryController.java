package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.analytics.AnalyticsRecord;
import kz.iitu.diploma.model.query.QueryHistory;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

  @Autowired
  private QueryRegister queryRegister;

  @PostMapping("/execute-query")
  public List<SearchInformation> executeQuery(@RequestBody QueryRecord queryRecord) {
    return queryRegister.executeQuery(queryRecord);
  }

  @PostMapping("/analitics")
  public AnalyticsRecord getAnalytics(@Param(value = "queryId") Long queryId) {
    return queryRegister.analyzeQuery(queryId);
  }

  @GetMapping("/query-history")
  public List<QueryHistory> getQueryHistory(@Param(value = "queryId") Long queryId) {
    return queryRegister.getQueryHistory(queryId);
  }

}
