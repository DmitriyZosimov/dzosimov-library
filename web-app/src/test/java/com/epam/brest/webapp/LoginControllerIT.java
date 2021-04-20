package com.epam.brest.webapp;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class LoginControllerIT {

  private static final String LOGIN_URL = "http://localhost:8060/login/1";
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private WebApplicationContext wac;
  private MockMvc mockMvc;
  private MockRestServiceServer server;
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    server = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void shouldGetLoginPage() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/login")
            .session(new MockHttpSession())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  public void shouldRedirectCatalogPageAfterLogin() throws Exception {
    server.expect(ExpectedCount.once(), requestTo(LOGIN_URL))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("true"));
    mockMvc.perform(
        MockMvcRequestBuilders.post("/login")
            .param("card", "1")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/catalog"))
        .andExpect(redirectedUrl("/catalog"))
        .andExpect(request().sessionAttribute("libraryCard", Matchers.is(1)));
  }

  @Test
  public void shouldGoToLoginPageWhenReaderNotExistAndReaderIsRemoved() throws Exception {
    server.expect(ExpectedCount.once(), requestTo(LOGIN_URL))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("false"));
    server.expect(ExpectedCount.once(), requestTo(LOGIN_URL + "/removed"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("true"));
    mockMvc.perform(
        MockMvcRequestBuilders.post("/login")
            .param("card", "1")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(request().sessionAttributeDoesNotExist("libraryCard"))
        .andExpect(model().attribute("isRemoved", Matchers.is(true)))
        .andExpect(model().attribute("removedCard", 1));
  }

  @Test
  public void shouldRedirectToCatalogWhenReaderNotExistAndNotRemoved() throws Exception {
    server.expect(ExpectedCount.once(), requestTo(LOGIN_URL))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("false"));
    server.expect(ExpectedCount.once(), requestTo(LOGIN_URL + "/removed"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("false"));
    mockMvc.perform(
        MockMvcRequestBuilders.post("/login")
            .param("card", "1")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/catalog"))
        .andExpect(redirectedUrl("/catalog"))
        .andExpect(request().sessionAttributeDoesNotExist("libraryCard"))
        .andExpect(model().attributeDoesNotExist("isRemoved"))
        .andExpect(model().attributeDoesNotExist("removedCard"));
  }

  @Test
  public void shouldRedirectToCatalogWhenLogout() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/logout")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/catalog"))
        .andExpect(redirectedUrl("/catalog"))
        .andExpect(request().sessionAttributeDoesNotExist("libraryCard"));
  }
}
