package com.epam.brest.webapp.config;

import com.epam.brest.service.BookService;
import com.epam.brest.service.LoginService;
import com.epam.brest.service.ReaderService;
import com.epam.brest.service.SearchReaderValidator;
import com.epam.brest.service.rest.BookServiceRest;
import com.epam.brest.service.rest.LoginServiceRest;
import com.epam.brest.service.rest.ReaderServiceRest;
import com.epam.brest.service.rest.SearchReaderValidatorRest;
import com.epam.brest.webapp.interceptor.LibraryCardInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@PropertySource({"classpath:library.properties"})
public class ApplicationConfig implements WebMvcConfigurer {

  @Value("${rest.server.protocol}")
  private String protocol;
  @Value("${rest.server.host}")
  private String host;
  @Value("${rest.server.port}")
  private String port;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate(new SimpleClientHttpRequestFactory());
  }

  @Bean
  BookService bookService() {
    String url = String.format("%s://%s:%s/book", protocol, host, port);
    return new BookServiceRest(url, restTemplate());
  }

  @Bean
  ReaderService readerService() {
    String url = String.format("%s://%s:%s/reader", protocol, host, port);
    return new ReaderServiceRest(url, restTemplate());
  }

  @Bean
  LoginService loginService() {
    String url = String.format("%s://%s:%s/login", protocol, host, port);
    return new LoginServiceRest(url, restTemplate());
  }

  @Bean
  LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    return localeResolver;
  }

  @Bean
  MessageSource resourceBundleMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lan");
    return localeChangeInterceptor;
  }

  @Bean
  SearchReaderValidator searchReaderValidator() {
    return new SearchReaderValidatorRest();
  }

  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(localeChangeInterceptor());

    registry.addInterceptor(new LibraryCardInterceptor())
        .addPathPatterns("/profile")
        .addPathPatterns("/profile/**")
        .addPathPatterns("/catalog/select/*");
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext context) {
    return args -> {
      System.out.println("Let's inspect the beans provided by Spring Boot:");
      String[] beanNames = context.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }
      System.out.println(context.getEnvironment().getProperty("library.web"));
      System.out.println(
          "java.runtime.version: " + context.getEnvironment().getProperty("java.runtime.version"));
    };
  }
}
