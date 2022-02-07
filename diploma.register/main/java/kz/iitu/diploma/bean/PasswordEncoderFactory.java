package kz.iitu.diploma.bean;

import static kz.greetgo.security.SecurityBuilders.newPasswordEncoderBuilder;

import kz.greetgo.security.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncoderFactory {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return newPasswordEncoderBuilder().setSalt("73!KXd&eyIainU@^WXq!^5&gWopzB7F8").build();
  }

}
