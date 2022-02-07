package kz.iitu.diploma.bean;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {


  @Bean
  public DataSource dataSource() {
    HikariDataSource pool = new HikariDataSource();

    pool.setDriverClassName("org.postgresql.Driver");
    pool.setJdbcUrl("jdbc:postgresql://localhost:5432/diploma");
    pool.setUsername("postgres");
    pool.setPassword("postgres");

    pool.setMinimumIdle(5);

    return pool;
  }

  @Primary
  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(dataSource());
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    {
      org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.setDefaultFetchSize(100);
      configuration.setDefaultStatementTimeout(30);
      sessionFactory.setConfiguration(configuration);
    }
    return sessionFactory.getObject();
  }

}

