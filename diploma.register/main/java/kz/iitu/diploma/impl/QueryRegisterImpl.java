package kz.iitu.diploma.impl;

import com.nimbusds.jose.shaded.json.JSONObject;
import kz.iitu.diploma.dao.QueryDao;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.QueryResult;
import kz.iitu.diploma.register.QueryRegister;
import kz.iitu.diploma.register.SessionRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QueryRegisterImpl implements QueryRegister {

  @Autowired
  private GoogleSearchService     googleSearchService;
  @Autowired
  private YandexSearchService     yandexSearchService;
  @Autowired
  private DuckDuckGoSearchService duckDuckGoSearchService;
  @Autowired
  private QueryDao                queryDao;
  @Autowired
  private SessionRegister         sessionRegister;

  @Override
  public void executeQuery(QueryRecord queryRecord) {
    var dividedQuery = divideQuery(queryRecord);

    int i = -1;

    QueryResult googleResult     = new QueryResult();
    QueryResult yandexResult     = new QueryResult();
    QueryResult duckDuckGoResult = new QueryResult();

    for (List<String> queryList : dividedQuery) {
      i++;
      for (String query : queryList) {
        if (i == 0) {
          googleResult = googleSearchService.search(query);
        } else if (i == 1) {
          yandexResult = yandexSearchService.search(query);
        } else {
          duckDuckGoResult = duckDuckGoSearchService.search(query);
        }
      }
    }

    String valuestr = parseResult(googleResult, "google") +
        parseResult(yandexResult, "yandex") +
        parseResult(duckDuckGoResult, "duckduckgo");

    queryDao.saveQuery(queryDao.nextQueryId(), sessionRegister.getPrincipal(), valuestr);
  }

  private List<List<String>> divideQuery(QueryRecord queryRecord) {
    int partitionSize = (queryRecord.queryList.size() + 3 - 1) / 3;

    List<List<String>> dividedQuery = new LinkedList<>();
    for (int i = 0; i < queryRecord.queryList.size(); i += partitionSize) {
      dividedQuery.add(queryRecord.queryList.subList(i,
          Math.min(i + partitionSize, queryRecord.queryList.size())));
    }

    return dividedQuery;
  }

  private String parseResult(QueryResult queryResult, String name) {
    StringBuilder builder    = new StringBuilder();
    List<String>  duplicates = new ArrayList<>();

    switch (name) {
      case "google":
        queryResult.list.forEach(map -> {
          builder.append("GOOGLESEARCH:");

          for (Object key : map.keySet()) {
            if (key.equals("search_metadata")) {
              if (!new JSONObject((Map) map.get(key)).get("status").equals("Success")) {
                continue;
              }
            }

            if (key.equals("top_stories")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(getTitle(o)).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("related_questions")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(getTitle(o)).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("organic_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(getTitle(o)).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("related_searches")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(new JSONObject((Map) o).get("query")).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
      case "yandex":
        queryResult.list.forEach(map -> {
          builder.append("YANDEXSEARCH:");

          for (Object key : map.keySet()) {
            if (key.equals("search_metadata")) {
              if (!new JSONObject((Map) map.get(key)).get("status").equals("Success")) {
                continue;
              }
            }

            if (key.equals("organic_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(getTitle(o)).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
      case "duckduckgo":
        queryResult.list.forEach(map -> {
          builder.append("DUCKDUCKGOSEARCH:");

          for (Object key : map.keySet()) {
            if (key.equals("search_metadata")) {
              if (!new JSONObject((Map) map.get(key)).get("status").equals("Success")) {
                continue;
              }
            }

            if (key.equals("organic_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  builder.append("title=").append(getTitle(o)).append(";");
                  builder.append("link=").append(getLink(o)).append(";");
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
    }

    return builder.toString();
  }

  private String getLink(Object o) {
    return new JSONObject((Map) o).get("link").toString();
  }

  private String getTitle(Object o) {
    return new JSONObject((Map) o).get("title").toString();
  }
}
