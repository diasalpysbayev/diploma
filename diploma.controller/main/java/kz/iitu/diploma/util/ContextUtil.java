package kz.iitu.diploma.util;

import kz.iitu.diploma.model.SessionAuthentication;
import kz.iitu.diploma.model.auth.AuthDetail;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtil {
  public ContextUtil() {
  }

  public static SecurityContext getContext() {
    return SecurityContextHolder.getContext();
  }

  public static void setContext(AuthDetail authDetails) {
    var authReq = new SessionAuthentication(authDetails);
    var sc      = getContext();
    sc.setAuthentication(authReq);
  }
}
