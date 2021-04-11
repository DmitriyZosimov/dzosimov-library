package com.epam.brest.webapp;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
public class CatalogControllerIT {

    private static final String BOOK_URL = "http://localhost:8060/book";
    private static final String BOOK_ID_URL = "http://localhost:8060/book/1";
    private static final String TIE_BOOK_AND_READER_URL = "http://localhost:8060/book/1/reader/1";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private MockRestServiceServer server;

    @Autowired
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnCatalogPage() throws Exception {
        BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
        BookSample bs2 = new BookSample(2, "author", "title", Genre.ART, 2);
        BookSample bs3 = new BookSample(3, "author", "title", Genre.ADVENTURE, 1);
        BookSample bookSample = new BookSample();
        SearchBookSample searchBookSample = new SearchBookSample();
        server.expect(ExpectedCount.once(), requestTo(BOOK_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(Arrays.asList(bs1, bs2, bs3))));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/")
                        .sessionAttr("searchBookSample", searchBookSample)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("catalog"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs1.getId())),
                                hasProperty("authors", is(bs1.getAuthors())),
                                hasProperty("title", is(bs1.getTitle())),
                                hasProperty("genre", is(bs1.getGenre())),
                                hasProperty("quantity", is(bs1.getQuantity()))
                        )
                )))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs2.getId())),
                                hasProperty("authors", is(bs2.getAuthors())),
                                hasProperty("title", is(bs2.getTitle())),
                                hasProperty("genre", is(bs2.getGenre())),
                                hasProperty("quantity", is(bs2.getQuantity()))
                        )
                )))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs3.getId())),
                                hasProperty("authors", is(bs3.getAuthors())),
                                hasProperty("title", is(bs3.getTitle())),
                                hasProperty("genre", is(bs3.getGenre())),
                                hasProperty("quantity", is(bs3.getQuantity()))
                        )
                )))
                .andExpect(MockMvcResultMatchers.request().
                        sessionAttribute("searchBookSample", searchBookSample));
    }

    @Test
    public void shouldReturnCatalogPageWhenPathCatalog() throws Exception {
        BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
        BookSample bs2 = new BookSample(2, "author", "title", Genre.ART, 2);
        BookSample bs3 = new BookSample(3, "author", "title", Genre.ADVENTURE, 1);

        server.expect(ExpectedCount.once(), requestTo(BOOK_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(Arrays.asList(bs1, bs2, bs3))));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/catalog")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("catalog"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs1.getId())),
                                hasProperty("authors", is(bs1.getAuthors())),
                                hasProperty("title", is(bs1.getTitle())),
                                hasProperty("genre", is(bs1.getGenre())),
                                hasProperty("quantity", is(bs1.getQuantity()))
                        )
                )))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs2.getId())),
                                hasProperty("authors", is(bs2.getAuthors())),
                                hasProperty("title", is(bs2.getTitle())),
                                hasProperty("genre", is(bs2.getGenre())),
                                hasProperty("quantity", is(bs2.getQuantity()))
                        )
                )))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs3.getId())),
                                hasProperty("authors", is(bs3.getAuthors())),
                                hasProperty("title", is(bs3.getTitle())),
                                hasProperty("genre", is(bs3.getGenre())),
                                hasProperty("quantity", is(bs3.getQuantity()))
                        )
                )));
    }

    @Test
    public void shouldTieReaderAndBook() throws Exception {
        BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
        BookSample bookSample = new BookSample();
        bookSample.setId(1);
        SearchBookSample searchBookSample = new SearchBookSample();
        bookSample.setId(2);

        server.expect(ExpectedCount.once(), requestTo(TIE_BOOK_AND_READER_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("true"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/catalog/select/" + bs1.getId())
                        .sessionAttr("bookSample", bookSample)
                        .sessionAttr("searchBookSample", searchBookSample)
                        .sessionAttr("libraryCard", 1)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/catalog"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"))
                .andExpect(MockMvcResultMatchers.request().
                        sessionAttribute("bookSample", bookSample))
                .andExpect(MockMvcResultMatchers.request().
                        sessionAttribute("searchBookSample", searchBookSample));
    }

    @Test
    public void shouldReturnFoundedBooks() throws Exception {
        BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
        SearchBookSample sbs = new SearchBookSample("author", "title", Genre.MYSTERY);
        BookSample bookSample = new BookSample();
        bookSample.setId(1);
        server.expect(ExpectedCount.once(), requestTo(BOOK_URL + "/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(Arrays.asList(bs1))));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/search")
                        .param("authors",sbs.getAuthors())
                        .param("title",sbs.getTitle())
                        .param("genre",sbs.getGenre().toString())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("catalog"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasItem(
                        Matchers.allOf(
                                hasProperty("id", is(bs1.getId())),
                                hasProperty("authors", is(bs1.getAuthors())),
                                hasProperty("title", is(bs1.getTitle())),
                                hasProperty("genre", is(bs1.getGenre())),
                                hasProperty("quantity", is(bs1.getQuantity()))
                        )
                )));
    }

    @Disabled
    @Test
    public void shouldBindingResultGetErrors() throws Exception {
        SearchBookSample sbs = new SearchBookSample("12", "tit325235le", Genre.MYSTERY);
        BookSample bookSample = new BookSample();
        bookSample.setId(1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/search")
                        .param("authors",sbs.getAuthors())
                        .param("title",sbs.getTitle())
                        .param("genre",sbs.getGenre().toString())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("searchBookSample", "authors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog"));
    }
}
