package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
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
    public void findAllTest(){
        List<Book> books = bookService.findAll();
        Assert.assertNotNull(books);
        Assert.assertFalse(books.isEmpty());
    }

    @Test
    public void findSearchBooksTest(){
        SearchBookSample bookSample = new SearchBookSample("о", "о", Genre.DEFAULT);
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
        int bookId = 0;
        boolean isAdded = false;
        while(!isAdded){
            bookId++;
            isAdded = bookService.addReaderForBook(1, bookId);
        }

        Assert.assertFalse(bookService.addReaderForBook(2, bookId));
    }

    @Test
    public void removeFieldReaderFromBookTest(){
        int bookId = 0;
        boolean isAdded = false;
        while(!isAdded){
            bookId++;
            isAdded = bookService.addReaderForBook(1, bookId);
        }
        Assert.assertTrue(bookService.removeFieldReaderFromBook(bookId, 1));
    }

    @Test
    public void removeFieldReaderFromBookWhenBookHasReaderIdNullTest(){
        Assert.assertFalse(bookService.removeFieldReaderFromBook(1, 1));
    }
}
