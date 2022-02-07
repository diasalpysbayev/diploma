package kz.iitu.diploma.model.auth;

public class SessionInfo {
  public Long   id;
  public String name;
  public String surname;
  public String tokenId;

  public SessionInfo(String tokenId) {
    this.tokenId = tokenId;
  }

}
