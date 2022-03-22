package kz.iitu.diploma.inservice.search_engine.duckduckgo;

import kz.iitu.diploma.model.search_engine.QueryResult;

public interface DuckDuckGoSearchService {

  QueryResult search(String query);

}
