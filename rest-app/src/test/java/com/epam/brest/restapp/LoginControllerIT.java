package com.epam.brest.restapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.NEVER)
public class LoginControllerIT {

    private static final String LOGIN_URL = "/login";

    @Autowired
    private LoginController loginController;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnTrueAfterCheckIfExistReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(get(LOGIN_URL + "/" + id)
            .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseAfterCheckIfExistReader() throws Exception {
        Integer id = 19999;
        MockHttpServletResponse response = mockMvc.perform(get(LOGIN_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseAfterCheckIfRemovedReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(get(LOGIN_URL + "/" + id + "/removed")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }
}
