package kz.iitu.diploma.register;

import kz.iitu.diploma.model.auth.SessionInfo;

public interface SessionRegister {
  Long getPrincipal();

  SessionInfo getAuthDetails();
}
