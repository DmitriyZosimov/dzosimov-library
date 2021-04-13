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

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest request, LocaleResolver localeResolver,
                              MessageSource messageSource){
        LOGGER.warn("handleError");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            Integer statusCode = Integer.valueOf(status.toString());
            String message = messageSource.getMessage("error." + statusCode, null,
                    localeResolver.resolveLocale(request));
            model.addAttribute("code", statusCode);
            model.addAttribute("message", message);
            model.addAttribute("path", request.getServletPath());
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
