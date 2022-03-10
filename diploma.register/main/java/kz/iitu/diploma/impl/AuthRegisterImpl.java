package kz.iitu.diploma.impl;

import com.google.zxing.WriterException;
import kz.greetgo.security.password.PasswordEncoder;
import kz.iitu.diploma.dao.AuthDao;
import kz.iitu.diploma.dao.ClientDao;
import kz.iitu.diploma.exception.DuplicatePhoneException;
import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.model.auth.*;
import kz.iitu.diploma.register.AuthRegister;
import kz.iitu.diploma.register.SessionRegister;
import kz.iitu.diploma.util.ContextUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static kz.iitu.diploma.util.AuthenticatorUtil.*;

@Service
public class AuthRegisterImpl implements AuthRegister {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthDao authDao;

  @Autowired
  private ClientDao clientDao;

  @Autowired
  private SmsService smsService;

  @Autowired
  private SessionRegister sessionRegister;

  @Override
  public void smsSend(String phoneNumber) {
    if (!phoneNumber.isBlank()) {
      var rnd    = new Random();
      var number = rnd.nextInt(999999);
      var code   = String.format("%06d", number);

      var smsRecord = new SmsRecord();
      smsRecord.code        = code;
      smsRecord.phoneNumber = phoneNumber;

      authDao.insertSmsCode(smsRecord);
      try {
        smsService.send(phoneNumber, "Vash code " + code);
      } catch (Exception e) {
        throw new RuntimeException("eiVaZ9RwBW :: " + e);
      }
    }
  }

  @Override
  public boolean checkPhone(String phoneNumber) {
    return authDao.checkPhone(phoneNumber);
  }

  @Override
  public boolean smsCheck(SmsRecord smsRecord) {
    return authDao.checkCode(smsRecord);
  }

  @Override
  public SessionInfo login(LoginRequest request) {
    String      encodedPassword = this.passwordEncoder.encode(request.getPassword());
    AuthDetail  authDetail      = this.getPerson(request.getPhoneNumber(), encodedPassword);
    SessionInfo session         = this.createSession(authDetail.id);

    ContextUtil.setContext(session);

    return session;
  }

  @Override
  public SessionInfo signUp(ClientRegisterRecord registerRecord) {
    checkPhone(registerRecord);
    registerRecord.id       = clientDao.nextClientId();
    registerRecord.password = passwordEncoder.encode(registerRecord.password);
    authDao.createClient(registerRecord);
    //todo save smsCode in terms of use and add система штрафов
    authDao.setSecretKeyId(generateSecretKey(), registerRecord.id);

    SessionInfo session = createSession(registerRecord.id);

    ContextUtil.setContext(session);
    return session;
  }

  @Override
  public SessionInfo getAuthDetailsByToken(String ggToken) {
    return authDao.getAuthDetailsByToken(ggToken);
  }

  @NotNull
  private AuthDetail getPerson(String phone, String encodedPassword) {
    return this.authDao.getClientByPhoneAndPassword(phone, encodedPassword);
  }

  private SessionInfo createSession(Long id) {
    SessionInfo sessionInfo = this.authDao.getClientById(id);

    sessionInfo.tokenId = UUID.randomUUID() + "-" + UUID.randomUUID();

    authDao.setTokenId(sessionInfo.tokenId, id);

    return sessionInfo;
  }

  private void checkPhone(ClientRegisterRecord request) {
    if (authDao.checkIsClientExist(request.getPhoneNumber()) != 0L) {
      throw new DuplicatePhoneException();
    }
  }

  @Override
  public void createQRCode() {
    String secretKey   = clientDao.getSecretKeyId(sessionRegister.getPrincipal());
    String email       = clientDao.getEmail(sessionRegister.getPrincipal());
    String companyName = "Diploma";
    String barCodeUrl  = getGoogleAuthenticatorBarCode(secretKey, email, companyName);

    try {
      //todo return as file
      generateQRCode(barCodeUrl);
    } catch (WriterException | IOException e) {
      throw new RuntimeException("k6c0x0K9VZ :: ", e);
    }
  }

  @Override
  public boolean checkTotp(String code) {
    var id        = sessionRegister.getPrincipal();
    var secretKey = clientDao.getSecretKeyId(id);
    return code.equals(getTOTPCode(secretKey));
  }

  public static void main(String[] args) {
//    var a = generateSecretKey();
//    String secretKey   = a;
//    System.out.println(a);
//    String email       = "John";
//    String companyName = "Diploma";
//    String barCodeUrl  = getGoogleAuthenticatorBarCode(secretKey, email, companyName);
//    try {
//      //todo return as file
//      generateQRCode(barCodeUrl);
//
//    } catch (WriterException | IOException e) {
//      throw new RuntimeException("k6c0x0K9VZ :: ", e);
//    }

    String secretKey = "WSI3KBMBKBG7ZI2L6YQUAMAEXW3T3Z3E";
    String lastCode = null;
    while (true) {
      String code = getTOTPCode(secretKey);
      if (!code.equals(lastCode)) {
        System.out.println(code);
      }
      lastCode = code;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {};
    }
  }
}
