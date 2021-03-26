package com.epam.brest.dao.jdbc;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.IReader;
import com.epam.brest.model.ReaderProxy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml",
        "classpath*:dao.xml"})
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
    public void existReader(){
        Book book = bookDaoSpringJdbc.findBookById(1).get();
        Assert.assertNotNull(book);
        int res = bookDaoSpringJdbc.addReaderForBook(book, 1);
        Assert.assertTrue(res == 1);
        boolean result = bookDaoSpringJdbc.existReader(1);
        Assert.assertTrue(result);

    }

    @Test
    public void deleteTest() {
        Book book = new Book("Zosimov Dima", "JdbcTemplate", Genre.SCI_FI);
        book = bookDaoSpringJdbc.save(book);
        Assert.assertNotNull(book.getId());
        Assert.assertTrue(book.getId() > 0);

        Integer res = bookDaoSpringJdbc.delete(book.getId());
        Assert.assertFalse(bookDaoSpringJdbc.exist(book));
        Assert.assertTrue(res == 1);
    }

    @Test
    public void testAddReaderForBook(){
        List<Book> books = bookDaoSpringJdbc.findAll();
        Assert.assertNotNull(books);
        Assert.assertTrue(books.size() > 0);

        Book book = books.get(0);
        int res = bookDaoSpringJdbc.addReaderForBook(book, 1);
        Assert.assertTrue(res == 1);
        book = bookDaoSpringJdbc.findBookById(book.getId()).get();
        Assert.assertNotNull(book);
        Assert.assertEquals(book.getReader().getReaderId(), Integer.valueOf(1));
    }

    @Test
    public void testRemoveReaderFromBook(){
        List<Book> books = bookDaoSpringJdbc.findAll();
        Assert.assertNotNull(books);
        Assert.assertTrue(books.size() > 0);
        for(Book book : books) {
            int res = bookDaoSpringJdbc.addReaderForBook(book, 1);
            Assert.assertTrue(res == 1);
        }

        int res = bookDaoSpringJdbc.removeReaderFromBook(1);
        Assert.assertTrue(res >= 1);
        Assert.assertTrue(res == books.size());
        books = bookDaoSpringJdbc.findAll();
        for(Book book : books){
            Assert.assertNull(book.getReader());
        }



    }

}