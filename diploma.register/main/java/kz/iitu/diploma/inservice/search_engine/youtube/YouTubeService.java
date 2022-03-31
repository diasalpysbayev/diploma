package kz.iitu.diploma.inservice.search_engine.youtube;

import kz.iitu.diploma.model.search_engine.QueryResult;

public interface YouTubeService {

  QueryResult search(String query);

}
