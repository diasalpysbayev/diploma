package kz.iitu.diploma.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryDetail {

  public String     name;
  public Boolean    isVideo;
  public BigDecimal latitude;
  public BigDecimal longitude;

}
