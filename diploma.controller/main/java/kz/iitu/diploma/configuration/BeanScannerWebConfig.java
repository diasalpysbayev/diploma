package kz.iitu.diploma.configuration;


import kz.iitu.diploma.controller.BeanScannerController;
import kz.iitu.diploma.error.BeanScannerError;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BeanScannerController.class, BeanScannerError.class})
public class BeanScannerWebConfig {
}
