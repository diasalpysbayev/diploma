package kz.iitu.diploma.inservice.search_engine.google_maps;

import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.search_engine.QueryResult;

public interface GoogleMapsService {

  QueryResult search(QueryDetail query);

}
