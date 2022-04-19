package kz.iitu.diploma.inservice.sms.real;

import kz.iitu.diploma.inservice.sms.SmsService;
import kz.iitu.diploma.model.sms.SmsRequest;
import kz.iitu.diploma.model.sms.SmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class SmsServiceReal implements SmsService {

  private final String host;
  private final String apiKey;
  private final String from;

  private RestTemplate restTemplate;

  public static void main(String[] args) throws Exception {
    SmsServiceReal smsService = new SmsServiceReal("http://api.mobizon.kz/service/message/sendsmsmessage",
        "kz3fb20d646b2e6ead73e46ff3ad366871b9bca6de1a9c473c9ada06ea00865a1ecfc9",
        null);
    RestTemplate restTemplate = new RestTemplate();
    smsService.setRestTemplate(restTemplate);

    SmsResponse smsResponse = smsService.send("77015745713", "Vash code");
    System.out.println(smsResponse.toString());
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    List<HttpMessageConverter<?>>       messageConverters = new ArrayList<>();
    MappingJackson2HttpMessageConverter converter         = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
    messageConverters.add(converter);
    this.restTemplate.setMessageConverters(messageConverters);
  }

  @Override
  public SmsResponse send(String recipient, String text) throws Exception {
    SmsRequest smsRequest = new SmsRequest(apiKey, recipient, text, null);
    return restTemplate.getForEntity(new URI(host + "?" + smsRequest.get()), SmsResponse.class).getBody();
  }

}
