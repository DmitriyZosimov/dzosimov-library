package com.epam.brest.dao;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:test-db.xml"})
public class TestBookDao {

    @Autowired
    private BookDao bookDao;

    @Test
    public void testFindAllBooks(){
        List<Book> books = bookDao.findAll();
        Assert.assertTrue(books.size() > 0);
    }

    @Test
    public void testInsertNewBook(){
        Book book = new Book("Решад Гюнтекен", "Птичка певчая", Genre.ROMANCE);
        book = bookDao.save(book);
        Assert.assertTrue(book.getId() != null);
    }

    @Test
    public void testUpdateBook(){
        Book book = bookDao.findById(1).get();
        Genre genre = book.getGenre();
        book.setGenre(Genre.ADVENTURE);
        book = bookDao.save(book);
        Assert.assertNotEquals(genre, book.getGenre());
    }

    @Test
    public void testDeleteBook(){
        Book book = new Book("Решад", "Птичка", Genre.ADVENTURE);
        book = bookDao.save(book);
        Assert.assertTrue(bookDao.existsById(book.getId()));
        bookDao.delete(book);
        Assert.assertFalse(bookDao.existsById(book.getId()));
    }
}
