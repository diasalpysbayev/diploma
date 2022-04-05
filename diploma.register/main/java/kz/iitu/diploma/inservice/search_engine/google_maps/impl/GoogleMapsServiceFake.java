package kz.iitu.diploma.inservice.search_engine.google_maps.impl;

import kz.iitu.diploma.inservice.search_engine.google_maps.GoogleMapsService;
import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.search_engine.QueryResult;
import lombok.SneakyThrows;

public class GoogleMapsServiceFake implements GoogleMapsService {

  @SneakyThrows
  @Override
  public QueryResult search(QueryDetail query) {
    return null;
  }

}
