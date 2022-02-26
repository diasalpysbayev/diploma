package kz.iitu.diploma.impl;

import kz.greetgo.security.password.PasswordEncoder;
import kz.iitu.diploma.dao.AuthDao;
import kz.iitu.diploma.dao.ClientDao;
import kz.iitu.diploma.exception.DuplicatePhoneException;
import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.model.auth.*;
import kz.iitu.diploma.register.AuthRegister;
import kz.iitu.diploma.util.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

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

    this.authDao.setTokenId(sessionInfo.tokenId, id, new Date());

    return sessionInfo;
  }

  private void checkPhone(ClientRegisterRecord request) {
    if (this.authDao.checkIsClientExist(request.getPhoneNumber()) != 0L) {
      throw new DuplicatePhoneException();
    }
  }
}
