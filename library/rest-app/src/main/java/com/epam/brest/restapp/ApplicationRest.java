package com.epam.brest.restapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.epam.brest"})
@PropertySource({"classpath:dao.properties"})
public class ApplicationRest extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRest.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context){
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = context.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}
