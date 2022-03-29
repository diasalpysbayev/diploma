package kz.iitu.diploma.inservice.search_engine.bing.impl;

import kz.iitu.diploma.inservice.search_engine.bing.BingSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.model.search_engine.QueryResult;
import lombok.SneakyThrows;

public class BingSearchServiceFake implements BingSearchService {

  @SneakyThrows
  @Override
  public QueryResult search(String query) {
    return null;
  }

}
