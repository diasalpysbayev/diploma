package kz.iitu.diploma.inservice.search_engine.google.impl;

import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import lombok.SneakyThrows;

import java.util.List;

public class GoogleSearchServiceFake implements GoogleSearchService {

  @SneakyThrows
  @Override
  public List<String> search(String query) {
    return null;
  }

}
