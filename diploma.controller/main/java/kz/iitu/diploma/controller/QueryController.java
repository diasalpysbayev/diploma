package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.analytics.AnalyticsRecord;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

}
