package kz.iitu.diploma.bean;

import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import kz.greetgo.file_storage.FileStorage;
import kz.greetgo.file_storage.impl.FileStorageBuilder;
import kz.iitu.diploma.config.FileStorageConfig;
import kz.iitu.diploma.util.DbConnector;
import kz.iitu.diploma.util.MimeTypeConfigurator;
import liquibase.pro.packaged.D;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FileStorageConfiguration {

  /**
   * Количество БД для хранилищя файлов (МЕНЯТЬ НЕЛЬЗЯ)
   */
  public static final  int DB_COUNT    = 10;
  /**
   * Количество таблиц в хранилище файлов (МЕНЯТЬ НЕЛЬЗЯ)
   */
  private static final int TABLE_COUNT = 48;

  private final FileStorageConfig storageConfig;

  @Bean
  public FileStorage createE() {


    var a = List.of(new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector(),new DbConnector() );

    return FileStorageBuilder
        .newBuilder()
        .configureFrom(MimeTypeConfigurator.get())
        .mandatoryName(true)
        .inMultiDb(a.stream().map(this::convert).collect(Collectors.toList()))
        .setTableCountPerDb(TABLE_COUNT)
        .build();

  }

  private DataSource convert(DbConnector dbConnector) {
    HikariDataSource pool = new HikariDataSource();

    pool.setDriverClassName("org.postgresql.Driver");
    pool.setJdbcUrl(dbConnector.url);
    pool.setUsername(dbConnector.username);
    pool.setPassword(dbConnector.password);
    pool.setMaximumPoolSize(200);

    pool.setMinimumIdle(5);

    return pool;

  }


  private List<DataSource> checkSize(List<DataSource> list) {
    if (list.size() != DB_COUNT) {
      throw new RuntimeException("Left Db Count: must be = " + DB_COUNT + ", actual is " + list.size());
    }
    return list;
  }

}
