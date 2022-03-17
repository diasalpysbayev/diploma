package kz.iitu.diploma.inservice.search_engine.yandex;

import kz.iitu.diploma.model.search_engine.GoogleResult;

public interface YandexSearchService {

  GoogleResult search(String query);

}
