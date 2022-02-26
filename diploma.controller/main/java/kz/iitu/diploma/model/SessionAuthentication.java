package kz.iitu.diploma.model;

import kz.iitu.diploma.model.auth.AuthDetail;
import kz.iitu.diploma.model.auth.SessionInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SessionAuthentication extends AbstractAuthenticationToken {
  private final SessionInfo sessionResponse;

  public SessionAuthentication(SessionInfo sessionResponse) {
    super(null);
    super.setAuthenticated(true);
    super.setDetails(sessionResponse);
    this.sessionResponse = sessionResponse;
  }

  public Object getCredentials() {
    return null;
  }

  public Object getPrincipal() {
    return this.sessionResponse;
  }
}
