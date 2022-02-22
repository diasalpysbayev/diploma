package kz.iitu.diploma.model.sms;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class SmsRequest implements Supplier<String> {

  private final String apiKey;
  private final String recipient;
  private final String text;
  private final String from;

  @SneakyThrows
  @Override
  public String get() {
    StringBuilder sb =new StringBuilder();
    sb.append("apiKey=").append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));
    sb.append("&recipient=").append(URLEncoder.encode(recipient, StandardCharsets.UTF_8));
    if(text!=null) sb.append("&text=").append(URLEncoder.encode(text, StandardCharsets.UTF_8));
    if(from!=null) sb.append("&from=").append(URLEncoder.encode(from, StandardCharsets.UTF_8));

    return sb.toString();
  }

}
