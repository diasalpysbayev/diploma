package kz.iitu.diploma.bean;

import okhttp3.OkHttpClient;
import okhttp3.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientConfiguration {
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .readTimeout(15000, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .connectionPool(new ConnectionPool(300, 5L, TimeUnit.MINUTES))
        .build();
  }
}
