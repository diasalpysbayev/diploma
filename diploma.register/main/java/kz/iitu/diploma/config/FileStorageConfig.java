package kz.iitu.diploma.config;

import kz.greetgo.conf.hot.DefaultListSize;
import kz.greetgo.conf.hot.Description;
import kz.iitu.diploma.util.DbConnector;

import java.util.List;

@Description("Параметры доступа к БД FileStorage (используется только БД Postgresql)")
public interface FileStorageConfig {

  @Description("URL доступа к БД")
  @DefaultListSize(10)
  List<DbConnector> db();

}
