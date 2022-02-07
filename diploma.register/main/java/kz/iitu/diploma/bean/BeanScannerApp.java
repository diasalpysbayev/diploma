package kz.iitu.diploma.bean;

import kz.iitu.diploma.impl.BeanScannerRegisterImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BeanScannerRegisterImpl.class})
public class BeanScannerApp {
}
