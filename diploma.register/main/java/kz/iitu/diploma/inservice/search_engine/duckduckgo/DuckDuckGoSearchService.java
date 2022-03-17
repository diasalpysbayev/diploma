package kz.iitu.diploma.inservice.search_engine.duckduckgo;

import kz.iitu.diploma.model.search_engine.GoogleResult;

public interface DuckDuckGoSearchService {

  GoogleResult search(String query);

}
