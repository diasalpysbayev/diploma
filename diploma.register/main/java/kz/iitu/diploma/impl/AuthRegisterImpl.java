package kz.iitu.diploma.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import kz.greetgo.security.password.PasswordEncoder;
import kz.iitu.diploma.dao.AuthDao;
import kz.iitu.diploma.dao.ClientDao;
import kz.iitu.diploma.exception.DuplicatePhoneException;
import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.model.auth.*;
import kz.iitu.diploma.model.file.FileInfo;
import kz.iitu.diploma.register.AuthRegister;
import kz.iitu.diploma.register.FileRegister;
import kz.iitu.diploma.register.ServerSendRegister;
import kz.iitu.diploma.register.SessionRegister;
import kz.iitu.diploma.util.ContextUtil;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static kz.iitu.diploma.util.AuthenticatorUtil.*;

@Service
public class AuthRegisterImpl implements AuthRegister {

  private static final Logger             log = LogManager.getLogger(AuthRegisterImpl.class);
  @Autowired
  private              PasswordEncoder    passwordEncoder;
  @Autowired
  private              AuthDao            authDao;
  @Autowired
  private              ClientDao          clientDao;
  @Autowired
  private              SmsService         smsService;
  @Autowired
  private              SessionRegister    sessionRegister;
  @Autowired
  private              FileRegister       fileRegister;
  @Autowired
  private              ServerSendRegister serverSendRegister;

  @SneakyThrows
  public static void main(String[] args) {

  }

  @Override
  public boolean smsSend(String phoneNumber) {
    if (!phoneNumber.isBlank()) {
      if (!authDao.checkNumberOfAttempts(phoneNumber)) {
        return false;
      }
      var rnd    = new Random();
      var number = rnd.nextInt(999999);
      var code   = String.format("%06d", number);

      var smsRecord = new SmsRecord();
      smsRecord.code        = code;
      smsRecord.phoneNumber = phoneNumber;

      authDao.insertSmsCode(smsRecord);
      try {
        smsService.send(phoneNumber, "Vash code " + code);
        log.info("RjEoqi2Zhm :: SmsRecord = " + smsRecord);
      } catch (Exception e) {
        log.error("eiVaZ9RwBW :: Exception with sms sending = " + e);
        throw new RuntimeException("eiVaZ9RwBW :: " + e);
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean checkPhone(String phoneNumber) {
    return authDao.checkPhone(phoneNumber);
  }

  @Override
  public boolean smsCheck(SmsRecord smsRecord) {
    if (!authDao.checkNumberOfAttempts(smsRecord.phoneNumber)) {
      return false;
    }
    return authDao.checkCode(smsRecord);
  }

  @Override
  public SessionInfo login(LoginRequest request) {
    SessionInfo sessionInfo = this.getPerson(request.getPhoneNumber());
    sessionInfo.tokenId = UUID.randomUUID() + "-" + UUID.randomUUID();

    ContextUtil.setContext(sessionInfo);

    if (authDao.checkSessionSingularity(sessionInfo.id)) {
      authDao.updateOldSessions(sessionInfo.id);
      serverSendRegister.emitEvent(sessionInfo.id);
    }

    authDao.setTokenId(sessionInfo.tokenId, sessionInfo.id);

    log.info("17YMnqh7aq :: Session = " + sessionInfo);

    return sessionInfo;
  }

  @Override
  public SessionInfo signUp(ClientRegisterRecord registerRecord) {
    checkPhone(registerRecord);
    registerRecord.id       = clientDao.nextClientId();
    registerRecord.password = passwordEncoder.encode(registerRecord.password);
    authDao.createClient(registerRecord);
    //todo save smsCode in terms of use and add система штрафов
    authDao.setSecretKeyId(generateSecretKey(), registerRecord.id);

    SessionInfo sessionInfo = this.getPerson(registerRecord.phoneNumber);
    sessionInfo.tokenId = UUID.randomUUID() + "-" + UUID.randomUUID();
    ContextUtil.setContext(sessionInfo);

    log.info("v6UJ0I0r8e :: Session = " + sessionInfo);

    return sessionInfo;
  }

  @Override
  public SessionInfo getAuthDetailsByToken(String ggToken) {
    return authDao.getAuthDetailsByToken(ggToken);
  }

  @NotNull
  private SessionInfo getPerson(String phone) {
    var sessionInfo = this.authDao.getClientByPhoneAndPassword(phone);
    log.info("u3DLlHbXM6 :: Session = " + sessionInfo);
    return sessionInfo;
  }

  private void checkPhone(ClientRegisterRecord request) {
    if (authDao.checkIsClientExist(request.getPhoneNumber()) != 0L) {
      log.error("4n2dhiJ0zw :: DuplicateError");
      throw new DuplicatePhoneException();
    }
  }

  @Override
  public String createQRCode(String phoneNumber) {
    String secretKey   = clientDao.getSecretKeyId(phoneNumber);
    String email       = clientDao.getEmail(sessionRegister.getPrincipal());
    String companyName = "Diploma";
    String barCodeUrl  = getGoogleAuthenticatorBarCode(secretKey, email, companyName);

    try {
      int width  = 115;
      int height = 115;

      BitMatrix matrix = new MultiFormatWriter().encode(barCodeUrl, BarcodeFormat.QR_CODE, width, height);

      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        MatrixToImageWriter.writeToStream(matrix, "png", out);
        FileInfo fileInfo = new FileInfo();
        fileInfo.name       = new Date().getTime() + ".png";
        fileInfo.mimeType   = "image/png";
        fileInfo.size       = Long.parseLong(String.valueOf(out.size()));
        fileInfo.base64data = Base64.getEncoder().encodeToString(out.toByteArray());

        log.info("379KtYBYmT :: FileInfo = " + fileInfo);

        var fileId = fileRegister.save(fileInfo);

        log.info("s27211E2rd :: fileId = " + fileId);

        return fileId;
      }
    } catch (WriterException | IOException e) {
      throw new RuntimeException("k6c0x0K9VZ :: ", e);
    }
  }

  @Override
  public SessionInfo checkTotp(String code, String phoneNumber) {
    var secretKey = clientDao.getSecretKeyId(phoneNumber);

    if (!code.equals(getTOTPCode(secretKey))) {
      return null;
    }

    SessionInfo sessionInfo = this.getPerson(phoneNumber);
    sessionInfo.tokenId = UUID.randomUUID() + "-" + UUID.randomUUID();

    ContextUtil.setContext(sessionInfo);

    log.info("o1e5hfgH8V :: session id = " + (sessionInfo.id));
    log.info("vzkhge9QsT :: checkSessionSingularity = " + authDao.checkSessionSingularity(sessionInfo.id));
    if (authDao.checkSessionSingularity(sessionInfo.id)) {
      authDao.updateOldSessions(sessionInfo.id);
      //      log.info("o4OQn1C77k :: Emitters = " + ServerSendEmitter.getEmitters().get(sessionInfo.id));
      serverSendRegister.emitEvent(sessionInfo.id);
    }

    authDao.setTokenId(sessionInfo.tokenId, sessionInfo.id);

    //    log.info("XN6P7xI656 :: Session Info = " + sessionInfo);
    return sessionInfo;
  }

  @Override
  public void updateDate(ClientRegisterRecord registerRecord) {
    authDao.updateDate(registerRecord);
  }

  @Override
  public UserInfo getUserInfo(String tokenId) {
    return clientDao.getUserInfo(tokenId);
  }

}
