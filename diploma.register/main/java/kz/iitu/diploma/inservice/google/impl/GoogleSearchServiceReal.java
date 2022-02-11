package kz.iitu.diploma.inservice.google.impl;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.google.GoogleSearchService;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GoogleSearchServiceReal implements GoogleSearchService {

  private final GoogleApiConfig googleApiConfig;

  public GoogleSearchServiceReal(GoogleApiConfig googleApiConfig) {
    this.googleApiConfig = googleApiConfig;
  }

  @SneakyThrows
  @Override
  public String search(String query) {
    HttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet    request    = new HttpGet(this.googleApiConfig.url());

    request.addHeader("Accept-Language", "ru");

    HttpResponse response = httpClient.execute(request);

    BufferedReader rd     = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder  result = new StringBuilder();
    String         line;

    while ((line = rd.readLine()) != null) {
      result.append(line);
    }

    return result.toString();
  }

}
