package kz.iitu.diploma.model.auth;


import kz.iitu.diploma.constraint.FieldMatch;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class ClientRegisterRecord {

  public Long id;

  @NotBlank(message = "Номер телефона не может быть пустым")
  public String phoneNumber;

  public String smsCode;

  @NotBlank(message = "Пароль не может быть пустым")
  public String password;

  @NotBlank(message = "Пароль не может быть пустым")
  public String confirmPassword;
}

