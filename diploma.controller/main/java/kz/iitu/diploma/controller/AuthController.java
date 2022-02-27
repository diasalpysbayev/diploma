package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.auth.ClientRegisterRecord;
import kz.iitu.diploma.model.auth.LoginRequest;
import kz.iitu.diploma.model.auth.SessionInfo;
import kz.iitu.diploma.model.auth.SmsRecord;
import kz.iitu.diploma.register.AuthRegister;
import kz.iitu.diploma.util.Json;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthRegister authRegister;

  @GetMapping("/check-phone")
  public boolean checkPhoneOnExist(@RequestParam(value = "phoneNumber") String phoneNumber) {
    return authRegister.checkPhone(phoneNumber);
  }

  @GetMapping("/sms-send")
  public void sendSms(@RequestParam(value = "phoneNumber") String phoneNumber) {
    authRegister.smsSend(phoneNumber);
  }

  @PostMapping("/sms-check")
  public boolean checkSms(@RequestBody @Json SmsRecord smsRecord) {
    return authRegister.smsCheck(smsRecord);
  }

  @PostMapping("/sign-up")
  public SessionInfo signUp(@Valid @RequestBody ClientRegisterRecord clientRegister) {
    return authRegister.signUp(clientRegister);
  }

  @SneakyThrows
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authRegister.login(loginRequest));
  }

  @GetMapping("/check-totp")
  public boolean totp(@RequestParam("code") String code) {
    return authRegister.checkTotp(code);
  }


}
