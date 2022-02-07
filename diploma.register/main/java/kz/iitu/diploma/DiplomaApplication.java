package kz.iitu.diploma;

import kz.iitu.diploma.configuration.LiquibaseManager;
import kz.iitu.diploma.util.AppFolderPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class DiplomaApplication {

  @Autowired
  private LiquibaseManager liquibaseManager;

  public static void main(String[] args) {
    SpringApplication.run(DiplomaApplication.class, args);
    System.out.println("DIAS");
  }

  @PostConstruct
  public void listen() {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    if (!AppFolderPath.do_not_run_liquibase().toFile().exists()) {
      liquibaseManager.apply();
    }
  }
}
