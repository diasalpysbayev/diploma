package kz.iitu.diploma.util;

import kz.iitu.diploma.model.SessionAuthentication;
import kz.iitu.diploma.model.auth.SessionInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtil {

  public static SecurityContext getContext() {
    return SecurityContextHolder.getContext();
  }

  public static void setContext(SessionInfo authDetails) {
    SessionAuthentication authReq = new SessionAuthentication(authDetails);
    SecurityContext       sc      = getContext();
    sc.setAuthentication(authReq);
  }
}
