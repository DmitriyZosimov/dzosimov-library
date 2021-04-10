package com.epam.brest.service;

import com.epam.brest.dao.jdbc.BookDaoSpringJdbc;
import com.epam.brest.dao.jdbc.ReaderDaoSpringJdbc;
import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.testdb.SpringTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = SpringTestConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.testdb", "com.epam.brest.dao"})
@Import({BookServiceImp.class, BookDaoSpringJdbc.class, ReaderServiceImp.class, ReaderDaoSpringJdbc.class})
@PropertySource({"classpath:dao.properties"})
@Transactional
public class BookServiceIT {
    @Autowired
    private IBookService bookService;
    @Autowired
    private IReaderService readerService;

    @Test
    public void shouldFindAll(){
        List<BookSample> books = bookService.findAll();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertNotNull(books.get(0).getQuantity());
    }

    /*
    согласно тестовой базе данных book c id=1 имеет 5 экземпляров, 3 из которых уже заняты
    читателя (reader), т.е. две свободные. по тесту читатель id=1 забирает 4-ую книгу,
    потом 5-ую, а позже пытается 6-ую, которой нет, и поэтому возвращается false;
     */
    @Test
    public void shouldReaderTryGetFreeBookInDao(){
        Integer readerId = 1;
        Integer bookId = 1;
        assertTrue(bookService.addReaderForBook(readerId, bookId));
        assertTrue(bookService.addReaderForBook(readerId, bookId));
        assertFalse(bookService.addReaderForBook(readerId, bookId));
    }

    @Test
    public void shouldReturnFalseWhenBookWasNotFound(){
        Integer readerId = 1;
        Integer bookId = 9999;
        assertFalse(bookService.addReaderForBook(readerId, bookId));
    }

    @Test
    public void shouldReturnFalseWhenReaderWasNotFound(){
        Integer readerId = 99999;
        Integer bookId = 1;
        assertFalse(bookService.addReaderForBook(readerId, bookId));
    }

    @Test
    public void shouldDeleteBook(){
        Integer bookId = 1;
        BookSample book = bookService.findBookById(bookId);
        assertNotNull(book);
        assertEquals(bookId, book.getId());

        Boolean result = bookService.delete(bookId);
        assertTrue(result);
        book = bookService.findBookById(bookId);
        assertNull(book);
    }

    @Test
    public void shouldCreateBook() {
        BookSample bookSample = new BookSample("authors", "title", Genre.MYSTERY);
        assertTrue(bookService.createBook(bookSample));

        SearchBookSample searchBookSample = new SearchBookSample(bookSample.getAuthors(),
                bookSample.getTitle(), bookSample.getGenre());
        List<BookSample> books = bookService.searchBooks(searchBookSample);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(bookSample.getAuthors(), books.get(0).getAuthors());
        assertEquals(bookSample.getTitle(), books.get(0).getTitle());
        assertEquals(bookSample.getGenre(), books.get(0).getGenre());
    }

    @Test
    public void shouldSearchBook() {
        SearchBookSample searchBookSample = new SearchBookSample("о", "", Genre.DEFAULT);
        List<BookSample> books = bookService.searchBooks(searchBookSample);
        assertFalse(books.isEmpty());
    }

    @Test
    public void removeFieldReaderFromBook() {
        Integer readerId = 1;
        Integer bookId = 1;
        ReaderSample reader = readerService.getProfile(1);
        assertTrue(bookService.addReaderForBook(readerId, bookId));
        assertTrue(bookService.removeFieldReaderFromBook(bookId, readerId));
         reader = readerService.getProfile(1);
        reader.getBooks().forEach(book -> assertNotEquals(bookId, book.getId()));
    }

    @Test
    public void shouldNotRemoveFieldReaderFromBook() {
        Integer readerId = 1;
        Integer bookId = 199999999;
        assertFalse(bookService.removeFieldReaderFromBook(bookId, readerId));

        bookId = 1;
        readerId = 199999999;
        assertFalse(bookService.removeFieldReaderFromBook(bookId, readerId));
    }

    @Test
    public void shouldReturnTrueAfterEditBook(){
        BookSample bookSample = bookService.findBookById(1);
        assertNotNull(bookSample);
        assertEquals(1,bookSample.getId());
        bookSample.setAuthors("test");
        Boolean result = bookService.editBook(bookSample);
        assertTrue(result);
        BookSample updatedBookSample = bookService.findBookById(1);
        assertNotNull(updatedBookSample);
        assertEquals(1,updatedBookSample.getId());
        assertEquals("test", updatedBookSample.getAuthors());
    }

    @Test
    public void shouldFindBookById(){
        BookSample bookSample = bookService.findBookById(1);
        assertNotNull(bookSample);
        assertEquals(1,bookSample.getId());
    }

    @Test
    public void shouldNotFindBookById(){
        BookSample bookSample = bookService.findBookById(9999999);
        assertNull(bookSample);
    }

}
