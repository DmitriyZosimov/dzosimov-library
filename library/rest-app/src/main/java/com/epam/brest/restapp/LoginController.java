package com.epam.brest.restapp;

import com.epam.brest.service.ILoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Check by identification if the reader is exist
     * @param id reader identification
     * @return true if the reader is exist, or false if the reader is not exist
     */
    @GetMapping(value = "/login/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> isExistCard(@PathVariable("id") Integer id){
        Boolean result = loginService.isExistCard(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    /**
     * Check by identification if the reader is removed (not active)
     * @param id reader identification
     * @return true if the reader is exist, or false if the reader is not exist
     */
    @GetMapping(value = "/login/{id}/removed", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> isRemovedCard(@PathVariable("id") Integer id){
        Boolean result = loginService.isRemovedCard(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}
