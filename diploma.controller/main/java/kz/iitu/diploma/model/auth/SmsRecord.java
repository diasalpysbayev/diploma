package kz.iitu.diploma.model.auth;

import lombok.ToString;

@ToString
public class SmsRecord {
  public String phoneNumber;
  public String code;
}
