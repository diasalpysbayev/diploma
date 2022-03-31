package kz.iitu.diploma.inservice.search_engine.youtube.impl;

import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.youtube.YouTubeService;
import kz.iitu.diploma.model.search_engine.QueryResult;
import lombok.SneakyThrows;

public class YouTubeServiceFake implements YouTubeService {

  @SneakyThrows
  @Override
  public QueryResult search(String query) {
    return null;
  }

}
