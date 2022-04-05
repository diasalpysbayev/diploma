package kz.iitu.diploma.inservice.search_engine.google_maps.impl;

import com.google.gson.Gson;
import kz.iitu.diploma.config.GoogleMapsApiConfig;
import kz.iitu.diploma.config.YouTubeApiConfig;
import kz.iitu.diploma.inservice.search_engine.google_maps.GoogleMapsService;
import kz.iitu.diploma.model.query.QueryDetail;
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

public class GoogleMapsServiceReal implements GoogleMapsService {

  private final GoogleMapsApiConfig googleMapsApiConfig;


  public GoogleMapsServiceReal(GoogleMapsApiConfig googleMapsApiConfig) {
    this.googleMapsApiConfig = googleMapsApiConfig;
  }

  @SneakyThrows
  @Override
  public QueryResult search(QueryDetail oldQuery) {
    HttpClient httpClient = HttpClientBuilder.create().build();

    var         query        = oldQuery.name.replace(" ", "+");
    QueryResult googleResult = new QueryResult();

    StringBuilder q = new StringBuilder("&q=" + query);
    q.append("&type=search");
    q.append("&ll=@").append(oldQuery.latitude).append(",").append(oldQuery.longitude).append(",20z");
    q.append("&google_domain=google.ru");
    q.append("&hl=kk");

//    q.append("&data=place"); если type = place - data is required

    String  api     = this.googleMapsApiConfig.url() + q + "&api_key=" + this.googleMapsApiConfig.api();
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
