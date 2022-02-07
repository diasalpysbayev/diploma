package kz.iitu.diploma.model.auth;

import kz.iitu.diploma.util.Json;
import lombok.Builder;
import lombok.Data;

@Data
@Json
@Builder
public class SmsRecord {
  public String phoneNumber;
  public String code;
}
