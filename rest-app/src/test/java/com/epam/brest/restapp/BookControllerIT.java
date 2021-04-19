package com.epam.brest.restapp;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class BookControllerIT {

    private static final String BOOK_URL = "/book";

    @Autowired
    private BookController bookController;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private MockBookService bookService = new MockBookService();

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldFindAllDepartments() throws Exception {
        List<BookSample> books = bookService.findAll();
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    public void shouldFindBookById() throws Exception {
        BookSample book = bookService.findBookById();
        assertNotNull(book);
        assertEquals(1, book.getId());
    }

    @Test
    public void shouldNotFoundBookById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(BOOK_URL + "/9999999")
            .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldSearchBooks() throws Exception {
        SearchBookSample bookSample = new SearchBookSample("o", "", Genre.DEFAULT);
        List<BookSample> books = bookService.searchBooks(bookSample);
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    public void shouldReturnTrueAfterSavingBook() throws Exception {
        BookSample bookSample = new BookSample("test", "test", Genre.ADVENTURE);
        assertTrue(bookService.createBook(bookSample));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenSavingBook() throws Exception {
        BookSample bookSample = new BookSample("", "", Genre.ADVENTURE);
        String json = mapper.writeValueAsString(bookSample);
        MockHttpServletResponse response = mockMvc.perform(post(BOOK_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnTrueAfterUpdatingBook() throws Exception {
        BookSample bookSample = bookService.findBookById();
        assertNotNull(bookSample);
        bookSample.setAuthors("test");
        assertTrue(bookService.editBook(bookSample));
        bookSample = bookService.findBookById();
        assertEquals("test", bookSample.getAuthors());
    }

    @Test
    public void shouldReturnStatusBadRequestWhenUpdatingBook() throws Exception {
        BookSample bookSample = bookService.findBookById();
        assertNotNull(bookSample);
        bookSample.setAuthors("");
        String json = mapper.writeValueAsString(bookSample);
        MockHttpServletResponse response = mockMvc.perform(post(BOOK_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnTrueAfterAddReaderForBook() throws Exception {
        Integer readerId = 4;
        Integer bookId = 1;
        assertTrue(bookService.addReaderForBook(bookId, readerId));
    }

    @Test
    public void shouldStatusOKAndBodyFalseWhenAddReaderForBook() throws Exception {
        Integer readerId = 4;
        Integer bookId = 99;
        MockHttpServletResponse response = mockMvc.perform(
                get(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldStatusBadRequestWhenAddReaderForBookIfReaderNotExists() throws Exception {
        Integer readerId = 44;
        Integer bookId = 1;
        MockHttpServletResponse response = mockMvc.perform(
                get(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenAddReaderForBookIfReaderIdNotInteger() throws Exception {
        String readerId = "readerId";
        Integer bookId = 1;
        MockHttpServletResponse response = mockMvc.perform(
                get(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnTrueAfterDeletingBook() throws Exception {
        Integer bookId = 1;
        assertTrue(bookService.delete(bookId));
    }

    @Test
    public void shouldReturnFalseAfterDeletingBook() throws Exception {
        Integer bookId = 999999999;
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BOOK_URL + "/" + bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldStatusOKAndBodyFalseAfterRemovingTieBetweenReaderAndBookWhenBookIsNotNull() throws Exception {
        Integer readerId = 4;
        Integer bookId = 1999999999;
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void shouldReturnStatusOKAndBodyFalseAfterRemovingTieBetweenReaderAndBook() throws Exception {
        Integer readerId = 49999999;
        Integer bookId = 1;
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        assertFalse(mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {}));
    }



    private class MockBookService {

        public List<BookSample> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(BOOK_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<List<BookSample>>() {});
        }

        public BookSample findBookById() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(BOOK_URL + "/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<BookSample>() {});
        }

        public List<BookSample> searchBooks(SearchBookSample bookSample) throws Exception {
            String json = mapper.writeValueAsString(bookSample);
            MockHttpServletResponse response = mockMvc.perform(post(BOOK_URL + "/search")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<List<BookSample>>() {});
        }

        public Boolean createBook(BookSample bookSample) throws Exception {
            String json = mapper.writeValueAsString(bookSample);
            MockHttpServletResponse response = mockMvc.perform(post(BOOK_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {});
        }

        public Boolean editBook(BookSample bookSample) throws Exception {
            String json = mapper.writeValueAsString(bookSample);
            MockHttpServletResponse response = mockMvc.perform(put(BOOK_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {});
        }

        public Boolean addReaderForBook(Integer bookId, Integer readerId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {});
        }

        public Boolean delete(Integer bookId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(BOOK_URL + "/" + bookId)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {});
        }

        public Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(BOOK_URL + "/" + bookId + "/reader/" + readerId)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), new TypeReference<Boolean>() {});
        }
    }

}
