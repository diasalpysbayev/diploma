package kz.iitu.diploma.bean;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.config.InstagramConfig;
import kz.iitu.diploma.config.YandexApiConfig;
import kz.iitu.diploma.inservice.google.GoogleSearchService;
import kz.iitu.diploma.inservice.google.impl.GoogleSearchServiceFake;
import kz.iitu.diploma.inservice.google.impl.GoogleSearchServiceReal;
import kz.iitu.diploma.inservice.instagram.InstagramService;
import kz.iitu.diploma.inservice.instagram.impl.InstagramServiceFake;
import kz.iitu.diploma.inservice.instagram.impl.InstagramServiceReal;
import kz.iitu.diploma.inservice.yandex.YandexSearchService;
import kz.iitu.diploma.inservice.yandex.impl.YandexSearchServiceFake;
import kz.iitu.diploma.inservice.yandex.impl.YandexSearchServiceReal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceFactory {

  private final GoogleApiConfig googleApiConfig;
  private final YandexApiConfig yandexApiConfig;
  private final InstagramConfig instagramConfig;

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
  public InstagramService instagramService() {
    if (instagramConfig.useFake()) return new InstagramServiceFake();

    return new InstagramServiceReal(instagramConfig);
  }
}
