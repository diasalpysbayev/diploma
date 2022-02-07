package kz.iitu.diploma.util;

import kz.iitu.diploma.DiplomaApplication;

public class ApplicationInfo {

  public static String appVersion() {
    return DiplomaApplication.class.getPackage().getImplementationVersion();
  }

}

