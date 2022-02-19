package kz.iitu.diploma.inservice.search_engine.google.impl;

import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import lombok.SneakyThrows;

public class GoogleSearchServiceFake implements GoogleSearchService {

  @SneakyThrows
  @Override
  public GoogleResult search(String query) {
    return null;
  }

}
