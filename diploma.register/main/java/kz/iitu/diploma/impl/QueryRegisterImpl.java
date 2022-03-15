package kz.iitu.diploma.impl;

import com.clearbit.ApiException;
import com.clearbit.Pair;
import com.clearbit.client.api.CombinedApi;
import com.clearbit.client.api.PersonApi;
import com.clearbit.client.model.PersonCompany;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import kz.iitu.diploma.register.QueryRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryRegisterImpl implements QueryRegister {

  @Autowired
  private GoogleSearchService     googleSearchService;
  @Autowired
  private YandexSearchService     yandexSearchService;
  @Autowired
  private DuckDuckGoSearchService duckDuckGoSearchService;

  @Override
  public void executeQuery(QueryRecord queryRecord) {

    int partitionSize = 3;

    List<List<String>> dividedQuery = new LinkedList<>();
    for (int i = 0; i < queryRecord.queryList.size(); i += partitionSize) {
      dividedQuery.add(queryRecord.queryList.subList(i,
          Math.min(i + partitionSize, queryRecord.queryList.size())));
    }

    int i = -1;

    GoogleResult googleResult     = new GoogleResult();
    List<String> yandexResult     = new ArrayList<>();
    List<String> duckDuckGoResult = new ArrayList<>();

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

    System.out.println("YES");

  }

  public static void main(String[] args) {
    final String clearbit_key =("sk_5bad0b79c9ac34edd5472b22c4a56f89");
    PersonApi    api          = new PersonApi();
    api.getApiClient().setUsername(clearbit_key);

    try {
      ObjectMapper  mapper        = new ObjectMapper();
      var c = api.streamingLookup("alpysbayevdias01@gmail.com");
//      PersonCompany personCompany = api.streamingLookup("alpysbayevdias01@gmail.com");

      List<Pair> a = new ArrayList<>();
      Pair p = new Pair("company", "iitu");
      a.add(p);
      // access attrs form response
//      api.doReq("api.URL", a);
//      System.out.println(personCompany.getPerson().getEmail());

      // print full JSON payload
      String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(c);
      System.out.println(jsonBody);
    } catch (JsonProcessingException | ApiException e) {
      e.printStackTrace();
    }
  }
}
