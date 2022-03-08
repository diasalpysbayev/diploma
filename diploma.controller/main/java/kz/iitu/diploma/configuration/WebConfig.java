package kz.iitu.diploma.configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import kz.iitu.diploma.util.WebFormatters;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerCustomizer() {
    return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setForceEncoding(true);
    characterEncodingFilter.setEncoding("UTF-8");

    var registrationBean = new FilterRegistrationBean<CharacterEncodingFilter>();
    registrationBean.setFilter(characterEncodingFilter);
    return registrationBean;

  }

  @Bean
  public StringHttpMessageConverter stringHttpMessageConverter() {
    return new StringHttpMessageConverter(StandardCharsets.UTF_8);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOriginPatterns("*").allowedHeaders("*").allowCredentials(true);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/swagger", "swagger-ui.html");
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    var newList = converters.stream()
        .filter(x -> !(x instanceof StringHttpMessageConverter))
        .collect(Collectors.toList());

    newList.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

    converters.clear();
    converters.addAll(newList);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    WebFormatters.registerJsonFormatter(registry);
    WebFormatters.registerDateFormatter(registry);
    WebFormatters.registerListObjectConverter(registry);
  }

}
