package kz.iitu.diploma.inservice.search_engine.youtube.impl;

import com.google.gson.Gson;
import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.config.YouTubeApiConfig;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.youtube.YouTubeService;
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

public class YouTubeServiceReal implements YouTubeService {

  private final YouTubeApiConfig youTubeApiConfig;


  public YouTubeServiceReal(YouTubeApiConfig youTubeApiConfig) {
    this.youTubeApiConfig = youTubeApiConfig;
  }

  @SneakyThrows
  @Override
  public QueryResult search(String oldQuery) {
    HttpClient httpClient = HttpClientBuilder.create().build();

    var         query        = oldQuery.replace(" ", "+");
    QueryResult googleResult = new QueryResult();

    StringBuilder q = new StringBuilder("&search_query=" + query);

    String  api     = this.youTubeApiConfig.url() + q + "&api_key=" + this.youTubeApiConfig.api();
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

      googleResult.list.add(map);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    return googleResult;

  }

}
