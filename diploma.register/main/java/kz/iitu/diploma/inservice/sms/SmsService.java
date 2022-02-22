package kz.iitu.diploma.inservice.sms;


import kz.iitu.diploma.model.sms.SmsResponse;

public interface SmsService {

  SmsResponse send(String recipient, String messageData) throws Exception;

}
