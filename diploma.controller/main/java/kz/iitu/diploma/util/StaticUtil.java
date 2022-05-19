package kz.iitu.diploma.util;

import kz.iitu.diploma.model.query.QueryDetail;

public class StaticUtil {
  public static QueryDetail initQueryDetail() {
    return QueryDetail.builder().isVideo(false).name("Криштиану роналду").build();
  }
}
