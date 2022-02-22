package kz.iitu.diploma.inservice.sms.fake;

import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.model.sms.SmsResponse;

public class SmsServiceFake implements SmsService {

  @Override
  public SmsResponse send(String recipient, String messageData) {
    return SmsResponse.of(0);
  }

}
