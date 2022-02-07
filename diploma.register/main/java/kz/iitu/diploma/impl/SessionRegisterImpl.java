package kz.iitu.diploma.impl;


import kz.iitu.diploma.model.auth.AuthDetail;
import kz.iitu.diploma.register.SessionRegister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionRegisterImpl implements SessionRegister {

  @Override
  public Long getPrincipal() {
    AuthDetail authDetails = getAuthDetails();

    if (authDetails != null) return authDetails.id;

    return null;
  }

  @Override
  public AuthDetail getAuthDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) return null;
    Object principal = authentication.getPrincipal();

    if (principal instanceof AuthDetail) {
      return (AuthDetail) principal;
    }

    return null;
  }
}

