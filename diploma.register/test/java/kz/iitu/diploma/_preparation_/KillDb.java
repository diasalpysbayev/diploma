package kz.iitu.diploma._preparation_;

import kz.iitu.diploma._preparation_.beans.DbWorker;

/**
 * <p>
 * see --> Удаление базы
 * </p>
 */
public class KillDb {

  public static void main(String[] args) throws Exception {
    new DbWorker().kill();
  }

}
