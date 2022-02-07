package kz.iitu.diploma.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppFolderPath {

  public static Path appDir() {
    return Paths.get(System.getProperty("user.home"), String.format("/%s.d", projectName()));
  }

  public static Path confDirPath() {
    return Paths.get(appDir().toString(), "/conf");
  }

  public static String confDir() {
    return appDir() + "/conf";
  }

  public static String securityDir() {
    return appDir() + "/security";
  }

  public static String projectName() {
    return "diploma";
  }

  public static Path do_not_run_liquibase() {
    return appDir().resolve("do_not_run_liquibase");
  }

  public static String schedulerConfDir() {
    return appDir() + "/scheduler_conf";
  }

}
