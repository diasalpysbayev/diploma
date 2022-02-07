package kz.iitu.diploma._preparation_;

import kz.iitu.diploma._preparation_.bean.DbWorker;
import kz.iitu.diploma._preparation_.bean.PostgresLiquibase;

public class RecreateDb {
  public static void main(String[] args) throws Exception {
    new DbWorker().recreate();
    new PostgresLiquibase().apply(false);
  }
}
