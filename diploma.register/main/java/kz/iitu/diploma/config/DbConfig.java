package kz.iitu.diploma.config;

import jdk.jfr.Description;
import kz.greetgo.conf.hot.DefaultStrValue;
import kz.greetgo.conf.hot.FirstReadEnv;

@Description("Параметры доступа к БД (используется только БД Postgresql)")
public interface DbConfig {

  @Description("URL доступа к БД")
  @DefaultStrValue("jdbc:postgresql://localhost:5432/diploma")
  @FirstReadEnv("DB_URL")
  String url();

  @Description("Пользователь для доступа к БД")
  @DefaultStrValue("diploma")
  @FirstReadEnv("DB_USER")
  String username();

  @Description("Пароль для доступа к БД")
  @DefaultStrValue("diploma")
  @FirstReadEnv("DB_PASSWORD")
  String password();
}

