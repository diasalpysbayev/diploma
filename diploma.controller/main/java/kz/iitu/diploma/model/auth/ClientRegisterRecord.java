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

  public String name;

  public String surname;

  public String patronymic;

  public String email;

  @NotBlank(message = "Пароль не может быть пустым")
  public String password;

  @NotBlank(message = "Пароль не может быть пустым")
  public String confirmPassword;

  public static ClientRegisterRecordBuilder builder() {
    return new ClientRegisterRecordBuilder();
  }

  public static class ClientRegisterRecordBuilder {
    ClientRegisterRecord record;

    public ClientRegisterRecordBuilder() {
      record = new ClientRegisterRecord();
    }

    public ClientRegisterRecord.ClientRegisterRecordBuilder parameters(Long id, String phoneNumber, String password,
                                                                       String confirmPassword, String firstName, String lastName) {
      this.record.id = id;
      this.record.phoneNumber = phoneNumber;
      this.record.password = password;
      this.record.confirmPassword = confirmPassword;
      this.record.name = firstName;
      this.record.surname = lastName;
      return this;
    }

    public ClientRegisterRecord build() {
      return this.record;
    }
  }

}

