package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.tools.BookMapper;
import com.epam.brest.service.exception.BookCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookServiceImp")
public class BookServiceImp implements IBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImp.class);

    public BookDao bookDao;

    @Autowired
    public BookServiceImp(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    /**
     * Create BookDto
     * @return BookDto
     */
    @Override
    public BookDto getBookDto() {
        LOGGER.info("Create BookDto");
        return new BookDto();
    }

    /**
     * Find all books
     * @return list of books
     */
    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public boolean addReaderForBook(Integer readerId, Integer bookId) {
        return false;
    }

    @Override
    public boolean delete(Integer bookId) {
        int result = bookDao.delete(bookId);
        if(result == 1){
            LOGGER.info("The book(bookId={}) was removed", bookId);
            return true;
        } else if(result > 1){
            LOGGER.warn("result={} > 1", result);
            return true;
        } else {
            LOGGER.info("The book(bookId={}) was not removed", bookId);
            return false;
        }
    }

    /**
     * Create a new book
     * @param bookDto model of a Book
     * @return
     */
    public Book createBook(BookDto bookDto) throws BookCreationException {
        Book book = BookMapper.getBook(bookDto);
        book = bookDao.save(book);
        if(book.getId() == null){
            throw new BookCreationException("book id must be not null");
        }
        return book;
    }

    @Override
    public List<Book> searchBooks(BookDto bookDto) {
        List<Book> books = bookDao.searchBooks(bookDto);
        return books;
    }
}
