package kz.iitu.diploma._preparation_;

import kz.iitu.diploma._preparation_.beans.DbParams;
import kz.iitu.diploma._preparation_.beans.DbWorker;

public class UpgradeDb {

  public static void main(String[] args) throws Exception {
    new DbWorker().runLiquibase(DbParams.readParams());
  }

}
