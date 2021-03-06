package kz.iitu.diploma.model.analytics;

import kz.iitu.diploma.util.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Json
public class AnalyticsRecord {

  public Long                 id;
  public Long                 queryId;
  public Map<String, Integer> valuestr;
  public Map<String, Integer> top;
  public Map<String, Integer> city;

}
