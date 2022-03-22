package kz.iitu.diploma.inservice.search_engine.google;

import kz.iitu.diploma.model.search_engine.QueryResult;

public interface GoogleSearchService {

  QueryResult search(String query);

}
