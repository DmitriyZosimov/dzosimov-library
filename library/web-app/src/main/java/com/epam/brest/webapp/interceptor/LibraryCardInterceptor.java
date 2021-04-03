package com.epam.brest.webapp.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LibraryCardInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryCardInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        LOGGER.info("Interceptor preHandle");
        HttpSession session = request.getSession();
        Integer card = (Integer) session.getAttribute("libraryCard");
        if(card != null && card > 0){
            LOGGER.info("Interceptor preHandle - card="+card);
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
