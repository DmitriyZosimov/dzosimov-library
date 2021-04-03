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
    public void shouldAddBookAndForwardCatalogPageWithGoodMessage() throws Exception {
        BookSample bs = new BookSample("author", "title", Genre.MYSTERY);
        String message = "The book was added";
        when(bookService.createBook(any(BookSample.class))).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                .sessionAttr("bookSample", bs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldNotAddBookAndForwardCatalogPageWithBadMessage() throws Exception {
        BookSample bs = new BookSample("author", "title", Genre.MYSTERY);
        String message = "The book was not added";
        when(bookService.createBook(any(BookSample.class))).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                        .sessionAttr("bookSample", bs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    //TODO:refactor
    @Disabled
    @Test
    public void shouldReturnBookPageWithErrorsWhenBindingResultHasErrors() throws Exception {
        BookSample bs = new BookSample("1", "", Genre.MYSTERY);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/new")
                        .sessionAttr("bookSample", bs)
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
    public void shouldForwardCatalogPageWhenBookNotFoundById() throws Exception {
        String message = "the book by id 1 not found";
        when(bookService.findBookById(anyInt())).thenReturn(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/catalog"))
                .andExpect(forwardedUrl("/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));

    }

    @Test
    public void shouldForwardToCatalogPageAfterEditBookWithGoodMessage() throws Exception {
        BookSample bs = new BookSample(1,"author", "title", Genre.MYSTERY, 3);
        String message = "The book was edited";
        when(bookService.editBook(any(BookSample.class))).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .sessionAttr("bookSample", bs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToCatalogPageAfterEditBookWithBadMessage() throws Exception {
        BookSample bs = new BookSample(1,"author", "title", Genre.MYSTERY, 3);
        String message = "The book was not edited";
        when(bookService.editBook(any(BookSample.class))).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .sessionAttr("bookSample", bs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    //TODO:refactor
    @Disabled
    @Test
    public void shouldReturnBookPageAfterEditWithErrorsWhenBindingResultHasErrors() throws Exception {
        BookSample bs = new BookSample(1,"1", "", Genre.MYSTERY, 3);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/" + bs.getId())
                        .sessionAttr("bookSample", bs)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeHasFieldErrors("bookSample", "authors", "title"))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForwardToCatalogPageAfterDeleteBookWithGoodMessage() throws Exception {
        String message = "The book was removed";
        when(bookService.delete(anyInt())).thenReturn(true);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/delete/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }

    @Test
    public void shouldForwardToCatalogPageAfterDeleteBookWithBadMessage() throws Exception {
        String message = "The book was not removed";
        when(bookService.delete(anyInt())).thenReturn(false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book/delete/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/catalog"))
                .andExpect(view().name("forward:/catalog"))
                .andExpect(model().attribute("resultMessage", Matchers.hasToString(message)));
    }
}
