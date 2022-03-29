package kz.iitu.diploma.inservice.search_engine.bing;

import kz.iitu.diploma.model.search_engine.QueryResult;

public interface BingSearchService {

  QueryResult search(String query);

}
