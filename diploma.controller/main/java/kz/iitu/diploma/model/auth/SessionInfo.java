package kz.iitu.diploma.model.auth;

import lombok.ToString;

@ToString
public class SessionInfo {
  public Long   id;
  public String name;
  public String surname;
  public String tokenId;
  public String phoneNumber;

  public SessionInfo(String tokenId) {
    this.tokenId = tokenId;
  }

}
