package kz.iitu.diploma.config;

import kz.greetgo.conf.hot.*;

@Description("Scheduler Configuration")
public interface SmsServiceConfig {

  @Description("Использование фэйка вместо реального сервиса SmsRequest")
  @DefaultBoolValue(true)
  boolean useFake();

  @Description("url для mobizon")
  @DefaultStrValue("http://api.mobizon.kz/service/message/sendsmsmessage")
  String host();

  @Description("Ключ API")
  @DefaultStrValue("kz3fb20d646b2e6ead73e46ff3ad366871b9bca6de1a9c473c9ada06ea00865a1ecfc9")
  String apiKey();

  @Description("Подпись отправителя")
  @DefaultStrValue("")
  String from();

  @DefaultIntValue(120)
  @FirstReadEnv("PHONE_NUMBER_CONFIRMATION_CODE_LIFETIME")
  int codeLifetime();

}
