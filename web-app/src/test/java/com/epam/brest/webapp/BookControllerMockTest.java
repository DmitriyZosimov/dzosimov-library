package com.epam.brest.webapp;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.service.IBookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Locale;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class BookControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookService bookService;

    @Test
    public void shouldReturnBookPage() throws Exception {
        BookSample bookSample = new BookSample();
        bookSample.setId(1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/new")
                        .sessionAttr("bookSample", bookSample)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("isNew", is(true)));
    }

    @Test
    public void shouldAddBookAndRedirectCatalogPageWithGoodMessage() throws Exception {
        BookSample bs = new BookSample("author", "title", Genre.MYSTERY);
        String message = "The book was added";
        when(bookService.createBook(any(BookSample.class))).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }

    @Test
    public void shouldNotAddBookAndRedirectCatalogPageWithBadMessage() throws Exception {
        BookSample bs = new BookSample("author", "title", Genre.MYSTERY);
        String message = "The book was not added";
        when(bookService.createBook(any(BookSample.class))).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }

    @Test
    public void shouldReturnBookPageWithErrorsWhenBindingResultHasErrors() throws Exception {
        BookSample bs = new BookSample("1", "", Genre.MYSTERY);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
                        .session(new MockHttpSession())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeHasFieldErrors("bookSample", "authors", "title"))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetBookPageWithFoundBookById() throws Exception {
        BookSample bs = new BookSample(1,"author", "title", Genre.MYSTERY, 3);
        when(bookService.findBookById(anyInt())).thenReturn(bs);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/" + bs.getId())
                .session(new MockHttpSession())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("bookSample",
                        hasProperty("id", is(bs.getId()))))
                .andExpect(model().attribute("bookSample",
                        hasProperty("authors", is(bs.getAuthors()))))
                .andExpect(model().attribute("bookSample",
                        hasProperty("title", is(bs.getTitle()))))
                .andExpect(model().attribute("bookSample",
                        hasProperty("genre", is(bs.getGenre()))));

    }

    @Test
    public void shouldRedirectToCatalogPageAfterEditBookWithGoodMessage() throws Exception {
        BookSample bs = new BookSample(1,"author", "title", Genre.MYSTERY, 3);
        String message = "The book was edited";
        when(bookService.editBook(any(BookSample.class))).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }

    @Test
    public void shouldRedirectToCatalogPageAfterEditBookWithBadMessage() throws Exception {
        BookSample bs = new BookSample(1,"author", "title", Genre.MYSTERY, 3);
        String message = "The book was not edited";
        when(bookService.editBook(any(BookSample.class))).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }

    @Test
    public void shouldReturnBookPageAfterEditWithErrorsWhenBindingResultHasErrors() throws Exception {
        BookSample bs = new BookSample(1,"1", "", Genre.MYSTERY, 3);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .param("authors", bs.getAuthors())
                        .param("title", bs.getTitle())
                        .param("genre", bs.getGenre().name())
                        .session(new MockHttpSession())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeHasFieldErrors("bookSample", "authors", "title"))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRedirectToCatalogPageAfterDeleteBookWithGoodMessage() throws Exception {
        String message = "The book was removed";
        when(bookService.delete(anyInt())).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/delete/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }

    @Test
    public void shouldRedirectToCatalogPageAfterDeleteBookWithBadMessage() throws Exception {
        String message = "The book was not removed";
        when(bookService.delete(anyInt())).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/delete/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/result?resultMessage=" + message))
                .andExpect(view().name("redirect:/result?resultMessage=" + message));
    }
}
