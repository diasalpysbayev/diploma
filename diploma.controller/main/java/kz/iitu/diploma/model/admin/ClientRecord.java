package kz.iitu.diploma.model.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientRecord {

  public Long   id;
  public String name;
  public String surname;
  public String patronymic;
  public String email;
  public String phoneNumber;

}
