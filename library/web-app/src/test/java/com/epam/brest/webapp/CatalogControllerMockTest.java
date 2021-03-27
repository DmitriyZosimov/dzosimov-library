package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.service.IBookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    @Mock
    private SessionLocaleResolver localeResolver;
    @Mock
    private MessageSource messageSource;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    @Captor
    private ArgumentCaptor<String> messageCodeCaptor;

    @Test
    public void test(){

    }

//    @Before
//    public void beforeLoadClass(){
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        Mockito.when(localeResolver.resolveLocale(any(HttpServletRequest.class))).thenReturn(Locale.ENGLISH);
//        Mockito.when(messageSource.getMessage(any(String.class), any(), any(Locale.class))).thenReturn("message");
//    }
//
//    @Test
//    public void getBookDto(){
//        BookSample bookSample = new BookSample();
//        Mockito.when(bookService.getBookSample()).thenReturn(bookSample);
//
//        BookSample result = catalogController.getBookSample();
//
//        Assert.assertNotNull(result);
//        Assert.assertEquals(bookSample, result);
//
//        Mockito.verify(bookService).getBookSample();
//        Mockito.verifyNoMoreInteractions(bookService);
//    }
//
//    @Test
//    public void getMainPage() {
//        List<Book> books = new ArrayList<>();
//        books.add(new Book());
//
//        Mockito.when(bookService.findAll()).thenReturn(books);
//        String result = catalogController.getMainPage(Boolean.TRUE, 1,model);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("catalog", result);
//
//        List<Book> resBooks = (List<Book>) model.getAttribute("books");
//        Assert.assertNotNull(resBooks);
//        Assert.assertEquals(books, resBooks);
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//        Assert.assertEquals(model.getAttribute("card"), 1);
//
//        Mockito.verify(bookService, Mockito.times(1)).findAll();
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(List.class));
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(Boolean.class));
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(Integer.class));
//    }
//
//    @Test
//    public void selectBookWhenAllDoneCorrectly(){
//        Mockito.when(bookService.addReaderForBook(anyInt(), anyInt())).thenReturn(true);
//
//        String result = catalogController.selectBook(1, model, session, request);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("catalog", result);
//
//        Assert.assertNotNull(model.getAttribute("resultMessage"));
//        Assert.assertEquals("message" ,model.getAttribute("resultMessage"));
//
//        Mockito.verify(messageSource).getMessage(messageCodeCaptor.capture(), any(), any());
//        String messageCode = messageCodeCaptor.getValue();
//        Assert.assertEquals("message.select.good", messageCode);
//
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(String.class));
//        Mockito.verify(bookService, Mockito.times(1)).
//                addReaderForBook(anyInt(), anyInt());
//    }
//
//    @Test
//    public void selectBookWhenDoneWrong(){
//        Mockito.when(bookService.addReaderForBook(anyInt(), anyInt())).thenReturn(false);
//
//        String result = catalogController.selectBook(1, model, session, request);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("catalog", result);
//
//        Assert.assertNotNull(model.getAttribute("resultMessage"));
//        Assert.assertEquals("message" ,model.getAttribute("resultMessage"));
//
//        Mockito.verify(messageSource).getMessage(messageCodeCaptor.capture(), any(), any());
//        String messageCode = messageCodeCaptor.getValue();
//        Assert.assertEquals("message.select.bad", messageCode);
//
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(String.class));
//        Mockito.verify(bookService, Mockito.times(1)).
//                addReaderForBook(anyInt(), anyInt());
//    }
//
//    @Test
//    public void searchBooksTest(){
//        BookSample book = new BookSample();
//        List<Book> list = new ArrayList<>();
//        list.add(new Book());
//
//        /**
//         * all done correctly
//         */
//        Mockito.when(bookService.searchBooks(book)).thenReturn(list);
//
//        String result = catalogController.searchBooks(book, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("catalog", result);
//        List<Book> resBooks = (List<Book>) model.getAttribute("books");
//        Assert.assertNotNull(resBooks);
//        Assert.assertFalse(resBooks.isEmpty());
//
//        /**
//         * when bindingResult has any errors
//         */
//        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
//        result = catalogController.searchBooks(book, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("error", result);
//
//        Mockito.verify(bindingResult, Mockito.times(2)).hasErrors();
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(List.class));
//        Mockito.verify(bookService, Mockito.times(1)).
//                searchBooks(any(BookSample.class));
//
//    }
//    //TODO: added resultTest methods and refactor this tests method
}
