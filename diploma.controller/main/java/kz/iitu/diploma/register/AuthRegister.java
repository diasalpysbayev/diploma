package kz.iitu.diploma.register;

import kz.iitu.diploma.model.auth.*;
import org.springframework.http.ResponseEntity;

public interface AuthRegister {

  boolean smsSend(String phone);

  boolean checkPhone(String phoneNumber);

  boolean smsCheck(SmsRecord smsRegisterRecord);

  SessionInfo signUp(ClientRegisterRecord registerRecord);

  SessionInfo getAuthDetailsByToken(String ggToken);

  String createQRCode(String phoneNumber);

  ResponseEntity<?> checkTotp(String code, String phoneNumber);

  void updateDate(ClientRegisterRecord registerRecord);

  UserInfo getUserInfo(String tokenId);
}
