package kz.iitu.diploma.inservice.search_engine.google;

import kz.iitu.diploma.model.search_engine.GoogleResult;

public interface GoogleSearchService {

  GoogleResult search(String query);

}
