package kz.iitu.diploma.model.query;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class QueryDetail {

  public String     name;
  public Boolean    isVideo;
  public BigDecimal latitude;
  public BigDecimal longitude;

}
