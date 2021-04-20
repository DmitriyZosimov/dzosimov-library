package com.epam.brest.restapp;

import com.epam.brest.service.LoginService;
import javax.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class LoginController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  /**
   * Check by identification if the reader is exist
   *
   * @param id reader identification
   * @return true if the reader is exist, or false if the reader is not exist
   */
  @GetMapping(value = "/login/{id}", produces = "application/json")
  public ResponseEntity<Boolean> isExistCard(@PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("isExistCard(id={})", id);
    Boolean result = loginService.isExistCard(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Check by identification if the reader is removed (not active)
   *
   * @param id reader identification
   * @return true if the reader is exist, or false if the reader is not exist
   */
  @GetMapping(value = "/login/{id}/removed", produces = "application/json")
  public ResponseEntity<Boolean> isRemovedCard(@PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("isRemovedCard(id={})", id);
    Boolean result = loginService.isRemovedCard(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
