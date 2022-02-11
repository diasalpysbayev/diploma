package kz.iitu.diploma.impl;

import kz.greetgo.security.password.PasswordEncoder;
import kz.iitu.diploma.dao.AuthDao;
import kz.iitu.diploma.exception.DuplicatePhoneException;
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

  @Override
  public void smsSend(String phone) {
    if (!phone.isBlank()) {
      var rnd    = new Random();
      var number = rnd.nextInt(999999);
      var code   = String.format("%06d", number);

      var smsRecord = SmsRecord.builder()
          .code(code)
          .phoneNumber(phone)
          .build();

      authDao.insertSmsCode(smsRecord);
    }
  }

  @Override
  public boolean checkPhone(String phone) {
    return false;
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

    ContextUtil.setContext(authDetail);

    return session;
  }

  @Override
  public SessionInfo signUp(ClientRegisterRecord registerRecord) {
    this.checkPhone(registerRecord);
    registerRecord.password = this.passwordEncoder.encode(registerRecord.password);
    this.authDao.createClient(registerRecord);

    AuthDetail  authDetail = new AuthDetail(registerRecord.id, registerRecord.phoneNumber);
    SessionInfo session    = this.createSession(registerRecord.id);

    ContextUtil.setContext(authDetail);
    return session;
  }

  @Override
  public AuthDetail getAuthDetailsByToken(String ggToken) {
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
