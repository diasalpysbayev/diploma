package kz.iitu.diploma.impl;

import kz.iitu.diploma.bean.*;
import kz.iitu.diploma.tesbean.MyBatisTestConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(classes = {
    TestConfiguration.class,
    DataSourceConfiguration.class,
    AllConfigFactory.class,
    MyBatisConfiguration.class,
    MyBatisTestConfiguration.class,
    ObjectMapperConfiguration.class,
    OkHttpClientConfiguration.class,
    BeanScannerApp.class
})
public abstract class AbstractTestParent extends AbstractTestNGSpringContextTests {
}

