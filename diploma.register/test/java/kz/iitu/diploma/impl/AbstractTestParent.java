package kz.iitu.diploma.impl;

import kz.iitu.diploma.TestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public abstract class AbstractTestParent {
}

