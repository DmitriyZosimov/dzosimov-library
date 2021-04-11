package com.epam.brest.webapp.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomControllerExceptionHandler {

    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView handlerHttpClientErrorException(HttpClientErrorException ex, HttpServletResponse response,
                                                        HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        response.setStatus(ex.getStatusCode().value());
        String message = messageSource.getMessage("error." + ex.getStatusCode().value(), null,
                localeResolver.resolveLocale(request));
        mav.addObject("code", ex.getStatusCode());
        mav.addObject("message", message);
        mav.setViewName("error");
        return mav;
    }
}
