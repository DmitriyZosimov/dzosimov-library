package com.epam.brest.service.rest;

import com.epam.brest.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginServiceRest implements LoginService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceRest.class);

  private String url;
  private RestTemplate restTemplate;

  public LoginServiceRest(String url, RestTemplate restTemplate) {
    this.url = url;
    this.restTemplate = restTemplate;
  }

  /*
  get /login/{id}
   */
  @Override
  public Boolean isExistCard(Integer card) {
    LOGGER.info("isExistCard(card={})", card);
    return getInfoAboutCard(url + "/" + card);
  }

  /*
  get /login/{id}/removed
   */
  @Override
  public Boolean isRemovedCard(Integer card) {
    LOGGER.info("isRemovedCard(card={})", card);
    return getInfoAboutCard(url + "/" + card + "/removed");
  }

  private Boolean getInfoAboutCard(String url) {
    ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
    return response.getBody();
  }
}
