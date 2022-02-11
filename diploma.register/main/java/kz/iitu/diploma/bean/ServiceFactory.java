package kz.iitu.diploma.bean;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.google.GoogleSearchService;
import kz.iitu.diploma.inservice.google.impl.GoogleSearchServiceFake;
import kz.iitu.diploma.inservice.google.impl.GoogleSearchServiceReal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceFactory {

  private final GoogleApiConfig  googleApiConfig;

  @Bean
  public GoogleSearchService googleSearchRegister() {
    if (googleApiConfig.useFake()) return new GoogleSearchServiceFake();

    return new GoogleSearchServiceReal(googleApiConfig);
  }


}
