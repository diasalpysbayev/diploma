package kz.iitu.diploma.model.auth;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginRequest {
  @NotBlank(message = "Телефон не может быть пустым")
  private String phoneNumber;
  @NotBlank(message = "Пароль не может быть пустым")
  private String password;
}

