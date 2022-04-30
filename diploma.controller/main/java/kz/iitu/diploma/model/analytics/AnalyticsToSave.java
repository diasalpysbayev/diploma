package kz.iitu.diploma.model.analytics;

import kz.iitu.diploma.util.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Json
public class AnalyticsToSave {

  public Long id;
  public Long queryId;
  public String valuestr;
  public String top;
  public String city;

}
