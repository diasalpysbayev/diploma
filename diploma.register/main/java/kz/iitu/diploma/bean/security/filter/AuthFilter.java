package kz.iitu.diploma.bean.security.filter;

import com.google.common.base.Strings;
import kz.iitu.diploma.model.auth.SessionInfo;
import kz.iitu.diploma.register.AuthRegister;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static kz.iitu.diploma.util.ContextUtil.setContext;

@RequiredArgsConstructor
public class AuthFilter implements Filter {

  public static final String GG_TOKEN = "tokenId";//All our projects, using gg_token
  private final AuthRegister authRegister;

  @Override
  @SneakyThrows
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

    HttpServletRequest  httpRequest  = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String ggToken = httpRequest.getHeader(GG_TOKEN);

    if (!Strings.isNullOrEmpty(ggToken)) {
      SessionInfo authDetails = authRegister.getAuthDetailsByToken(ggToken);

      if (authDetails == null || authDetails.id == null) {
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType("application/json");

        httpResponse.getWriter().write("no_session");
        return;
      }

      setContext(authDetails);

      chain.doFilter(request, response);
      return;
    }

    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpResponse.setContentType("application/json");

    httpResponse.getWriter().write("no_session");
  }

}

