package kz.iitu.diploma.inservice.search_engine.yandex.impl;

import kz.iitu.diploma.config.YandexApiConfig;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class YandexSearchServiceReal implements YandexSearchService {

  private final YandexApiConfig yandexApiConfig;

  public YandexSearchServiceReal(YandexApiConfig yandexApiConfig) {
    this.yandexApiConfig = yandexApiConfig;
  }

  @SneakyThrows
  @Override
  public List<String> search(String query) {
    HttpClient httpClient = HttpClientBuilder.create().build();

    List<String> responseList = new ArrayList<>();
    List<String> list         = List.of(query.split(","));

    String gl    = "&gl=kz";
    String safe  = "&safe=off";
    String asQdr = "&as_qdr=d10"; // last 10 days

    StringBuilder q = new StringBuilder("&text=");
    list.forEach(str -> {
      String asEpq = "&as_epq=" + str; // обязательно быть в поиске
      q.append(str).append("+OR+");
    });

    q.delete(q.length() - 4, q.length());
    q.append(gl).append(safe);

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

      responseList.add(result.toString());
    } catch (IOException e) {
      throw new RuntimeException();
    }

    return responseList;
  }
}
