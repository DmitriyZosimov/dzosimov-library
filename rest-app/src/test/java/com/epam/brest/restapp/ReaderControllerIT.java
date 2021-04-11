package com.epam.brest.restapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class ReaderControllerIT {

    private static final String READER_URL = "/reader";

    @Autowired
    private ReaderController readerController;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;


    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(readerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldReturnListOfAllReaders() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(READER_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        List<ReaderSample> readers = mapper.readValue(response.getContentAsString(), new TypeReference<List<ReaderSample>>() {});
        assertFalse(readers.isEmpty());
    }

    @Test
    public void shouldReturnReaderById() throws Exception {
        Integer readerId = 1;

        MockHttpServletResponse response = mockMvc.perform(get(READER_URL + "/" + readerId)
            .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        ReaderSample reader = mapper.readValue(response.getContentAsString(), new TypeReference<ReaderSample>() {});
        assertNotNull(reader);
        assertEquals(readerId, reader.getReaderId());
    }

    @Test
    public void shouldReturnStatusNotFoundReaderById() throws Exception {
        Integer readerId = 1999;
        MockHttpServletResponse response = mockMvc.perform(get(READER_URL + "/" + readerId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnReaderWithoutBooksById() throws Exception {
        Integer readerId = 1;

        MockHttpServletResponse response = mockMvc.perform(get(READER_URL + "/" + readerId + "/without_books")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        ReaderSample reader = mapper.readValue(response.getContentAsString(), new TypeReference<ReaderSample>() {});
        assertNotNull(reader);
        assertEquals(readerId, reader.getReaderId());
    }

    @Test
    public void shouldReturnStatusNotFoundReaderWithoutBooksById() throws Exception {
        Integer readerId = 1999;
        MockHttpServletResponse response = mockMvc.perform(get(READER_URL + "/" + readerId + "/without_books")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnSavedReader() throws Exception {
        ReaderSample readerSample = new ReaderSample("test", "test", "test");
        String json = mapper.writeValueAsString(readerSample);
        MockHttpServletResponse response = mockMvc.perform(post(READER_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse();
        assertNotNull(response);
        readerSample = mapper.readValue(response.getContentAsString(), new TypeReference<ReaderSample>() {});
        assertNotNull(readerSample);
        assertNotNull(readerSample.getReaderId());
    }

    @Test
    public void shouldReturnBadRequestWhenRequestReaderIsNull() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post(READER_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnTrueAfterUpdatingReader() throws Exception {
        Integer readerId = 1;
        MockHttpServletResponse response = mockMvc.perform(get(READER_URL + "/" + readerId + "/without_books")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        ReaderSample reader = mapper.readValue(response.getContentAsString(), new TypeReference<ReaderSample>() {});
        assertNotNull(reader);
        assertEquals(readerId, reader.getReaderId());
        reader.setFirstName("test");
        reader.setLastName("test");
        reader.setPatronymic("test");

        String json = mapper.writeValueAsString(reader);
        response = mockMvc.perform(put(READER_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse();
        assertNotNull(response);
        assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnStatusOKAndBodyFalseAfterUpdatingReaderWhenReaderIsNotExist() throws Exception {
        ReaderSample reader = new ReaderSample();
        reader.setReaderId(9999);
        reader.setFirstName("test");
        reader.setLastName("test");
        reader.setPatronymic("test");

        String json = mapper.writeValueAsString(reader);
        MockHttpServletResponse response = mockMvc.perform(put(READER_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnTrueAfterRestoringReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(delete(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));

        response = mockMvc.perform(put(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotDeletedBeforeRestoringReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(put(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotExistBeforeRestoringReader() throws Exception {
        Integer id = 1999;
        MockHttpServletResponse response = mockMvc.perform(put(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnTrueAfterDeletingReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(delete(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseWhenReaderHasBeenDeletedYetBeforeDeletingReader() throws Exception {
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(delete(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertTrue(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));

        response = mockMvc.perform(delete(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotExistBeforeDeletingReader() throws Exception {
        Integer id = 1999;
        MockHttpServletResponse response = mockMvc.perform(delete(READER_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnListOfFoundReadersAfterSearchingReadersByDate() throws Exception {
        SearchReaderSample searchReaderSample = new SearchReaderSample();
        searchReaderSample.setFrom(LocalDate.of(2020, 01, 13));
        searchReaderSample.setTo(LocalDate.now());
        String json = mapper.writeValueAsString(searchReaderSample);
        MockHttpServletResponse response = mockMvc.perform(post("/readers/search")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        List<ReaderSample> list = mapper.readValue(response.getContentAsString(), new TypeReference<List<ReaderSample>>() {});
        assertFalse(list.isEmpty());
        list.forEach(r -> assertTrue(r.getDateOfRegistry().isAfter(searchReaderSample.getFrom()) &&
                r.getDateOfRegistry().isBefore(searchReaderSample.getTo())));
    }

    @Test
    public void shouldReturnValidationErrorAndHttpStatusBadRequestBeforeSearchingReadersByDate() throws Exception {
        SearchReaderSample searchReaderSample = new SearchReaderSample();
        searchReaderSample.setTo(LocalDate.of(2020, 01, 13));
        searchReaderSample.setFrom(LocalDate.now());
        String json = mapper.writeValueAsString(searchReaderSample);
        MockHttpServletResponse response = mockMvc.perform(post("/readers/search")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }
}
