package kz.iitu.diploma.model.auth;

import lombok.ToString;

@ToString
public class UserInfo {
  public Long   id;
  public String name;
  public String surname;
  public String phoneNumber;
  public String email;
}
