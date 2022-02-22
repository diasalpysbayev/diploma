package kz.iitu.diploma.model.sms;

import lombok.ToString;

@ToString
public class SmsResponse {

  public int code;
  public SmsData data;
  public String message;

  public static SmsResponse of(int code){
    SmsResponse ret = new SmsResponse();
    ret.code = code;
    return ret;
  }
}
