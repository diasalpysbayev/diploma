package kz.iitu.diploma.register;

import kz.iitu.diploma.model.analytics.AnalyticsRecord;
import kz.iitu.diploma.model.query.QueryHistory;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;

import java.util.List;

public interface QueryRegister {

  List<SearchInformation> executeQuery(QueryRecord queryRecord);

  AnalyticsRecord analyzeQuery(Long id);

  List<QueryHistory> getQueryHistory(Long id);

}
