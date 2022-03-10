package kz.iitu.diploma.impl;

import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import kz.iitu.diploma.register.QueryRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryRegisterImpl implements QueryRegister {

  @Autowired
  private GoogleSearchService     googleSearchService;
  @Autowired
  private YandexSearchService     yandexSearchService;
  @Autowired
  private DuckDuckGoSearchService duckDuckGoSearchService;

  @Override
  public void executeQuery(QueryRecord queryRecord) {

    int partitionSize = 3;

    List<List<String>> dividedQuery = new LinkedList<>();
    for (int i = 0; i < queryRecord.queryList.size(); i += partitionSize) {
      dividedQuery.add(queryRecord.queryList.subList(i,
          Math.min(i + partitionSize, queryRecord.queryList.size())));
    }

    int i = -1;

    GoogleResult googleResult     = new GoogleResult();
    List<String> yandexResult     = new ArrayList<>();
    List<String> duckDuckGoResult = new ArrayList<>();

    for (List<String> queryList : dividedQuery) {
      i++;
      for (String query : queryList) {
        if (i == 0) {
          googleResult = googleSearchService.search(query);
        } else if (i == 1) {
          yandexResult = yandexSearchService.search(query);
        } else {
          duckDuckGoResult = duckDuckGoSearchService.search(query);
        }
      }
    }

    System.out.println("YES");

  }
}
