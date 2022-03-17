package kz.iitu.diploma.inservice.search_engine.duckduckgo.impl;

import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import lombok.SneakyThrows;

import java.util.List;

public class DuckDuckGoSearchServiceFake implements DuckDuckGoSearchService {

  @SneakyThrows
  @Override
  public GoogleResult search(String query) {
    return null;
  }

}
