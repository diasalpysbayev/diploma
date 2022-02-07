package kz.iitu.diploma.tesbean;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("kz.iitu.diploma.dao")
public class MyBatisTestConfiguration {
}

