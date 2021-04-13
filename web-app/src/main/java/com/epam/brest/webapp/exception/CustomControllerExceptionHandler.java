package com.epam.brest.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomControllerExceptionHandler.class);

    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    public CustomControllerExceptionHandler(LocaleResolver localeResolver, MessageSource messageSource) {
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView handlerHttpClientErrorException(HttpClientErrorException ex, HttpServletResponse response,
                                                        HttpServletRequest request){
        LOGGER.warn("handlerHttpClientErrorException, message: {}", ex.getMessage());
        HttpStatus status = ex.getStatusCode();
        ModelAndView mav = new ModelAndView();
        response.setStatus(status.value());
        String message = messageSource.getMessage("error." + status.value(), null,
                localeResolver.resolveLocale(request));
        return renderErrorPage(status, request, message);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ModelAndView noHandlerFoundException(NoHandlerFoundException ex, HttpServletResponse response,
                                                   HttpServletRequest request) {
        LOGGER.warn("noHandlerFoundException, message: {}", ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        ModelAndView mav = new ModelAndView();
        response.setStatus(status.value());
        String message = messageSource.getMessage("error." + status.value(), null,
                localeResolver.resolveLocale(request));
        return renderErrorPage(status, request, message);
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException(Exception ex, HttpServletResponse response,
                                                        HttpServletRequest request){
        LOGGER.warn("handlerException, message: {}", ex.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ModelAndView mav = new ModelAndView();
        response.setStatus(status.value());
        String message = messageSource.getMessage("error." + status.value(), null,
                localeResolver.resolveLocale(request));
        return renderErrorPage(status, request, message);
    }

    private ModelAndView renderErrorPage(HttpStatus status, HttpServletRequest request, String message) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("code", status);
        mav.addObject("message", message);
        mav.addObject("path", request.getServletPath());
        mav.setViewName("error");
        return mav;
    }
}
