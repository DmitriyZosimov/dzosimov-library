package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.dto.BookDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:service-context.xml", "classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml"})
public class BookServiceImpTest {

    @Autowired
    private BookServiceImp bookService;

    @Test
    public void findSearchBooksTest(){
        BookDto bookDto = new BookDto("о", "о", Genre.DEFAULT);
        List<Book> bigBooks = bookService.findAll();
        List<Book> books = bookService.searchBooks(bookDto);
        Assert.assertFalse(books.isEmpty());
    }
}
