package kz.iitu.diploma.model;

import kz.iitu.diploma.model.auth.AuthDetail;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SessionAuthentication extends AbstractAuthenticationToken {
  private final AuthDetail authDetails;

  public SessionAuthentication(AuthDetail sessionResponse) {
    super(null);
    super.setAuthenticated(true);
    super.setDetails(sessionResponse);
    this.authDetails = sessionResponse;
  }

  public Object getCredentials() {
    return null;
  }

  public Object getPrincipal() {
    return this.authDetails;
  }
}
