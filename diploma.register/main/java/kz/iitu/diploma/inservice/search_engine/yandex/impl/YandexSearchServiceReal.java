package kz.iitu.diploma.inservice.search_engine.yandex.impl;

import com.google.gson.Gson;
import kz.iitu.diploma.config.YandexApiConfig;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
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

public class YandexSearchServiceReal implements YandexSearchService {

  private final YandexApiConfig yandexApiConfig;

  public YandexSearchServiceReal(YandexApiConfig yandexApiConfig) {
    this.yandexApiConfig = yandexApiConfig;
  }

  @SneakyThrows
  @Override
  public QueryResult search(String oldQuery) {
    QueryResult googleResult = new QueryResult();

    HttpClient httpClient = HttpClientBuilder.create().build();
    var query = oldQuery.replace(" ", "+");

    String kz    = "&yandex_domain=yandex.kz";

    StringBuilder q = new StringBuilder("&text=" + query); // lr - location
    q.append(kz);

    String  api     = this.yandexApiConfig.url() + q + "&api_key=" + this.yandexApiConfig.api();
    HttpGet request = new HttpGet(api);

    request.addHeader("Accept-Language", "ru");

    try {
      HttpResponse   response = httpClient.execute(request);
      BufferedReader rd       = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String         line;
      StringBuilder  result   = new StringBuilder();

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
