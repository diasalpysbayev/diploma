package kz.iitu.diploma.impl;

import com.nimbusds.jose.shaded.json.JSONObject;
import kz.iitu.diploma.dao.QueryDao;
import kz.iitu.diploma.inservice.search_engine.bing.BingSearchService;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.inservice.search_engine.youtube.YouTubeService;
import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.QueryResult;
import kz.iitu.diploma.model.search_engine.SearchInformation;
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
  private BingSearchService       bingSearchService;
  @Autowired
  private YouTubeService          youTubeService;
  @Autowired
  private QueryDao                queryDao;
  @Autowired
  private SessionRegister         sessionRegister;

  @Override
  public List<SearchInformation> executeQuery(QueryRecord queryRecord) {
    var dividedQuery = divideQuery(queryRecord);

    int i = -1;

    QueryResult googleResult     = new QueryResult();
    QueryResult yandexResult     = new QueryResult();
    QueryResult duckDuckGoResult = new QueryResult();
    QueryResult bingResult       = new QueryResult();
    QueryResult youTubeResult    = new QueryResult();

    for (List<QueryDetail> queryList : dividedQuery) {
      i++;
      for (QueryDetail query : queryList) {
        if (query.isVideo) {
          youTubeResult = youTubeService.search(query.name);
        }
        //        if (i == 0) {
        //          googleResult = googleSearchService.search(query.name);
        //        } else if (i == 1) {
        //          yandexResult = yandexSearchService.search(query.name);
        //        } else if (i == 2) {
        //          duckDuckGoResult = duckDuckGoSearchService.search(query.name);
        //        } else {
        //          bingResult = bingSearchService.search(query.name);
        //        }
      }
    }

    List<SearchInformation> searchInformationList = new LinkedList<>();
    searchInformationList.addAll(parseResult(googleResult, "google"));
    searchInformationList.addAll(parseResult(yandexResult, "yandex"));
    searchInformationList.addAll(parseResult(duckDuckGoResult, "duckduckgo"));
    searchInformationList.addAll(parseResult(bingResult, "bing"));
    searchInformationList.addAll(parseResult(youTubeResult, "youtube"));

    var queryId = queryDao.nextQueryId();
    queryDao.saveQuery(queryId, sessionRegister.getPrincipal(), queryRecord.queryList.toString());

    searchInformationList.forEach(searchInformation -> {
      var queryInfoId = queryDao.nextQueryInfoId();
      queryDao.saveQueryInfo(queryInfoId, queryId, searchInformation.title, searchInformation.url);
    });

    return searchInformationList;
  }

  private List<List<QueryDetail>> divideQuery(QueryRecord queryRecord) {
    int partitionSize = (queryRecord.queryList.size() + 3 - 1) / 3;

    List<List<QueryDetail>> dividedQuery = new LinkedList<>();
    for (int i = 0; i < queryRecord.queryList.size(); i += partitionSize) {
      dividedQuery.add(queryRecord.queryList.subList(i,
          Math.min(i + partitionSize, queryRecord.queryList.size())));
    }

    return dividedQuery;
  }

  private List<SearchInformation> parseResult(QueryResult queryResult, String name) {
    List<SearchInformation> searchInformationList = new LinkedList<>();
    List<String>            duplicates            = new ArrayList<>();

    switch (name) {
      case "google":
        queryResult.list.forEach(map -> {
          for (Object key : map.keySet()) {
            if (key.equals("search_metadata")) {
              if (!new JSONObject((Map) map.get(key)).get("status").equals("Success")) {
                continue;
              }
            }

            if (key.equals("top_stories")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("related_questions")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  duplicates.add(getLink(o));
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                }
              }
            }

            if (key.equals("organic_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  duplicates.add(getLink(o));
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                }
              }
            }

            if (key.equals("related_searches")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(new JSONObject((Map) o).get("query").toString())
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
      case "yandex":
      case "duckduckgo":
      case "bing":
        queryResult.list.forEach(map -> {
          for (Object key : map.keySet()) {
            if (key.equals("search_metadata")) {
              if (!new JSONObject((Map) map.get(key)).get("status").equals("Success")) {
                continue;
              }
            }

            if (key.equals("organic_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
      case "youtube":
        queryResult.list.forEach(map -> {
          for (Object key : map.keySet()) {
            if (key.equals("video_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("channel_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }

            if (key.equals("movie_results")) {
              for (Object o : (List) map.get(key)) {
                if (!duplicates.contains(getLink(o))) {
                  searchInformationList.add(SearchInformation.builder()
                      .title(getTitle(o))
                      .url(getLink(o))
                      .build());
                  duplicates.add(getLink(o));
                }
              }
            }
          }
        });
        break;
    }

    return searchInformationList;
  }

  private String getLink(Object o) {
    return new JSONObject((Map) o).get("link").toString();
  }

  private String getTitle(Object o) {
    return new JSONObject((Map) o).get("title").toString();
  }
}
