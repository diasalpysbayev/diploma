package kz.iitu.diploma.bean;

import kz.greetgo.conf.hot.FileConfigFactory;
import kz.iitu.diploma.config.*;
import kz.iitu.diploma.util.AppFolderPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class AllConfigFactory extends FileConfigFactory {

  @Override
  protected Path getBaseDir() {
    return AppFolderPath.confDirPath();
  }

  @Bean
  public DbConfig createPostgresDbConfig() {
    return createConfig(DbConfig.class);
  }

  @Bean
  public SchedulerConfig createSchedulerConfig() {
    return createConfig(SchedulerConfig.class);
  }

  @Bean
  public GoogleApiConfig createGoogleApiConfig() {
    return createConfig(GoogleApiConfig.class);
  }

  @Bean
  public YandexApiConfig createYandexApiConfig() {
    return createConfig(YandexApiConfig.class);
  }

  @Bean
  public DuckDuckGoApiConfig createDuckDuckGoApiConfig() {
    return createConfig(DuckDuckGoApiConfig.class);
  }

  @Bean
  public InstagramConfig createInstagramConfig() {
    return createConfig(InstagramConfig.class);
  }

  @Bean
  public SmsServiceConfig createSmsServiceConfig() {
    return createConfig(SmsServiceConfig.class);
  }

  @Bean
  public FileStorageConfig createFileStorageConfig() {
    return createConfig(FileStorageConfig.class);
  }

  @Bean
  public BingApiConfig createBingConfig() {
    return createConfig(BingApiConfig.class);
  }

}
