package kz.iitu.diploma.impl;


import kz.iitu.diploma.model.auth.SessionInfo;
import kz.iitu.diploma.register.SessionRegister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionRegisterImpl implements SessionRegister {

  @Override
  public Long getPrincipal() {
    SessionInfo authDetails = getAuthDetails();

    if (authDetails != null) return authDetails.id;

    return null;
  }

  @Override
  public SessionInfo getAuthDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) return null;
    Object principal = authentication.getPrincipal();

    if (principal instanceof SessionInfo) {
      return (SessionInfo) principal;
    }

    return null;
  }
}

