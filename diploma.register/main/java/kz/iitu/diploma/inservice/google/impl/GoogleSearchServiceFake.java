package kz.iitu.diploma.inservice.google.impl;

import kz.iitu.diploma.inservice.google.GoogleSearchService;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GoogleSearchServiceFake implements GoogleSearchService {

  @SneakyThrows
  @Override
  public String search(String query) {
    return null;
  }

}
