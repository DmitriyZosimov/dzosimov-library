package com.epam.brest.webapp;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.service.BookService;
import com.epam.brest.service.ReaderService;
import java.time.LocalDate;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class ProfileControllerMockTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;
  @MockBean
  private ReaderService readerService;

  @Test
  public void shouldReturnReadersPage() throws Exception {
    ReaderSample rs1 = createReaderSample();
    ReaderSample rs2 = createReaderSample();
    rs2.setReaderId(2);
    ReaderSample rs3 = createReaderSample();
    rs3.setReaderId(3);

    when(readerService.findAll()).thenReturn(Arrays.asList(rs1, rs2, rs3));
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profiles")
            .session(new MockHttpSession())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.view().name("readers"))
        .andExpect(MockMvcResultMatchers.model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs1.getReaderId())),
                hasProperty("firstName", is(rs1.getFirstName())),
                hasProperty("lastName", is(rs1.getLastName())),
                hasProperty("patronymic", is(rs1.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs1.getDateOfRegistry()))
            )
        )))
        .andExpect(MockMvcResultMatchers.model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs2.getReaderId())),
                hasProperty("firstName", is(rs2.getFirstName())),
                hasProperty("lastName", is(rs2.getLastName())),
                hasProperty("patronymic", is(rs2.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs2.getDateOfRegistry()))
            )
        )))
        .andExpect(MockMvcResultMatchers.model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs3.getReaderId())),
                hasProperty("firstName", is(rs3.getFirstName())),
                hasProperty("lastName", is(rs3.getLastName())),
                hasProperty("patronymic", is(rs3.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs3.getDateOfRegistry()))
            )
        )));
  }

  @Test
  public void shouldGetProfilePageWithFoundReaderById() throws Exception {
    ReaderSample rs = createReaderSample();
    when(readerService.findReaderById(anyInt())).thenReturn(rs);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profile")
            .sessionAttr("libraryCard", rs.getReaderId())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("profile"))
        .andExpect(model().attribute("readerSample",
            hasProperty("readerId", is(rs.getReaderId()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("firstName", is(rs.getFirstName()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("lastName", is(rs.getLastName()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("patronymic", is(rs.getPatronymic()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("dateOfRegistry", is(rs.getDateOfRegistry()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("books", is(rs.getBooks()))));

  }

  @Test
  public void shouldGetReaderPageWithFoundReaderById() throws Exception {
    ReaderSample rs = createReaderSample();
    rs.setBooks(null);
    when(readerService.findReaderByIdWithoutBooks(anyInt())).thenReturn(rs);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profile/edit")
            .sessionAttr("libraryCard", rs.getReaderId())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("reader"))
        .andExpect(model().attribute("readerSample",
            hasProperty("readerId", is(rs.getReaderId()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("firstName", is(rs.getFirstName()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("lastName", is(rs.getLastName()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("patronymic", is(rs.getPatronymic()))))
        .andExpect(model().attribute("readerSample",
            hasProperty("dateOfRegistry", is(rs.getDateOfRegistry()))))
        .andExpect(model().attribute("isNew", is(false)));

  }

  @Test
  public void shouldRedirectToCatalogPageAfterEditingReaderWithGoodMessage() throws Exception {
    ReaderSample rs = createReaderSample();
    rs.setBooks(null);
    String message = "The+reader+was+edited";
    when(readerService.editReader(any(ReaderSample.class))).thenReturn(true);
    mockMvc.perform(
        MockMvcRequestBuilders.post("/profile/edit")
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
            .sessionAttr("libraryCard", rs.getReaderId())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldRedirectToProfilePageAfterEditingReaderWithBadMessage() throws Exception {
    ReaderSample rs = createReaderSample();
    rs.setBooks(null);
    String message = "The+reader+was+not+edited";
    when(readerService.editReader(any(ReaderSample.class))).thenReturn(false);
    mockMvc.perform(
        MockMvcRequestBuilders.post("/profile/edit")
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
            .sessionAttr("libraryCard", rs.getReaderId())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldGetReaderPageBeforeEditingWhenBindingResultHasErrors() throws Exception {
    ReaderSample rs = createReaderSample();
    rs.setFirstName("11");
    rs.setBooks(null);
    mockMvc.perform(
        MockMvcRequestBuilders.post("/profile/edit")
            .sessionAttr("libraryCard", rs.getReaderId())
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("reader"))
        .andExpect(model().attributeHasFieldErrors("readerSample", "firstName"));
  }

  @Test
  public void shouldGetReaderPage() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/registry")
            .sessionAttr("bookSample", new BookSample())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("reader"))
        .andExpect(model().attribute("isNew", is(true)))
        .andExpect(model().attribute("readerSample",
            hasProperty("firstName", Matchers.nullValue())));
  }

  @Test
  public void shouldRedirectToCatalogAfterAddingReaderWithGoodMessage() throws Exception {
    ReaderSample rs = new ReaderSample("first", "last", "patronymic");
    ReaderSample savedRs = createReaderSample();
    String message = "The+reader+was+added%2C+library+card+is+1";
    when(readerService.createReader(any(ReaderSample.class))).thenReturn(savedRs);
    mockMvc.perform(
        MockMvcRequestBuilders.post("/registry")
            .session(new MockHttpSession())
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldRedirectToCatalogAfterAddingReaderWithBadMessage() throws Exception {
    ReaderSample rs = new ReaderSample("first", "last", "patronymic");
    String message = "The+reader+was+not+added";
    when(readerService.createReader(any(ReaderSample.class))).thenReturn(rs);
    mockMvc.perform(
        MockMvcRequestBuilders.post("/registry")
            .session(new MockHttpSession())
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldGetReaderPageBeforeAddingWhenBindingResultHasErrors() throws Exception {
    ReaderSample rs = new ReaderSample("12", "", "patronymic");
    mockMvc.perform(
        MockMvcRequestBuilders.post("/registry")
            .session(new MockHttpSession())
            .param("firstName", rs.getFirstName())
            .param("lastName", rs.getLastName())
            .param("patronymic", rs.getPatronymic())
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("reader"))
        .andExpect(model().attributeHasFieldErrors("readerSample",
            "firstName", "lastName"));
  }

  @Test
  public void shouldRedirectToCatalogAfterDeletingReaderWithGoodMessage() throws Exception {
    String message = "The+reader+was+removed";
    when(readerService.changeReaderToNoActive(anyInt())).thenReturn(true);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profile/delete")
            .sessionAttr("libraryCard", 1)
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"))
        .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("libraryCard"));
  }

  @Test
  public void shouldRedirectToCatalogAfterDeletingReaderWithBadMessage() throws Exception {
    String message = "The+reader+was+not+removed";
    when(readerService.changeReaderToNoActive(anyInt())).thenReturn(false);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profile/delete")
            .sessionAttr("libraryCard", 1)
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"))
        .andExpect(MockMvcResultMatchers.request().sessionAttribute("libraryCard", is(1)));
  }

  @Test
  public void shouldRedirectToCatalogAfterRestoringReaderWithGoodMessage() throws Exception {
    String message = "The+reader+was+restored%2C+library+card+is+1";
    when(readerService.changeReaderToActive(anyInt())).thenReturn(true);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/restore/1")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldRedirectToCatalogAfterRestoringReaderWithBadMessage() throws Exception {
    String message = "The+reader+was+not+restored";
    when(readerService.changeReaderToActive(anyInt())).thenReturn(false);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/restore/1")
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
        .andExpect(view().name("redirect:/result"));
  }

  @Test
  public void shouldRedirectToCatalogAfterDeletingBookFromBooksListOfReader() throws Exception {
    when(bookService.removeBookFromReader(anyInt(), anyInt())).thenReturn(true);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/profile/book/delete/1")
            .sessionAttr("libraryCard", 1)
    ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"))
        .andExpect(view().name("redirect:/profile"));
  }

  @Test
  public void shouldReturnReadersPageWithFoundReaders() throws Exception {
    ReaderSample rs1 = createReaderSample();
    ReaderSample rs2 = createReaderSample();
    rs2.setReaderId(2);
    ReaderSample rs3 = createReaderSample();
    rs3.setReaderId(3);
    SearchReaderSample searchReaderSample = new SearchReaderSample();
    searchReaderSample.setFrom(LocalDate.of(2020, 01, 23));
    searchReaderSample.setTo(LocalDate.now());
    when(readerService.searchReaders(any())).thenReturn(Arrays.asList(rs1, rs2, rs3));
    mockMvc.perform(MockMvcRequestBuilders.get("/readers/search")
        .session(new MockHttpSession())
        .param("from", searchReaderSample.getFrom().toString())
        .param("to", searchReaderSample.getTo().toString()))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("readers"))
        .andExpect(model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs1.getReaderId())),
                hasProperty("firstName", is(rs1.getFirstName())),
                hasProperty("lastName", is(rs1.getLastName())),
                hasProperty("patronymic", is(rs1.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs1.getDateOfRegistry()))
            )
        )))
        .andExpect(MockMvcResultMatchers.model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs2.getReaderId())),
                hasProperty("firstName", is(rs2.getFirstName())),
                hasProperty("lastName", is(rs2.getLastName())),
                hasProperty("patronymic", is(rs2.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs2.getDateOfRegistry()))
            )
        )))
        .andExpect(MockMvcResultMatchers.model().attribute("readers", Matchers.hasItem(
            Matchers.allOf(
                hasProperty("readerId", is(rs3.getReaderId())),
                hasProperty("firstName", is(rs3.getFirstName())),
                hasProperty("lastName", is(rs3.getLastName())),
                hasProperty("patronymic", is(rs3.getPatronymic())),
                hasProperty("dateOfRegistry", is(rs3.getDateOfRegistry()))
            )
        )));
  }

  @Test
  public void shouldReturnReadersPageWhenBindingResultHasErrors() throws Exception {
    ReaderSample rs1 = createReaderSample();
    ReaderSample rs2 = createReaderSample();
    rs2.setReaderId(2);
    ReaderSample rs3 = createReaderSample();
    rs3.setReaderId(3);
    SearchReaderSample searchReaderSample = new SearchReaderSample();
    searchReaderSample.setFrom(LocalDate.of(2020, 01, 23));
    searchReaderSample.setTo(LocalDate.now());
    when(readerService.searchReaders(any())).thenReturn(Arrays.asList(rs1, rs2, rs3));
    mockMvc.perform(MockMvcRequestBuilders.get("/readers/search")
        .session(new MockHttpSession())
        .param("from", searchReaderSample.getTo().toString())
        .param("to", searchReaderSample.getFrom().toString()))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("readers"))
        .andExpect(model().attributeHasFieldErrors("searchReaderSample", "from"));
  }

  private ReaderSample createReaderSample() {
    ReaderSample readerSample = new ReaderSample();
    readerSample.setReaderId(1);
    readerSample.setFirstName("first");
    readerSample.setLastName("last");
    readerSample.setPatronymic("patronymic");
    readerSample.setDateOfRegistry(LocalDate.now());

    Book bs1 = new Book(1, "author", "title", Genre.MYSTERY, 1);
    Book bs2 = new Book(2, "author two", "title two", Genre.MYSTERY, 1);
    Book bs3 = new Book(3, "author three", "title three", Genre.MYSTERY, 1);
    readerSample.setBooks(Arrays.asList(bs1, bs2, bs3));
    return readerSample;
  }
}
