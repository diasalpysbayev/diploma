package kz.iitu.diploma.inservice.search_engine.yandex;

import kz.iitu.diploma.model.search_engine.QueryResult;

public interface YandexSearchService {

  QueryResult search(String query);

}
