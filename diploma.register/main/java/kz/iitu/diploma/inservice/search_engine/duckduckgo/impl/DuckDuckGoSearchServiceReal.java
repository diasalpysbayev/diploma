package kz.iitu.diploma.inservice.search_engine.duckduckgo.impl;

import com.google.gson.Gson;
import kz.iitu.diploma.config.DuckDuckGoApiConfig;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.model.search_engine.QueryResult;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class DuckDuckGoSearchServiceReal implements DuckDuckGoSearchService {

  private final DuckDuckGoApiConfig duckDuckGoApiConfig;

  public DuckDuckGoSearchServiceReal(DuckDuckGoApiConfig duckDuckGoApiConfig) {
    this.duckDuckGoApiConfig = duckDuckGoApiConfig;
  }

  @SneakyThrows
  @Override
  public QueryResult search(String oldQuery) {
    QueryResult ddg = new QueryResult();

    HttpClient httpClient = HttpClientBuilder.create().build();
    var query = oldQuery.replace(" ", "+");

//    String kl  = "&kl=fr-fr"; // region
    String safe  = "&safe=-2";

    StringBuilder q = new StringBuilder("&q=" + query);

    q.append(safe);

    String  api     = this.duckDuckGoApiConfig.url() + q + "&api_key=" + this.duckDuckGoApiConfig.api();
    HttpGet request = new HttpGet(api);

    request.addHeader("Accept-Language", "ru");

    try {
      HttpResponse   response = httpClient.execute(request);
      BufferedReader rd       = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder  result   = new StringBuilder();
      String         line;

      while ((line = rd.readLine()) != null) {
        result.append(line);
      }

      Gson gson = new Gson();

      Map map = gson.fromJson(result.toString(), Map.class);

      ddg.list.add(map);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    return ddg;

  }

}
