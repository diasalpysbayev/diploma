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
import kz.iitu.diploma.register.SessionRegister;
import kz.iitu.diploma.util.ContextUtil;
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

  private static final Logger          log = LogManager.getLogger(AuthRegisterImpl.class);
  @Autowired
  private              PasswordEncoder passwordEncoder;
  @Autowired
  private              AuthDao         authDao;
  @Autowired
  private              ClientDao       clientDao;
  @Autowired
  private              SmsService      smsService;
  @Autowired
  private              SessionRegister sessionRegister;
  @Autowired
  private              FileRegister    fileRegister;

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

//    String secretKey = "WSI3KBMBKBG7ZI2L6YQUAMAEXW3T3Z3E";
//    String lastCode  = null;
//    while (true) {
//      String code = getTOTPCode(secretKey);
//      if (!code.equals(lastCode)) {
//        System.out.println(code);
//      }
//      lastCode = code;
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//      }
//    }

    int[] nums = {2,5,5,11};
    int target = 10;

    int left = 0;
    int right = nums.length - 1;

    int[] output = new int[2];

    while (true) {
      if (nums[left] + nums[right] == target) {
        output[0] = left;
        output[1] = right;
        System.out.println(output[0] + " " + output[1]);
        break;
      } else if (right > 1 && left == 0){
        right--;
      } else if (left < nums.length - 2) {
        right = nums.length - 1;
        left++;
      }
    }
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
    SessionInfo sessionInfo     = this.getPerson(request.getPhoneNumber());
    sessionInfo.tokenId = UUID.randomUUID() + "-" + UUID.randomUUID();

    authDao.setTokenId(sessionInfo.tokenId, sessionInfo.id);

    ContextUtil.setContext(sessionInfo);

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

    SessionInfo sessionInfo     = this.getPerson(registerRecord.phoneNumber);
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
        fileInfo.name       = new Date().toString() + ".png";
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

    return this.authDao.getClientByPhoneAndPassword(phoneNumber);
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
