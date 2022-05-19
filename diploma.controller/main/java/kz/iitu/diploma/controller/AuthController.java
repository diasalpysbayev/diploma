package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.auth.*;
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

  @PostMapping("/check-phone")
  public boolean checkPhoneOnExist(@RequestParam(value = "phoneNumber") String phoneNumber) {
    return authRegister.checkPhone(phoneNumber);
  }

  @PostMapping("/sms-send")
  public ResponseEntity<String> sendSms(@RequestParam(value = "phoneNumber") String phoneNumber) {
    if (authRegister.smsSend(phoneNumber)) {
      return ResponseEntity.ok("Success");
    }
    return ResponseEntity.status(400).body("Too many attempts");
  }

  @PostMapping("/sms-check")
  public boolean checkSms(@RequestBody @Json SmsRecord smsRecord) {
    return authRegister.smsCheck(smsRecord);
  }

  @PostMapping("/sign-up")
  public SessionInfo signUp(@Valid @RequestBody ClientRegisterRecord clientRegister) {
    return authRegister.signUp(clientRegister);
  }

  @PostMapping("/get-qr")
  public String getQrCode(@Valid @RequestBody LoginRequest loginRequest) {
    return authRegister.createQRCode(loginRequest.getPhoneNumber());
  }

  @PostMapping("/check-totp")
  public ResponseEntity<?> totp(@RequestParam("code") String code, @RequestParam("phoneNumber") String phoneNumber) {
    return authRegister.checkTotp(code, phoneNumber);
  }

  @PostMapping("/recover")
  public void recover(@RequestBody ClientRegisterRecord clientRegister) {
    authRegister.updateDate(clientRegister);
  }

  @GetMapping("/get-info")
  public UserInfo getInfo(@RequestParam(value = "tokenId") String tokenId) {
    return authRegister.getUserInfo(tokenId);
  }

  @PostMapping("/recover-password")
  public void recover(@RequestParam(value = "phoneNumber") String phoneNumber, @RequestParam(value = "password") String password) {
    authRegister.recovery(phoneNumber, password);
  }

}
