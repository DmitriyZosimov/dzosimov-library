package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.service.IBookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CatalogControllerMockTest {

    @InjectMocks
    private CatalogController catalogController;
    @Mock
    private IBookService bookService;
    @Spy
    private final Model model = new ExtendedModelMap();
    @Mock
    private BindingResult bindingResult;

    @Test
    public void getBookDto(){
        BookDto bookDto = new BookDto();
        Mockito.when(bookService.getBookDto()).thenReturn(bookDto);

        BookDto result = catalogController.getBookDto();

        Assert.assertNotNull(result);
        Assert.assertEquals(bookDto, result);

        Mockito.verify(bookService).getBookDto();
        Mockito.verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getMainPage() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());

        Mockito.when(bookService.findAll()).thenReturn(books);
        String result = catalogController.getMainPage(Boolean.TRUE, 1,model);
        Assert.assertNotNull(result);
        Assert.assertEquals("catalog", result);

        List<Book> resBooks = (List<Book>) model.getAttribute("books");
        Assert.assertNotNull(resBooks);
        Assert.assertEquals(books, resBooks);
        Assert.assertTrue((Boolean) model.getAttribute("result"));
        Assert.assertEquals(model.getAttribute("card"), 1);

        Mockito.verify(bookService, Mockito.times(1)).findAll();
        Mockito.verify(model, Mockito.times(1)).
                addAttribute(any(String.class), any(List.class));
        Mockito.verify(model, Mockito.times(1)).
                addAttribute(any(String.class), any(Boolean.class));
        Mockito.verify(model, Mockito.times(1)).
                addAttribute(any(String.class), any(Integer.class));
    }

    @Test
    public void selectBook(){

        /**
         * when all done correctly
         */
        Mockito.when(bookService.addReaderForBook(anyInt(), anyInt())).thenReturn(true);
        String result = catalogController.selectBook(1, 1, model);
        Assert.assertNotNull(result);
        Assert.assertEquals("catalog", result);
        Assert.assertNotNull(model.getAttribute("result"));
        Assert.assertEquals(Boolean.TRUE ,model.getAttribute("result"));
        //getMainPage
        List<Book> resBooks = (List<Book>) model.getAttribute("books");
        Assert.assertNotNull(resBooks);
        Assert.assertTrue(resBooks.isEmpty());

        /**
         * when addReaderForBook give badResult
         */
        Mockito.when(bookService.addReaderForBook(anyInt(), anyInt())).thenReturn(false);
        result = catalogController.selectBook(1, 1, model);
        Assert.assertNotNull(result);
        Assert.assertEquals("catalog", result);
        Assert.assertNotNull(model.getAttribute("result"));
        Assert.assertEquals(Boolean.FALSE ,model.getAttribute("result"));
        //getMainPage
        resBooks = (List<Book>) model.getAttribute("books");
        Assert.assertNotNull(resBooks);
        Assert.assertTrue(resBooks.isEmpty());

        //first is when all done correctly, second is when had badResult
        Mockito.verify(model, Mockito.times(2)).
                addAttribute(any(String.class), any(Boolean.class));
        Mockito.verify(model, Mockito.times(2)).
                addAttribute(any(String.class), any(List.class));
        Mockito.verify(bookService, Mockito.times(2)).
                addReaderForBook(anyInt(), anyInt());
    }

    @Test
    public void searchBooksTest(){
        BookDto book = new BookDto();
        List<Book> list = new ArrayList<>();
        list.add(new Book());

        /**
         * all done correctly
         */
        Mockito.when(bookService.searchBooks(book)).thenReturn(list);

        String result = catalogController.searchBooks(book, model, bindingResult);
        Assert.assertNotNull(result);
        Assert.assertEquals("catalog", result);
        List<Book> resBooks = (List<Book>) model.getAttribute("books");
        Assert.assertNotNull(resBooks);
        Assert.assertFalse(resBooks.isEmpty());

        /**
         * when bindingResult has any errors
         */
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        result = catalogController.searchBooks(book, model, bindingResult);
        Assert.assertNotNull(result);
        Assert.assertEquals("error", result);

        Mockito.verify(bindingResult, Mockito.times(2)).hasErrors();
        Mockito.verify(model, Mockito.times(1)).
                addAttribute(any(String.class), any(List.class));
        Mockito.verify(bookService, Mockito.times(1)).
                searchBooks(any(BookDto.class));

    }
}
