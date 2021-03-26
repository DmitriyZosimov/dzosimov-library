package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional()
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:service-context.xml", "classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml"})
public class BookServiceImpTest {

    @Autowired
    private BookServiceImp bookService;

    @Test
    public void findSearchBooksTest(){
        BookSample bookSample = new BookSample("о", "о", Genre.DEFAULT);
        List<Book> bigBooks = bookService.findAll();
        List<Book> books = bookService.searchBooks(bookSample);
        Assert.assertFalse(books.isEmpty());
    }

    @Test
    public void addReaderForBookTest(){
        Assert.assertTrue(bookService.addReaderForBook(1, 1));
    }

    @Test
    public void addReaderForBookWhenReaderNotNullTest(){
        Book book = bookService.findBookById(1);
        Assert.assertNotNull(book);
        Assert.assertEquals(Integer.valueOf(1), book.getId());
        if(book.getReader() == null){
            Assert.assertTrue(bookService.addReaderForBook(1, book.getId()));
        }

        Assert.assertFalse(bookService.addReaderForBook(2, 1));
    }

    @Test
    public void removeFieldReaderFromBookTest(){
        Book book = bookService.findBookById(1);
        Assert.assertNotNull(book);
        Assert.assertEquals(Integer.valueOf(1), book.getId());
        if(book.getReader() == null){
            Assert.assertTrue(bookService.addReaderForBook(1, book.getId()));
            book = bookService.findBookById(1);
        }
        Assert.assertTrue(bookService.removeFieldReaderFromBook(1, 1));
        book = bookService.findBookById(1);
        Assert.assertNull(book.getReader());
    }

    @Test
    public void removeFieldReaderFromBookWhenBookHasReaderIdNullTest(){
        Assert.assertFalse(bookService.removeFieldReaderFromBook(1, 1));
    }
}
