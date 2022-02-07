package kz.iitu.diploma._preparation_.bean;

import kz.greetgo.conf.hot.DefaultStrValue;
import kz.greetgo.conf.hot.FirstReadEnv;
import kz.greetgo.conf.sys_params.SysParams;
import kz.iitu.diploma.config.DbConfig;
import kz.iitu.diploma.util.AppFolderPath;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbWorker {
  public void recreate() throws Exception {
    initParams();
    DbParams params = DbParams.readParams();
    killDb(params);
    createDb(params);
    runLiquibase(params);
  }

  public void kill() throws Exception {
    initParams();
    DbParams params = DbParams.readParams();
    killDb(params);
  }


  void killDb(DbParams params) throws Exception {
    try (Connection con = getAdminConnection()) {
      String dbName = extractDbName(params.url);
      String userName = params.username;
      execSql(con, "drop database if EXISTS " + dbName);
      execSql(con, "drop owned by " + userName + " cascade");
      execSql(con, "drop user " + userName);
    } catch (PSQLException e) {
      System.err.println(e);
    }
  }

  void createDb(DbParams params) throws Exception {
    try (Connection con = getAdminConnection()) {
      String dbName = extractDbName(params.url);
      String userName = params.username;
      execSql(con, "create database " + dbName);
      execSql(con, "create user " + userName + " with password '" + params.password + "'");
      execSql(con, "GRANT ALL ON DATABASE " + dbName + " TO " + userName);
    } catch (PSQLException e) {
      System.err.println(e);
    }
  }

  private void createPropertiesFile(File f) throws Exception {
    f.getParentFile().mkdirs();

    PrintStream out = new PrintStream(new FileOutputStream(f), true, "UTF-8");

    for (Method method : DbConfig.class.getMethods()) {

      FirstReadEnv env = method.getAnnotation(FirstReadEnv.class);
      if (env != null) {
        String envKey = env.value();
        String property = System.getenv(envKey);
        if (property != null) {
          out.println(method.getName() + "=" + property);
          continue;
        }
      }

      DefaultStrValue annotation = method.getAnnotation(DefaultStrValue.class);
      if (annotation != null) {
        out.println(method.getName() + "=" + annotation.value());
      } else {
        out.println(method.getName() + "url=");
      }

    }

    out.close();
  }

  public void runLiquibase(DbParams dbParams) throws SQLException, LiquibaseException, ClassNotFoundException {
    try (Connection connection = getConnection(dbParams)) {
      new Liquibase("liquibase/changelog-master.xml", new ClassLoaderResourceAccessor(),
          new JdbcConnection(connection)).update((String) null);
    }
  }

  public Connection getConnection(DbParams dbParams) throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
        dbParams.url().toLowerCase(), dbParams.username.toLowerCase(), dbParams.password
    );
  }

  private void initParams() throws Exception {
    File f = new File(AppFolderPath.confDir() + "/" + DbConfig.class.getSimpleName() + ".hotconfig");
    if (!f.exists()) {
      createPropertiesFile(f);
    } else {
      try (InputStream inputStream = new FileInputStream(
          AppFolderPath.confDir() + "/" + DbConfig.class.getSimpleName() + ".hotconfig")) {
        Properties properties = new Properties();
        properties.load(inputStream);

        String defaultUrlValue = DbConfig.class.getMethod("url").getAnnotation(DefaultStrValue.class).value();
        if (defaultUrlValue.equals(properties.getProperty("url"))) {
          createPropertiesFile(f);
        }

      }
    }
  }

  private void execSql(Connection con, String sql) throws SQLException {
    try (Statement stt = con.createStatement()) {
      System.out.println("EXEC SQL: " + sql);
      stt.executeUpdate(sql);
    }
  }

  public static Connection getAdminConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
        SysParams.pgAdminUrl(),
        SysParams.pgAdminUserid(),
        "postgres");
  }

  public static String extractDbName(String url) {
    int idx = url.lastIndexOf('/');
    return url.substring(idx + 1);
  }

}
