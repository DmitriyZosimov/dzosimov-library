package com.epam.brest.webapp.config;

import com.epam.brest.service.IBookService;
import com.epam.brest.service.ILoginService;
import com.epam.brest.service.IReaderService;
import com.epam.brest.service.rest.BookServiceRest;
import com.epam.brest.service.rest.LoginServiceRest;
import com.epam.brest.service.rest.ReaderServiceRest;
import com.epam.brest.webapp.interceptor.LibraryCardInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private String port;

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    IBookService bookService(){
        String url = String.format("%s://%s:%s/book", protocol, host, port);
        return new BookServiceRest(url, restTemplate());
    }

    @Bean
    IReaderService readerService(){
        String url = String.format("%s://%s:%s/reader", protocol, host, port);
        return new ReaderServiceRest(url, restTemplate());
    }

    @Bean
    ILoginService loginService(){
        String url = String.format("%s://%s:%s/login", protocol, host, port);
        return new LoginServiceRest(url, restTemplate());
    }

    @Bean
    SessionLocaleResolver getSessionLocaleResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    MessageSource getResourceBundleMessageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("messages");
        messageSource.addBasenames("validation");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lan");
        registry.addInterceptor(localeChangeInterceptor).addPathPatterns("/**");

        registry.addInterceptor(new LibraryCardInterceptor())
                .addPathPatterns("/profile")
                .addPathPatterns("/profile/**")
                .addPathPatterns("/catalog/select/*");
    }
}
