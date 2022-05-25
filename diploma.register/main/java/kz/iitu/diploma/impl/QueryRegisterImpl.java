package kz.iitu.diploma.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import kz.iitu.diploma.dao.AdminDao;
import kz.iitu.diploma.dao.QueryDao;
import kz.iitu.diploma.inservice.search_engine.bing.BingSearchService;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.google_maps.GoogleMapsService;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.inservice.search_engine.youtube.YouTubeService;
import kz.iitu.diploma.model.analytics.AnalyticsRecord;
import kz.iitu.diploma.model.analytics.AnalyticsToSave;
import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.query.QueryHistory;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.PlaceInfo;
import kz.iitu.diploma.model.search_engine.QueryResult;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import kz.iitu.diploma.register.SessionRegister;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;

@Service
@RequiredArgsConstructor
public class QueryRegisterImpl implements QueryRegister {

  private static final Logger                  log = LogManager.getLogger(AuthRegisterImpl.class);
  @Autowired
  private              GoogleSearchService     googleSearchService;
  @Autowired
  private              YandexSearchService     yandexSearchService;
  @Autowired
  private              DuckDuckGoSearchService duckDuckGoSearchService;
  @Autowired
  private              BingSearchService       bingSearchService;
  @Autowired
  private              YouTubeService          youTubeService;
  @Autowired
  private              GoogleMapsService       googleMapsService;
  @Autowired
  private              QueryDao                queryDao;
  @Autowired
  private              AdminDao                adminDao;
  @Autowired
  private              SessionRegister         sessionRegister;

  private static String getUrlContents(String theUrl) {
    StringBuilder content = new StringBuilder();
    try {
      URL           url           = new URL(theUrl);
      URLConnection urlConnection = url.openConnection();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String         line;
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }
      bufferedReader.close();
    } catch (Exception e) {
    }
    return content.toString();
  }

  @SneakyThrows
  private static List<String> getListFromFile(String url) {
    List<String> listOfStrings = new ArrayList<>();

    BufferedReader bf = new BufferedReader(
        new FileReader(url));

    String line = bf.readLine();

    while (line != null) {
      listOfStrings.add(line);
      line = bf.readLine();
    }

    bf.close();

    return listOfStrings;
  }

  @Override
  public List<SearchInformation> executeQuery(QueryRecord queryRecord) {
    log.info("xo5uPSTDKB :: QueryRecord = " + queryRecord);

    List<String> blockedList = adminDao.getBlockedList();

    if (!checkBlockedWord(blockedList, queryRecord.queryList)) {
      return null;
    }

    var dividedQuery = divideQuery(queryRecord);

    int i = -1;

    QueryResult googleResult     = new QueryResult();
    QueryResult yandexResult     = new QueryResult();
    QueryResult duckDuckGoResult = new QueryResult();
    QueryResult bingResult       = new QueryResult();
    QueryResult youTubeResult    = new QueryResult();
    QueryResult googleMapsResult = new QueryResult();

    for (List<QueryDetail> queryList : dividedQuery) {
      i++;
      for (QueryDetail query : queryList) {
        if (query.latitude != null && query.longitude != null) {
          googleMapsResult = googleMapsService.search(query);
        }
        if (query.isVideo) {
          youTubeResult = youTubeService.search(query.name);
        }
        if (i == 0) {
          googleResult = googleSearchService.search(query.name);
        } else if (i == 1) {
          yandexResult = yandexSearchService.search(query.name);
        } else if (i == 2) {
          duckDuckGoResult = duckDuckGoSearchService.search(query.name);
        } else {
          bingResult = bingSearchService.search(query.name);
        }
      }
    }

    List<SearchInformation> searchInformationList = new LinkedList<>();
    searchInformationList.addAll(parseResult(googleResult, "google"));
    searchInformationList.addAll(parseResult(yandexResult, "yandex"));
    searchInformationList.addAll(parseResult(duckDuckGoResult, "duckduckgo"));
    searchInformationList.addAll(parseResult(bingResult, "bing"));
    searchInformationList.addAll(parseResult(youTubeResult, "youtube"));
    searchInformationList.addAll(parseResult(googleMapsResult, "google-maps"));

    var queryId = queryDao.nextQueryId();
    queryDao.saveQuery(queryId, queryRecord.clientId, queryRecord.queryList.toString());

    for (SearchInformation searchInformation : searchInformationList) {
      var queryInfoId = queryDao.nextQueryInfoId();
      searchInformation.queryId = queryId;
      queryDao.saveQueryInfo(queryInfoId, queryId, searchInformation.title, searchInformation.url, searchInformation.placeInfo);
    }

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
      case "google-maps":
        queryResult.list.forEach(map -> {
          for (Object key : map.keySet()) {
            if (key.equals("place_results")) {
              String a;
              try {
                a = new ObjectMapper().writeValueAsString(map.get(key));
                var        b   = new ObjectMapper().readValue(a, Object.class);
                JSONObject obj = new JSONObject((Map<String, ?>) b);
                PlaceInfo placeInfo = PlaceInfo.builder()
                    .address(obj.get("address").toString())
                    .description(obj.get("description").toString())
                    .phone(obj.get("phone").toString())
                    .rating(new BigDecimal(obj.get("rating").toString()))
                    .longitude(new BigDecimal(String.valueOf(((Map<String, BigDecimal>) obj.get("gps_coordinates")).get("longitude"))))
                    .latitude(new BigDecimal(String.valueOf(((Map<String, BigDecimal>) obj.get("gps_coordinates")).get("latitude"))))
                    .build();

                searchInformationList.add(SearchInformation.builder()
                    .url(obj.get("website").toString())
                    .title(obj.get("title").toString())
                    .placeInfo(placeInfo)
                    .build());

              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
            }
          }
        });
        break;
    }

    return searchInformationList;
  }

  @SneakyThrows
  @Override
  public AnalyticsRecord analyzeQuery(Long id) {

    AnalyticsToSave analytics = queryDao.getAnalytics(id);

    if (analytics != null) {
      return buildAnalyticsRecord(analytics);
    }

    List<String> urls = queryDao.getQueryDetails(id);

    if (urls == null || urls.size() == 0) {
      return null;
    }

    Map<String, Integer> result    = new HashMap<>();
    InputStream          is        = new FileInputStream("main/resources/en-token.bin");
    TokenizerModel       model     = new TokenizerModel(is);
    TokenizerME          tokenizer = new TokenizerME(model);

    List<String> stopList = getListFromFile("main/resources/stop_words_russian.txt");
    List<String> cityList = getListFromFile("main/resources/city.txt");

    for (String url : urls) {
      String   output = getUrlContents(url);
      Document doc    = Jsoup.parse(output);
      String   text   = doc.body().text();

      log.info("7sDdIrT7s6 :: text from url " + url + " :: " + text);

      List<String> tokens = List.of(tokenizer.tokenize(text));


      for (String token : tokens) {
        var meanings = lookupForMeanings(token);

        if (stopList.contains(token)) {
          continue;
        }

        if (meanings.stream().map(x -> x.getLemma().toString().toLowerCase())
            .collect(Collectors.toList()).contains(token.toLowerCase())) {
          token = meanings.get(0).getLemma().toString();
        }

        if (result.containsKey(token)) {
          result.put(token, result.get(token) + 1);
        } else {
          result.put(token, 1);
        }
      }
    }

    List<Map.Entry<String, Integer>> sorted = result.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .filter(x -> x.getValue() > 3)
        .collect(Collectors.toList());

    String valuestr = sorted.stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining(","));

    String top = sorted.stream()
        .limit(10)
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining(","));

    String foundCity = sorted.stream()
        .filter(x -> cityList.stream().anyMatch(x.getKey()::equalsIgnoreCase))
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining(","));

    AnalyticsToSave analyticsRecord = AnalyticsToSave.builder()
        .id(queryDao.nextAnalyticsId())
        .queryId(id)
        .valuestr(valuestr)
        .city(foundCity)
        .top(top)
        .build();

    queryDao.insertAnalytics(analyticsRecord);

    return buildAnalyticsRecord(analyticsRecord);
  }


  private String getLink(Object o) {
    return new JSONObject((Map) o).get("link").toString();
  }

  private String getTitle(Object o) {
    return new JSONObject((Map) o).get("title").toString();
  }

  private String getParam(Object o, String name) {
    return new JSONObject((Map) o).get(name).toString();
  }

  private Map<String, Integer> getMap(List<String> allValues) {
    Map<String, Integer> map = new HashMap<>();

    for (String value : allValues) {
      String  key = value.split("=")[0];
      Integer val = Integer.valueOf(value.split("=")[1]);
      map.put(key, val);
    }

    return map;
  }

  private List<String> getString(String str) {
    return Stream.of(str.split(","))
        .map(String::new)
        .collect(Collectors.toList());
  }

  private AnalyticsRecord buildAnalyticsRecord(AnalyticsToSave analytics) {
    List<String> allValues  = getString(analytics.valuestr);
    List<String> cityValues = getString(analytics.city);
    List<String> topValues  = getString(analytics.top);

    Map<String, Integer> mapAllValues = getMap(allValues);
    Map<String, Integer> mapCity      = getMap(cityValues);
    Map<String, Integer> mapTop       = getMap(topValues);

    return AnalyticsRecord.builder()
        .queryId(analytics.queryId)
        .id(analytics.id)
        .city(mapCity)
        .valuestr(mapAllValues)
        .top(mapTop)
        .build();
  }

  private boolean checkBlockedWord(List<String> blockedList, List<QueryDetail> queryList) {
    for (String blocked : blockedList) {
      for (QueryDetail detail : queryList) {
        if (detail.name.toLowerCase().contains(blocked.toLowerCase())) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public List<QueryHistory> getQueryHistory(Long id) {
    List<Long>              ids                   = queryDao.getClientQueryIds(id);
    List<QueryHistory>      queryHistoryList      = new ArrayList<>();

    for (Long clientId : ids) {
      List<SearchInformation> history = queryDao.getHistory(clientId);
      if (history.size() > 0) {
        QueryHistory queryHistory = new QueryHistory();
        queryHistory.searchInformationList = history;
        queryHistoryList.add(queryHistory);
      }
    }

    return queryHistoryList;
  }
}
