package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.jdbc.BookDaoSpringJdbc;
import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml"})
public class BookDaoSpringJdbcTest {

    @Autowired
    private BookDaoSpringJdbc bookDaoSpringJdbc;

    @Test
    public void findAllTest() {
        List<Book> books = bookDaoSpringJdbc.findAll();
        Assert.assertNotNull(books);
        Assert.assertTrue(books.size() > 0);
    }

    @Test
    public void findBookByIdTest() {
        Optional<Book> optBook = bookDaoSpringJdbc.findBookById(2);
        Assert.assertNotNull(optBook.get());
        Assert.assertTrue(optBook.get().getId() == 2);
    }

    @Test
    public void saveTest() {
        Book book = new Book("Zosimov Dima", "JdbcTemplate", Genre.SCI_FI);
        book = bookDaoSpringJdbc.save(book);
        Assert.assertNotNull(book.getId());
        Assert.assertTrue(book.getId() > 0);
    }

    @Test
    public void updateTest(){
        Book book = new Book("Zosimov Dima", "JdbcTemplate", Genre.SCI_FI);
        book = bookDaoSpringJdbc.save(book);
        Assert.assertNotNull(book.getId());
        Assert.assertTrue(book.getId() > 0);

        book.setAuthors("Алдуин");
        book.setTitle("История клинков");
        book.setGenre(Genre.HISTORY);
        Integer res = bookDaoSpringJdbc.update(book);
        Assert.assertTrue(res == 1);
        Assert.assertEquals(book, bookDaoSpringJdbc.findBookById(book.getId()).get());
    }

    @Test
    public void existTest(){
        Book book = new Book("Zosimov Dima", "JdbcTemplate", Genre.SCI_FI);
        book = bookDaoSpringJdbc.save(book);
        Assert.assertNotNull(book.getId());
        Assert.assertTrue(book.getId() > 0);

        Assert.assertTrue(bookDaoSpringJdbc.exist(book));
        book.setId(15);
        Assert.assertFalse(bookDaoSpringJdbc.exist(book));
    }

    @Test
    public void deleteTest() {
        Book book = new Book("Zosimov Dima", "JdbcTemplate", Genre.SCI_FI);
        book = bookDaoSpringJdbc.save(book);
        Assert.assertNotNull(book.getId());
        Assert.assertTrue(book.getId() > 0);

        Integer res = bookDaoSpringJdbc.delete(book);
        Assert.assertFalse(bookDaoSpringJdbc.exist(book));
        Assert.assertTrue(res == 1);
    }

}