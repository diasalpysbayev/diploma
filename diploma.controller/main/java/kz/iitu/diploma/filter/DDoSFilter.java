package kz.iitu.diploma.filter;

import kz.iitu.diploma.logging.LOG;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class DDoSFilter implements Filter {

  private static final IpDDoSChecker ipDdosChecker = new IpDDoSChecker();


  protected LOG getLogger() {
    return LOG.byName("SERVICE.FILTER");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    String clientIpAddress = getClientIpAddress((HttpServletRequest) request);

    if (ipDdosChecker.checkFastIp(clientIpAddress)) {
      throw new RuntimeException("Many attempts to login from one ip");
    }

    chain.doFilter(request, response);

  }

  private static final String[] IP_HEADER_CANDIDATES = {
    "X-Forwarded-For",
    "X-FORWARDED-FOR",
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "HTTP_VIA",
    "X-Real-IP",
    "REMOTE_ADDR"};


  public String getClientIpAddress(HttpServletRequest request) {

    for (String header : IP_HEADER_CANDIDATES) {
      String ip = request.getHeader(header);
      if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
        getLogger().trace(() -> String.format("IP_HEADER_CANDIDATES: %s, ip: %s", header, ip));
        return ip;
      }
    }
    getLogger().trace(() -> String.format("getRemoteAddr ip: %s", request.getRemoteAddr()));

    return request.getRemoteAddr();
  }

}