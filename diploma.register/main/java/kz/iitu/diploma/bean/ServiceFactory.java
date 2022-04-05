package kz.iitu.diploma.bean;

import kz.iitu.diploma.config.*;
import kz.iitu.diploma.inservice.instagram.InstagramService;
import kz.iitu.diploma.inservice.instagram.impl.InstagramServiceFake;
import kz.iitu.diploma.inservice.instagram.impl.InstagramServiceReal;
import kz.iitu.diploma.inservice.search_engine.bing.BingSearchService;
import kz.iitu.diploma.inservice.search_engine.bing.impl.BingSearchServiceFake;
import kz.iitu.diploma.inservice.search_engine.bing.impl.BingSearchServiceReal;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.DuckDuckGoSearchService;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.impl.DuckDuckGoSearchServiceFake;
import kz.iitu.diploma.inservice.search_engine.duckduckgo.impl.DuckDuckGoSearchServiceReal;
import kz.iitu.diploma.inservice.search_engine.google.GoogleSearchService;
import kz.iitu.diploma.inservice.search_engine.google.impl.GoogleSearchServiceFake;
import kz.iitu.diploma.inservice.search_engine.google.impl.GoogleSearchServiceReal;
import kz.iitu.diploma.inservice.search_engine.google_maps.GoogleMapsService;
import kz.iitu.diploma.inservice.search_engine.google_maps.impl.GoogleMapsServiceFake;
import kz.iitu.diploma.inservice.search_engine.google_maps.impl.GoogleMapsServiceReal;
import kz.iitu.diploma.inservice.search_engine.yandex.YandexSearchService;
import kz.iitu.diploma.inservice.search_engine.yandex.impl.YandexSearchServiceFake;
import kz.iitu.diploma.inservice.search_engine.yandex.impl.YandexSearchServiceReal;
import kz.iitu.diploma.inservice.search_engine.youtube.YouTubeService;
import kz.iitu.diploma.inservice.search_engine.youtube.impl.YouTubeServiceFake;
import kz.iitu.diploma.inservice.search_engine.youtube.impl.YouTubeServiceReal;
import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.inservice.sms.fake.SmsServiceFake;
import kz.iitu.diploma.inservice.sms.real.SmsServiceReal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ServiceFactory {

  private final GoogleApiConfig     googleApiConfig;
  private final YandexApiConfig     yandexApiConfig;
  private final DuckDuckGoApiConfig duckDuckGoApiConfig;
  private final InstagramConfig     instagramConfig;
  private final SmsServiceConfig    smsServiceConfig;
  private final BingApiConfig       bingApiConfig;
  private final YouTubeApiConfig    youTubeApiConfig;
  private final GoogleMapsApiConfig googleMapsApiConfig;
  private final RestTemplate        restTemplate;

  @Bean
  public GoogleSearchService googleSearchRegister() {
    if (googleApiConfig.useFake()) return new GoogleSearchServiceFake();

    return new GoogleSearchServiceReal(googleApiConfig);
  }

  @Bean
  public YandexSearchService yandexSearchRegister() {
    if (googleApiConfig.useFake()) return new YandexSearchServiceFake();

    return new YandexSearchServiceReal(yandexApiConfig);
  }

  @Bean
  public DuckDuckGoSearchService duckDuckGoSearchRegister() {
    if (duckDuckGoApiConfig.useFake()) return new DuckDuckGoSearchServiceFake();

    return new DuckDuckGoSearchServiceReal(duckDuckGoApiConfig);
  }

  @Bean
  public InstagramService instagramService() {
    if (instagramConfig.useFake()) return new InstagramServiceFake();

    return new InstagramServiceReal(instagramConfig);
  }

  @Bean
  public SmsService smsService() {
    if (smsServiceConfig.useFake()) {
      return new SmsServiceFake();
    }

    SmsServiceReal serviceReal = new SmsServiceReal(smsServiceConfig.host(), smsServiceConfig.apiKey(), smsServiceConfig.from());
    serviceReal.setRestTemplate(restTemplate);

    return serviceReal;
  }

  @Bean
  public BingSearchService bingSearchService() {
    if (bingApiConfig.useFake()) {
      return new BingSearchServiceFake();
    }

    return new BingSearchServiceReal(bingApiConfig);
  }

  @Bean
  public YouTubeService youTubeService() {
    if (youTubeApiConfig.useFake()) {
      return new YouTubeServiceFake();
    }

    return new YouTubeServiceReal(youTubeApiConfig);
  }

  @Bean
  public GoogleMapsService googleMapsService() {
    if (youTubeApiConfig.useFake()) {
      return new GoogleMapsServiceFake();
    }

    return new GoogleMapsServiceReal(googleMapsApiConfig);
  }
}
