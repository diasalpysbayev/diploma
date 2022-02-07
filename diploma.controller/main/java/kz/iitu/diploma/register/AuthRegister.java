package kz.iitu.diploma.register;

import kz.iitu.diploma.model.auth.*;

public interface AuthRegister {
  void smsSend(String phone);

  boolean checkPhone(String phone);

  boolean smsCheck(SmsRecord smsRegisterRecord);

  SessionInfo login(LoginRequest loginRequest) throws Exception;

  SessionInfo signUp(ClientRegisterRecord registerRecord);

  AuthDetail getAuthDetailsByToken(String ggToken);
}
