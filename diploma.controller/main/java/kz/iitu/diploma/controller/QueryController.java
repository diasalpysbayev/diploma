package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public void getAnalytics() {

  }

}
