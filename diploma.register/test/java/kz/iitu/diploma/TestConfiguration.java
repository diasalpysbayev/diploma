package kz.iitu.diploma;

import kz.iitu.diploma.bean.BeanScannerApp;
import kz.iitu.diploma.configuration.BeanScannerWebConfig;
import kz.iitu.diploma.tesbean.ScannerForTests;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@MapperScan(basePackages = {"kz.iitu.diploma.dao"})
@ComponentScan(basePackages = {"kz.iitu.diploma.bean"})
@Import({BeanScannerWebConfig.class, JdbcTemplateAutoConfiguration.class, ScannerForTests.class, BeanScannerApp.class})
public class TestConfiguration {
}
