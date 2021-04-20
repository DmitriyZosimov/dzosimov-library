package com.epam.brest.restapp;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.epam.brest"})
@PropertySource({"classpath:dao.properties", "classpath:library.properties"})
public class ApplicationRest extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationRest.class, args);
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
      System.out.println(context.getEnvironment().getProperty("library.rest"));
      System.out.println(
          "java.runtime.version: " + context.getEnvironment().getProperty("java.runtime.version"));
    };
  }

  @Bean
  MessageSource resourceBundleMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.addBasenames("validation");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  LocalValidatorFactoryBean localValidatorFactoryBean() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(resourceBundleMessageSource());
    return bean;
  }
}
