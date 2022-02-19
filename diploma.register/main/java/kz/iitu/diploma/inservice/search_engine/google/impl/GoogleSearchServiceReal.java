package kz.iitu.diploma.inservice.search_engine.google.impl;

import com.google.gson.Gson;
import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class GoogleSearchServiceReal implements GoogleSearchService {

  private final GoogleApiConfig googleApiConfig;


  public GoogleSearchServiceReal(GoogleApiConfig googleApiConfig) {
    this.googleApiConfig = googleApiConfig;
  }

  @SneakyThrows
  @Override
  public GoogleResult search(String query) {
    HttpClient httpClient = HttpClientBuilder.create().build();

    List<String> list         = List.of(query.split(","));
    GoogleResult googleResult = new GoogleResult();

    String gl    = "&gl=kz";
    String safe  = "&safe=off";
    String asQdr = "&as_qdr=d10"; // last 10 days

    StringBuilder q = new StringBuilder("&q=");
    list.forEach(str -> {
      String asEpq = "&as_epq=" + str; // обязательно быть в поиске
      q.append(str).append("+OR+");
    });

    q.delete(q.length() - 4, q.length());
    q.append(gl).append(safe);

    String  api     = this.googleApiConfig.url() + q + "&api_key=" + this.googleApiConfig.api();
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
