package kz.iitu.diploma.config;

import kz.greetgo.conf.hot.*;

@Description("Google API Configuration")
public interface GoogleApiConfig {

  @Description("Usa fake")
  @DefaultBoolValue(true)
  boolean useFake();

  @Description("Url")
  @DefaultStrValue("https://serpapi.com/search.json?engine=google")
  String url();

  @Description("API key")
  @DefaultStrValue("c56b6098975e346c21bf5197ae7f8faf2258a34e181be76887ed22b1f4616ec2")
  String api();

}

