package com.epam.brest.webapp;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.BookService;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CatalogController.class)
public class CatalogControllerMockTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;
  @Captor
  private ArgumentCaptor<Integer> captor;
  @Captor
  private ArgumentCaptor<SearchBookSample> captorSearchBook;

  @Test
  public void shouldReturnCatalogPage() throws Exception {
    BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
    BookSample bs2 = new BookSample(2, "author", "title", Genre.ART, 2);
    BookSample bs3 = new BookSample(3, "author", "title", Genre.ADVENTURE, 1);
    BookSample bookSample = new BookSample();
    bookSample.setId(1);
    SearchBookSample searchBookSample = new SearchBookSample();
    bookSample.setId(2);

    when(bookService.findAll()).thenReturn(Arrays.asList(bs1, bs2, bs3));
    mockMvc.perform(
        MockMvcRequestBuilders.get("/")
            .sessionAttr("bookSample", bookSample)
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
            sessionAttribute("bookSample", bookSample))
        .andExpect(MockMvcResultMatchers.request().
            sessionAttribute("searchBookSample", searchBookSample));
  }

  @Test
  public void shouldReturnCatalogPageWhenPathCatalog() throws Exception {
    BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
    BookSample bs2 = new BookSample(2, "author", "title", Genre.ART, 2);
    BookSample bs3 = new BookSample(3, "author", "title", Genre.ADVENTURE, 1);

    when(bookService.findAll()).thenReturn(Arrays.asList(bs1, bs2, bs3));
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

    when(bookService.addBookForReader(anyInt(), anyInt())).thenReturn(true);

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
    Mockito.verify(bookService).addBookForReader(captor.capture(), anyInt());
    Integer readerId = captor.getValue();
    Assertions.assertEquals(Integer.valueOf(1), readerId);
  }

  @Test
  public void shouldReturnFoundedBooks() throws Exception {
    BookSample bs1 = new BookSample(1, "author", "title", Genre.MYSTERY, 3);
    SearchBookSample sbs = new SearchBookSample("author", "title", Genre.MYSTERY);
    BookSample bookSample = new BookSample();
    bookSample.setId(1);

    when(bookService.searchBooks(any())).thenReturn(Arrays.asList(bs1));
    mockMvc.perform(
        MockMvcRequestBuilders.post("/search")
            .param("authors", sbs.getAuthors())
            .param("title", sbs.getTitle())
            .param("genre", sbs.getGenre().toString())
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
    verify(bookService).searchBooks(captorSearchBook.capture());
    SearchBookSample searchBookSample = captorSearchBook.getValue();
    Assertions.assertEquals(sbs.getAuthors(), searchBookSample.getAuthors());
  }

}
