package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.IReaderService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest
public class ProfileControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookService bookService;
    @MockBean
    private IReaderService readerService;


    @Test
    public void shouldGetProfilePageWithFoundReaderById() throws Exception {
        ReaderSample rs = createReaderSample();
        when(readerService.getProfile(anyInt())).thenReturn(rs);
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
    public void shouldForwardCatalogPageWhenReaderNotFoundById() throws Exception {
        String message = "the reader by id 9999999 not found";
        when(readerService.getProfile(anyInt())).thenReturn(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile")
                .sessionAttr("libraryCard", 9999999)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/catalog"))
                .andExpect(forwardedUrl("/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldGetReaderPageWithFoundReaderById() throws Exception {
        ReaderSample rs = createReaderSample();
        rs.setBooks(null);
        when(readerService.getProfileWithoutBooks(anyInt())).thenReturn(rs);
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
    public void shouldForwardCatalogPageBeforeEditingWhenReaderNotFoundById() throws Exception {
        String message = "the reader by id 9999999 not found";
        when(bookService.findBookById(anyInt())).thenReturn(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/profile/edit")
                        .sessionAttr("libraryCard", 9999999)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/catalog"))
                .andExpect(forwardedUrl("/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToProfilePageAfterEditingReaderWithGoodMessage() throws Exception {
        ReaderSample rs = createReaderSample();
        rs.setBooks(null);
        String message = "The reader was edited";
        when(readerService.editProfile(any(ReaderSample.class))).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/edit")
                .sessionAttr("readerSample", rs)
                .sessionAttr("libraryCard", rs.getReaderId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/profile"))
                .andExpect(view().name("forward:/profile"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToProfilePageAfterEditingReaderWithBadMessage() throws Exception {
        ReaderSample rs = createReaderSample();
        rs.setBooks(null);
        String message = "The reader was not edited";
        when(readerService.editProfile(any(ReaderSample.class))).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/edit")
                        .sessionAttr("readerSample", rs)
                        .sessionAttr("libraryCard", rs.getReaderId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/profile"))
                .andExpect(view().name("forward:/profile"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    //TODO: refactor
    @Disabled
    @Test
    public void shouldGetReaderPageBeforeEditingWhenBindingResultHasErrors() throws Exception {
        ReaderSample rs = createReaderSample();
        rs.setFirstName("11");
        rs.setBooks(null);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/edit")
                        .sessionAttr("readerSample", rs)
                        .sessionAttr("libraryCard", rs.getReaderId())
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
    public void shouldForwardToCatalogAfterAddingReaderWithGoodMessage() throws Exception {
        ReaderSample rs = new ReaderSample("first", "last", "patronymic");
        ReaderSample savedRs = createReaderSample();
        String message = "The reader was added, library card is 1";
        when(readerService.createReader(any(ReaderSample.class))).thenReturn(savedRs);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/registry")
                        .sessionAttr("readerSample", rs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToCatalogAfterAddingReaderWithBadMessage() throws Exception {
        ReaderSample rs = new ReaderSample("first", "last", "patronymic");
        ReaderSample savedRs = null;
        String message = "The reader was not added";
        when(readerService.createReader(any(ReaderSample.class))).thenReturn(savedRs);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/registry")
                        .sessionAttr("readerSample", rs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    //TODO:refactor
    @Disabled
    @Test
    public void shouldGetReaderPageBeforeAddingWhenBindingResultHasErrors() throws Exception {
        ReaderSample rs = new ReaderSample("12", "", "patronymic");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/registry")
                        .sessionAttr("readerSample", rs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("reader"))
                .andExpect(model().attributeHasFieldErrors("readerSample",
                        "firstName", "lastName"));
    }

    @Test
    public void shouldForwardToCatalogAfterDeletingReaderWithGoodMessage() throws Exception {
        String message = "The reader was removed";
        when(readerService.removeProfile(anyInt())).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/delete")
                        .sessionAttr("libraryCard", 1)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)))
                .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("libraryCard"));
    }

    @Test
    public void shouldForwardToCatalogAfterDeletingReaderWithBadMessage() throws Exception {
        String message = "The reader was not removed";
        when(readerService.removeProfile(anyInt())).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/delete")
                        .sessionAttr("libraryCard", 1)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("libraryCard", is(1)));
    }

    @Test
    public void shouldForwardToCatalogAfterRestoringReaderWithGoodMessage() throws Exception {
        String message = "The reader was restored, library card is 1";
        when(readerService.restoreProfile(anyInt())).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/restore/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToCatalogAfterRestoringReaderWithBadMessage() throws Exception {
        String message = "The reader was not restored";
        when(readerService.restoreProfile(anyInt())).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/restore/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldRedirectToCatalogAfterDeletingBookFromBooksListOfReader() throws Exception {
        when(bookService.removeFieldReaderFromBook(anyInt(), anyInt())).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profile/book/delete/1")
                .sessionAttr("libraryCard", 1)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"))
                .andExpect(view().name("redirect:/profile"));
    }

    private ReaderSample createReaderSample() {
        ReaderSample readerSample = new ReaderSample();
        readerSample.setReaderId(1);
        readerSample.setFirstName("first");
        readerSample.setLastName("last");
        readerSample.setPatronymic("patronymic");
        readerSample.setDateOfRegistry(LocalDate.now());

        Book bs1 = new Book(1,"author", "title", Genre.MYSTERY, 1);
        Book bs2 = new Book(2,"author two", "title two", Genre.MYSTERY, 1);
        Book bs3 = new Book(3,"author three", "title three", Genre.MYSTERY, 1);
        readerSample.setBooks(Arrays.asList(bs1, bs2, bs3));
        return readerSample;
    }
}
