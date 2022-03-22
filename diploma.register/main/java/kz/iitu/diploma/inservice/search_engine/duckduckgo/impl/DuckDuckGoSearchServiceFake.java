package kz.iitu.diploma.inservice.search_engine.duckduckgo.impl;

import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.model.search_engine.QueryResult;
import lombok.SneakyThrows;

public class DuckDuckGoSearchServiceFake implements DuckDuckGoSearchService {

  @SneakyThrows
  @Override
  public QueryResult search(String query) {
    return null;
  }

}
