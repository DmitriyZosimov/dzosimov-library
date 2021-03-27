package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.exception.BookCreationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerMockTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private IBookService bookService;
    @Spy
    private final Model model = new ExtendedModelMap();
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

    @Test
    public void addBookGetRequest(){
        String resultPage = bookController.addBook(model);
        Assert.assertNotNull(resultPage);
        Assert.assertEquals("book", resultPage);
        Assert.assertTrue((Boolean) model.getAttribute("isNew"));

        Mockito.verify(model).addAttribute(eq("isNew"), anyBoolean());
    }

//    @Test
//    public void addBookPostRequest() throws BookCreationException {
//        BookSample bookSample = new BookSample();
//
//        //all done correctly
//        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
//        String resultPage = bookController.addBook(bookSample, model, bindingResult,request);
//        Assert.assertNotNull(resultPage);
//        Assert.assertEquals("redirect:/catalog", resultPage);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//
//        //has any errors in BindingResult
//        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
//        resultPage = bookController.addBook(bookSample, model, bindingResult,request);
//        Assert.assertNotNull(resultPage);
//        Assert.assertEquals("error", resultPage);
//
//        // 2 times because 2 times used bookService during this test
//        Mockito.verify(bookService, Mockito.times(1)).createBook(any(BookSample.class));
//        Mockito.verify(model, Mockito.times(1)).addAttribute(any(String.class), anyBoolean());
//        Mockito.verify(bindingResult, Mockito.times(2)).hasErrors();
//    }

    //TODO: edit
//    @Test
//    public void deleteBook(){
//
//        /**
//         * when all done correctly
//         */
//        Mockito.when(bookService.delete(anyInt())).thenReturn(true);
//        String result = bookController.deleteBook(1, model, request);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/result", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//
//        /**
//         * when addReaderForBook give badResult
//         */
//        Mockito.when(bookService.delete(anyInt())).thenReturn(false);
//        result = bookController.deleteBook(1, model, request);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/result", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertFalse((Boolean) model.getAttribute("result"));
//
//        //first is when all done correctly, second is when had badResult
//        Mockito.verify(model, Mockito.times(2)).
//                addAttribute(any(String.class), any(Boolean.class));
//        Mockito.verify(bookService, Mockito.times(2)).
//                delete(anyInt());
//    }

}
