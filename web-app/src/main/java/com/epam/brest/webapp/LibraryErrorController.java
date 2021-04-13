package com.epam.brest.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LibraryErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryErrorController.class);

    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    public LibraryErrorController(LocaleResolver localeResolver, MessageSource messageSource) {
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest request){
        LOGGER.warn("handleError");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            Integer statusCode = Integer.valueOf(status.toString());
            String message = messageSource.getMessage("error." + statusCode, null,
                    localeResolver.resolveLocale(request));
            model.addAttribute("code", statusCode);
            model.addAttribute("message", message);
            model.addAttribute("path", request.getAttribute("javax.servlet.forward.request_uri"));
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
