package kz.iitu.diploma.config;

import kz.greetgo.conf.hot.DefaultBoolValue;
import kz.greetgo.conf.hot.DefaultStrValue;
import kz.greetgo.conf.hot.Description;

@Description("Instagram Configuration")
public interface InstagramConfig {

  @Description("Use fake")
  @DefaultBoolValue(true)
  boolean useFake();

  @Description("Instagram Username")
  @DefaultStrValue("diasalpysbayev")
  String username();

  @Description("Instagram Password")
  @DefaultStrValue("Aurum753335")
  String password();
}

