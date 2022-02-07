package kz.iitu.diploma.register;

import kz.iitu.diploma.model.auth.AuthDetail;

public interface SessionRegister {
  Long getPrincipal();

  AuthDetail getAuthDetails();
}
