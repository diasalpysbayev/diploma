package kz.iitu.diploma._preparation_.beans;

import kz.iitu.diploma.config.DbConfig;
import kz.iitu.diploma.util.AppFolderPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DbParams {

  public String url;
  public String username;
  public String password;

  public static DbParams readParams() throws IOException {
    Properties properties = new Properties();

    try (FileInputStream input = new FileInputStream(
        AppFolderPath.confDir() + "/" + DbConfig.class.getSimpleName() + ".hotconfig")) {
      properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));

      DbParams ret = new DbParams();
      ret.username = properties.getProperty("username");
      ret.password = properties.getProperty("password");
      ret.url      = properties.getProperty("url");
      return ret;
    }
  }

  public static DbParams readFileStorageParams() throws IOException {

    Properties properties = new Properties();

    try (FileInputStream input = new FileInputStream(
        AppFolderPath.confDir() + "/" + DbConfig.class.getSimpleName() + ".hotconfig")) {
      properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));

      DbParams ret = new DbParams();
      ret.username = properties.getProperty("username") + "_fs";
      ret.password = properties.getProperty("password") + "_fs";
      ret.url      = properties.getProperty("url") + "_fs";
      return ret;
    }

  }

  public String url() {
    return url;
  }

}
