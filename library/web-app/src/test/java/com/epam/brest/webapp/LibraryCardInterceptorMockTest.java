package com.epam.brest.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class LibraryCardInterceptorMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGoToLoginPageWhenOpenProfile() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldGoToLoginPageWhenOpenCatalogSelectBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/catalog/select/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldGoToLoginPageWhenOpenProfileDelete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile/delete")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldGoToLoginPageWhenOpenProfileEditPost() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile/edit")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldGoToLoginPageWhenOpenProfileEditGet() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/edit")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldGoToLoginPageWhenDeleteBookInProfile() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile/book/delete/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }
}
