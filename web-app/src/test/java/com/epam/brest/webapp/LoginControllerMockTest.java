package com.epam.brest.webapp;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.brest.service.LoginService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest
public class LoginControllerMockTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoginService loginService;

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
    Mockito.when(loginService.isExistCard(anyInt())).thenReturn(true);
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
    Mockito.when(loginService.isExistCard(anyInt())).thenReturn(false);
    Mockito.when(loginService.isRemovedCard(anyInt())).thenReturn(true);
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
    Mockito.when(loginService.isExistCard(anyInt())).thenReturn(false);
    Mockito.when(loginService.isRemovedCard(anyInt())).thenReturn(false);
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
