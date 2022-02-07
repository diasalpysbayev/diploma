package kz.iitu.diploma.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
public class AuthDetail {
  public Long       id;
  public String     phone;

  public AuthDetail(Long id, String phone) {
    this.id    = id;
    this.phone = phone;
  }

}
