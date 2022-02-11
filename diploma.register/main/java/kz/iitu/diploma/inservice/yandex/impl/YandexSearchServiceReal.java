package kz.iitu.diploma.inservice.yandex.impl;

import kz.iitu.diploma.config.YandexApiConfig;
import kz.iitu.diploma.inservice.yandex.YandexSearchService;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class YandexSearchServiceReal implements YandexSearchService {

  private final YandexApiConfig yandexApiConfig;

  public YandexSearchServiceReal(YandexApiConfig yandexApiConfig) {
    this.yandexApiConfig = yandexApiConfig;
  }

  @SneakyThrows
  @Override
  public String search(String query) {
    HttpClient  httpClient = HttpClientBuilder.create().build();
    HttpGet request    = new HttpGet(this.yandexApiConfig.url());

    request.addHeader("Accept-Language", "ru");

    HttpResponse response = httpClient.execute(request);

    StringBuilder  result = new StringBuilder();
    BufferedReader rd     = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    String         line;

    while ((line = rd.readLine()) != null) {
      result.append(line);
    }

    return result.toString();  }
}
